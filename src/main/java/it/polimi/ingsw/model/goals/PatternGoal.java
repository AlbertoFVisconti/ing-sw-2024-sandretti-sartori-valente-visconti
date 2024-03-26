package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.CardLocation;

import java.awt.*;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.List;


/**
 * Represents a goal based on matching a specific pattern of resources on the player's board.
 * This goal computes the score based on how many times the pattern is found in the player's board
 */
public class PatternGoal implements Goal {
    private final int scorePerMatch;
    private final Resource[][] pattern;

    private Point pivot;

    /**
     * Constructs a new PatternGoal with the specified pattern and score per match.
     *
     * @param pattern       pattern of resources to match on the player's board.
     * @param scorePerMatch score awarded per matching pattern.
     */
    public PatternGoal(Resource[][] pattern, int scorePerMatch) {
        this.scorePerMatch = scorePerMatch;
        this.pattern = pattern.clone();

        pivot = null;
        for(int i = 0; i < pattern.length && pivot == null; i++) {
            for(int j = 0; j < pattern[i].length && pivot == null; j++) {
                if (pattern[i][j] != null) {
                    this.pivot = new Point(j,i);
                }
            }
        }

        if (pivot == null) throw new InvalidParameterException("Pattern needs to contain at least one valid resource");
    }

    /**
     * Evaluates the score obtained by the player based on how many times the pattern is found in the player's board.
     *
     * @param player Player whose board needs to be evaluated.
     * @return the score obtained by the player.
     */
    @Override
    public int evaluate(Player player) {
        Map<CardLocation, Card> board = player.getBoard();

        List<GraphNode> matches = new ArrayList<>();

        for(CardLocation p : board.keySet()) {
            Card c = board.get(p);

            if(c instanceof PlayCard && ((PlayCard) c).getType().equals(pattern[pivot.y][pivot.x])) {
                // the card at location p is compatible with the pattern's pivot, I check if there's a match
                CardLocation startingCandidateCell = p.sub(pivot.x, -2*pivot.y - pivot.x);

                if(isMatch(board, startingCandidateCell)) {
                    // there's a match, I add the Node to the list
                    matches.add(new GraphNode(startingCandidateCell));
                }
            }
        }

        // I build the proximity lists
        for(int i = 0; i < matches.size(); i++) {
            for(int j = i+1; j < matches.size(); j++) {
                GraphNode n1 = matches.get(i);
                GraphNode n2 = matches.get(j);

                if(conflict(n1.matchStartingCard, n2.matchStartingCard)) {
                    // the two match overlap thus I add the arc to the graph
                    n1.conflictingNodes.add(n2);
                    n2.conflictingNodes.add(n1);
                }
            }
        }

        int validMatches = 0;
        for (Iterator<GraphNode> iterator = matches.iterator(); iterator.hasNext(); ) {
            GraphNode node = iterator.next();
            if(node.conflictingNodes.isEmpty()) {
                validMatches++;
                iterator.remove();
            }
        }

        List<List<GraphNode>> groups = splitInConnectedGroups(matches);

        for(List<GraphNode> group : groups) {
            if(group.size() > 30) {
                System.err.println("A large ("+group.size()+") group of conflicting matches has been found; long elaboration time's is to be expected (you might want to kill if the size is beyond 50)");
            }
            minimumVertexSize(group, 0, new HashSet<>());
            validMatches += (group.size() - bound);
        }

        return validMatches*scorePerMatch;
    }

    /**
     * Splits the graph list in groups of connected nodes.
     *
     * @param graph the List of node.
     * @return a list of groups of node (list of list).
     */
    private static List<List<GraphNode>> splitInConnectedGroups(List<GraphNode> graph) {
        Set<GraphNode> checked = new HashSet<>();

        List<List<GraphNode>> ans = new ArrayList<>();

        for(GraphNode node : graph) {
            if(!checked.contains(node)) {
                List<GraphNode> group = new ArrayList<>();
                dfs(node, group);

                checked.addAll(group);

                ans.add(group);
            }
        }

        return ans;
    }

    /**
     * Performs the depth-first-search algorithm and fill the provided list with the reached nodes.
     *
     * @param startingNode the node to start the algorithm on.
     * @param group the List to store the reached notes.
     */
    private static void dfs(GraphNode startingNode, List<GraphNode> group) {
        group.add(startingNode);

        for(GraphNode neighbour : startingNode.conflictingNodes) {
            if(!group.contains(neighbour)) dfs(neighbour, group);
        }
    }

    private static int bound;

    /**
     * Compute the exact size of the Minimum Vertex Cover of the provided graph.
     *
     *
     * @param graph the graph to use for the computation
     * @param i the current checked node (when called, always 0)
     * @param removed the set of already removed nodes
     */
    private static void minimumVertexSize(List<GraphNode> graph, int i, Set<GraphNode> removed) {
        if(i == 0) bound = 2000000000;
        if (i >= graph.size()) {
            bound = Math.min(bound, removed.size());
            return;
        }

        if(removed.size() >= bound) return;

        if (removed.contains(graph.get(i)))  {
            PatternGoal.minimumVertexSize(graph, i+1,removed);
            return;
        }


        if(graph.get(i).conflictingNodes.isEmpty())  {
            PatternGoal.minimumVertexSize(graph, i+1, removed);
            return;
        }


        removed.add(graph.get(i));
        minimumVertexSize(graph, i+1,removed);
        removed.remove(graph.get(i));

        Set<GraphNode> removedThisTime = new HashSet<>();
        for(GraphNode neighbour : graph.get(i).conflictingNodes) {
            if(!removed.contains(neighbour)) {
                removedThisTime.add(neighbour);
            }
        }

        removed.addAll(removedThisTime);
        minimumVertexSize(graph, i+1, removed);
        removed.removeAll(removedThisTime);
    }

    /**
     * Support class that represents the matches' graph's node.
     * Each node is identified by a starting Card's Locatino (the card in the top-left of the pattern, that could be null)
     * and by the proximity list of connected (conflicting) nodes (matches)
     */
    private static class GraphNode {
        private final CardLocation matchStartingCard;
        private final List<GraphNode> conflictingNodes;

        /**
         * Build a GraphNode representing the match found in the provided location.
         * It initializes the proximity list as empty.
         *
         * @param matchStartingCard the card.
         */
        private GraphNode(CardLocation matchStartingCard) {
            this.matchStartingCard = matchStartingCard;
            this.conflictingNodes = new ArrayList<>();
        }

        @Override
        public String toString() {
            return matchStartingCard.toString() + " {" + conflictingNodes.stream().map((GraphNode n) -> {return n.matchStartingCard.toString();} ).reduce("", String::concat) + "}";
        }
    }

    /**
     * Checks whether a certain location of the board contain a pattern match.
     * <p>
     * Helper method to support the goal evaluation.
     *
     * @param board the player's board
     * @param startingCandidateCell the top-left cell of the candidate match
     * @return {@code true} if the pattern is found, {@code false} otherwise
     */
    private boolean isMatch(Map<CardLocation, Card> board, CardLocation startingCandidateCell) {
        for(int i = 0; i < pattern.length; i++) {
            for(int j = 0; j < pattern[0].length; j++) {
                if(pattern[i][j] != null) {
                    Card c = board.get(startingCandidateCell.add(j, -i*2-j));

                    if(c == null) {
                        return false;
                    }
                    if ((c instanceof PlayCard) && !((PlayCard) c).getType().equals(pattern[i][j])) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Returns true if the matches represented by the provided CardLocations (starting cell of the match)
     * overlap.
     * <p>
     * Helper method to support the goal evaluation.
     *
     * @param a the CardLocation representing the top-left cell of the first match.
     * @param b the CardLacation representing the top-left cell of the second match.
     * @return {@code true} if the matches overlap, {@code false} otherwise.
     */
    private boolean conflict(CardLocation a, CardLocation b) {
        CardLocation dist = a.sub(b);
        if(Math.abs(dist.x) >= this.pattern[0].length) return false;

        // this can be improved, just a naive implementation to speed things up
        Set<CardLocation> locations = new HashSet<>();

        for(int i = 0; i < pattern.length; i++) {
            for(int j = 0; j < pattern[0].length; j++) {
                if(pattern[i][j] != null) {
                    locations.add(a.add(j, -i * 2 - j));
                }
            }
        }

        for(int i = 0; i < pattern.length; i++) {
            for(int j = 0; j < pattern[0].length; j++) {
                if(locations.contains(b.add(j, -i*2-j))) {
                    if(pattern[i][j] != null) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "PatternGoal{" +
                "scorePerMatch=" + scorePerMatch +
                ", pattern=" + Arrays.deepToString(pattern) +
                '}';
    }
}
