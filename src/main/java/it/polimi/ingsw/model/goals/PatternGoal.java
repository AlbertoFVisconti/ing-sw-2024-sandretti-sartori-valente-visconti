package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.player.Player;

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
        Map<Point, Card> board = player.getBoard();

        List<GraphNode> matches = new ArrayList<>();

        for(Point p : board.keySet()) {
            Card c = board.get(p);

            if(c instanceof PlayCard && ((PlayCard) c).getType().equals(pattern[pivot.y][pivot.x])) {
                // the card at location p is compatible with the pattern's pivot, I check if there's a match
                Point startingCandidateCell = new Point(subPoints(p, new Point(pivot.x, -2*pivot.y - pivot.x)));
                if(isMatch(board, new Point(startingCandidateCell))) {
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

        matches.sort(
                (GraphNode a, GraphNode b) ->{
                    if(a.matchStartingCard.y == b.matchStartingCard.y) {
                        return a.matchStartingCard.x - b.matchStartingCard.x;
                    }
                    return a.matchStartingCard.y - b.matchStartingCard.y;

                }
        );

        for(int i = 0; i < matches.size(); i++) {
            for (GraphNode n : matches.get(i).conflictingNodes) {
                matches.remove(n);
            }
        }

        return matches.size() * scorePerMatch;
    }

    /**
     * Support class that represents the matches' graph's node.
     * Each node is identified by a starting Card's point (the card in the top-left of the pattern, that could be null)
     * and by the proximity list of connected (conflicting) nodes (matches)
     */
    private static class GraphNode {
        private final Point matchStartingCard;
        private final List<GraphNode> conflictingNodes;

        /**
         * Build a GraphNode representing the match found in the provided location.
         * It initializes the proximity list as empty.
         *
         * @param matchStartingCard the card.
         */
        private GraphNode(Point matchStartingCard) {
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
    private boolean isMatch(Map<Point, Card> board, Point startingCandidateCell) {

        for(int i = 0; i < pattern.length; i++) {
            for(int j = 0; j < pattern[0].length; j++) {
                if(pattern[i][j] != null) {
                    Card c = board.get(addPoints(startingCandidateCell, new Point(j, -i * 2 - j)));
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
     * Returns true if the matches represented by the provided points (starting cell of the match)
     * overlap.
     * <p>
     * Helper method to support the goal evaluation.
     *
     * @param a the point representing the top-left cell of the first match.
     * @param b the point representing the top-left cell of the second match.
     * @return {@code true} if the matches overlap, {@code false} otherwise.
     */
    private boolean conflict(Point a, Point b) {
        Point dist = subPoints(a,b);
        if(dist.x >= this.pattern[0].length) return false;

        // this can be improved, just a naive implementation to speed things up
        Set<Point> points = new HashSet<>();

        for(int i = 0; i < pattern.length; i++) {
            for(int j = 0; j < pattern[0].length; j++) {
                points.add(addPoints(a, new Point(j, -i*2 - j)));
            }
        }

        for(int i = 0; i < pattern.length; i++) {
            for(int j = 0; j < pattern[0].length; j++) {
                if(points.contains(addPoints(b, new Point(j, -i*2 - j)))) {
                    if(pattern[i][j] != null) return true;
                }
            }
        }

        return false;
    }

    /**
     * Adds to points and create a new Point object containing the sum.
     * <p>
     * Helper method to support the goal evaluation.
     *
     * @param a the first point.
     * @param b the second point.
     * @return a new Point object representing the sum of the two provided points.
     */
    private static Point addPoints(Point a, Point b) {
        return new Point(a.x + b.x, a.y + b.y);
    }

    /**
     * Performs subtraction between two points and create a new Point object containing the result.
     * <p>
     * Helper method to support the goal evaluation.
     *
     * @param a the point to subtract from.
     * @param b the point to subtract.
     * @return a new Point object representing the subtraction of the two provided points.
     */
    private static Point subPoints(Point a, Point b) {
        return new Point(a.x - b.x, a.y - b.y);
    }

    @Override
    public String toString() {
        return "PatternGoal{" +
                "scorePerMatch=" + scorePerMatch +
                ", pattern=" + Arrays.deepToString(pattern) +
                '}';
    }
}
