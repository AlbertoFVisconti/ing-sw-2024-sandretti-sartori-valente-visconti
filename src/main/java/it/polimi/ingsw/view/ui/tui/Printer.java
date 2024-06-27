package it.polimi.ingsw.view.ui.tui;

import it.polimi.ingsw.model.ScoreBoard;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.CardSlot;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.chat.Chat;
import it.polimi.ingsw.model.chat.ChatMessage;
import it.polimi.ingsw.model.decks.Deck;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.goals.ItemGoal;
import it.polimi.ingsw.model.goals.PatternGoal;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.CardLocation;

import java.util.*;

/**
 * Printer is a helper class that the TextualUserInterface allows to print the game elements
 * like cards, goals, boards.
 */
public final class Printer {
    // some consoles don't support colors, ENABLE_COLORS allows to enable or disable them
    public final static boolean ENABLE_COLORS = false;

    // the width (in terms of characters) of the standard game card
    private final static int DEFAULT_CARD_WIDTH = 27;
    // the height (in terms of characters) of the standard game card
    private final static int DEFAULT_CARD_HEIGHT = 11;

    private final static int BOARD_WIDTH = 80;
    private final static int BOARD_HEIGHT = 50;

    // map that allows to retrieve a "graphic" representation (using strings) of card's corners
    private final static HashMap<Corner, String[]> cornerToStringsMap;

    // map that allows to retrieve the TextColor to use for a card's corner's content
    private final static HashMap<Corner, TextColor> cornerToColorMap;

    // loading the maps data
    static {
        cornerToStringsMap = new HashMap<>();
        cornerToColorMap = new HashMap<>();

        // ANIMAL corner
        cornerToStringsMap.put(
                Corner.ANIMAL,
                new String[]{
                        "/|_|\\",
                        "\\   /",
                        " \\_/ "
                }
        );

        // FUNGUS corner
        cornerToStringsMap.put(
                Corner.FUNGUS,
                new String[]{
                        " ___ ",
                        "/   \\",
                        " |_| "
                }
        );

        // PLANT corner
        cornerToStringsMap.put(
                Corner.PLANT,
                new String[]{
                        "  ^  ",
                        "_/|\\_",
                        "\\   /"
                }
        );

        // INSECT corner
        cornerToStringsMap.put(
                Corner.INSECT,
                new String[]{
                        "|\\\"/|",
                        "| _ |",
                        "|/ \\|"
                }
        );

        // INK corner
        cornerToStringsMap.put(
                Corner.INK,
                new String[]{
                        " ___ ",
                        " | | ",
                        "[___]"
                }
        );

        // SCROLL corner
        cornerToStringsMap.put(
                Corner.SCROLL,
                new String[]{
                        " ___ ",
                        "|   |",
                        "|___|"
                }
        );

        // FEATHER corner
        cornerToStringsMap.put(
                Corner.FEATHER,
                new String[]{
                        "   /\\",
                        "  / /",
                        " /-°"
                }
        );

        // EMPTY corner
        cornerToStringsMap.put(
                Corner.EMPTY,
                new String[]{
                        "     ",
                        "     ",
                        "     "
                }
        );

        // HIDDEN corner
        cornerToStringsMap.put(
                null,
                new String[]{
                        "     ",
                        "     ",
                        "     "
                }
        );


        // ANIMAL -> CYAN
        cornerToColorMap.put(
                Corner.ANIMAL,
                TextColor.CYAN
        );

        // PLANT -> GREEN
        cornerToColorMap.put(
                Corner.PLANT,
                TextColor.GREEN
        );

        // FUNGUS -> RED
        cornerToColorMap.put(
                Corner.FUNGUS,
                TextColor.RED
        );

        // INSECT -> PURPLE
        cornerToColorMap.put(
                Corner.INSECT,
                TextColor.PURPLE
        );

        // INK -> YELLOW
        cornerToColorMap.put(
                Corner.INK,
                TextColor.YELLOW
        );

        // FEATHER -> YELLOW
        cornerToColorMap.put(
                Corner.FEATHER,
                TextColor.YELLOW
        );

        // SCROLL -> YELLOW
        cornerToColorMap.put(
                Corner.SCROLL,
                TextColor.YELLOW
        );

        // EMPTY --> WHITE
        cornerToColorMap.put(
                Corner.EMPTY,
                TextColor.WHITE
        );

        // HIDDEN -> WHITE
        cornerToColorMap.put(
                null,
                TextColor.WHITE
        );
    }

    /**
     * Allows to generate a Canvas that contains aa card placeholder.
     *
     * @param cardLocation CardLocation that needs to be displayed in the placeholder
     * @return Canvas that contains an empty card's frame
     */
    private static Canvas getCardPlaceholder(CardLocation cardLocation) {
        Canvas canvas = new Canvas(DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT);

        // setting the deck frame

        canvas.putChar('╭', 0, 0);
        canvas.putChar('╮', DEFAULT_CARD_WIDTH - 1, 0);
        canvas.putChar('╰', 0, DEFAULT_CARD_HEIGHT - 1);
        canvas.putChar('╯', DEFAULT_CARD_WIDTH - 1, DEFAULT_CARD_HEIGHT - 1);

        for (int i = 1; i < DEFAULT_CARD_WIDTH - 1; i+=3) {
            canvas.putChar('─', i, 0);
            canvas.putChar('─', i, DEFAULT_CARD_HEIGHT - 1);
        }

        for (int i = 1; i < DEFAULT_CARD_HEIGHT - 1; i+=3) {
            canvas.putChar('│', 0, i);
            canvas.putChar('│', DEFAULT_CARD_WIDTH - 1, i);
        }

        String s = "(" + cardLocation.x() + ", " + cardLocation.y() + ")";

        canvas.putString(s,
                DEFAULT_CARD_WIDTH/2 - s.length()/2,
                DEFAULT_CARD_HEIGHT/2
                );

        return canvas;
    }

    /**
     * Allows to generate a Canvas that contains an empty card's frame.
     *
     * @return Canvas that contains an empty card's frame
     */
    private static Canvas getCardFrame() {
        Canvas canvas = new Canvas(DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT);

        // setting the deck frame

        canvas.putChar('╭', 0, 0);
        canvas.putChar('╮', DEFAULT_CARD_WIDTH - 1, 0);
        canvas.putChar('╰', 0, DEFAULT_CARD_HEIGHT - 1);
        canvas.putChar('╯', DEFAULT_CARD_WIDTH - 1, DEFAULT_CARD_HEIGHT - 1);

        for (int i = 1; i < DEFAULT_CARD_WIDTH - 1; i++) {
            canvas.putChar('─', i, 0);
            canvas.putChar('─', i, DEFAULT_CARD_HEIGHT - 1);
        }

        for (int i = 1; i < DEFAULT_CARD_HEIGHT - 1; i++) {
            canvas.putChar('│', 0, i);
            canvas.putChar('│', DEFAULT_CARD_WIDTH - 1, i);
        }

        return canvas;
    }

    /**
     * Allows to generate a Canvas that represents a deck
     *
     * @param deck the Deck whose representation is needed
     * @return Canvas containing a representation of the deck
     */
    private static Canvas getPlayCardDeckCanvas(Deck<PlayCard> deck) {
        if (deck == null) return new Canvas(0, 0);
        Canvas canvas = getCardFrame();

        canvas.setTextColor(cornerToColorMap.get(deck.getTopOfTheStack().getCorner()));
        canvas.putStringMatrix(cornerToStringsMap.get(deck.getTopOfTheStack().getCorner()), DEFAULT_CARD_WIDTH / 2 - 2, DEFAULT_CARD_HEIGHT / 2 - 1);
        canvas.resetColor();

        return canvas;
    }

    /**
     * Allows to generate a Canvas that represent the specified side
     * of a given card
     *
     * @param card Card whose representation is needed
     * @param onBackSide {@code true} if the back side of the card is needed, {@code false} otherwise
     * @return Canvas containing a representation of the card
     */
    private static Canvas getCardCanvas(Card card, boolean onBackSide) {
        if (card == null) return new Canvas(0, 0);

        Canvas canvas = getCardFrame();


        Corner ctl, ctr, cbl, cbr;
        ctl = card.getTopLeftCorner(onBackSide);
        ctr = card.getTopRightCorner(onBackSide);
        cbl = card.getBottomLeftCorner(onBackSide);
        cbr = card.getBottomRightCorner(onBackSide);

        if (ctl != null) {
            canvas.putString("─────", 1, 4);
        }
        if (ctr != null) {
            canvas.putString("─────", DEFAULT_CARD_WIDTH - 6, 4);
        }
        if (cbl != null) {
            canvas.putString("─────", 1, DEFAULT_CARD_HEIGHT - 5);
        }
        if (cbr != null) {
            canvas.putString("─────", DEFAULT_CARD_WIDTH - 6, DEFAULT_CARD_HEIGHT - 5);
        }


        if (ctl != null) {
            for (int i = 0; i < 3; i++) {
                canvas.putChar('│', 6, 1 + i);
            }
            canvas.putChar('╯', 6, 4);
            canvas.putChar('┬', 6, 0);

            canvas.setTextColor(cornerToColorMap.get(ctl));
            canvas.putStringMatrix(cornerToStringsMap.get(ctl), 1, 1);
            canvas.resetColor();
        }

        if (ctr != null) {
            for (int i = 0; i < 3; i++) {
                canvas.putChar('│', DEFAULT_CARD_WIDTH - 7, 1 + i);
            }
            canvas.putChar('╰', DEFAULT_CARD_WIDTH - 7, 4);
            canvas.putChar('┬', DEFAULT_CARD_WIDTH - 7, 0);

            canvas.setTextColor(cornerToColorMap.get(ctr));
            canvas.putStringMatrix(cornerToStringsMap.get(ctr), DEFAULT_CARD_WIDTH - 6, 1);
            canvas.resetColor();
        }

        if (cbl != null) {
            for (int i = 1; i < 4; i++) {
                canvas.putChar('│', 6, DEFAULT_CARD_HEIGHT - i - 1);
            }
            canvas.putChar('╮', 6, DEFAULT_CARD_HEIGHT - 5);
            canvas.putChar('┴', 6, DEFAULT_CARD_HEIGHT - 1);

            canvas.setTextColor(cornerToColorMap.get(cbl));
            canvas.putStringMatrix(cornerToStringsMap.get(cbl), 1, DEFAULT_CARD_HEIGHT - 3 - 1);
            canvas.resetColor();
        }

        if (cbr != null) {
            for (int i = 1; i < 4; i++) {
                canvas.putChar('│', DEFAULT_CARD_WIDTH - 7, DEFAULT_CARD_HEIGHT - i - 1);
            }
            canvas.putChar('╭', DEFAULT_CARD_WIDTH - 7, DEFAULT_CARD_HEIGHT - 5);
            canvas.putChar('┴', DEFAULT_CARD_WIDTH - 7, DEFAULT_CARD_HEIGHT - 1);

            canvas.setTextColor(cornerToColorMap.get(cbr));
            canvas.putStringMatrix(cornerToStringsMap.get(cbr), DEFAULT_CARD_WIDTH - 6, DEFAULT_CARD_HEIGHT - 3 - 1);
            canvas.resetColor();
        }

        if (card instanceof StartCard startCard && onBackSide) {

            int i = 0;
            List<Corner> corners = startCard.getPermanentResources().toList();
            int rows = (corners.size() / 2 + corners.size() % 2);
            for (Corner corner : corners) {
                canvas.setTextColor(cornerToColorMap.get(corner));
                canvas.putStringMatrix(cornerToStringsMap.get(corner), DEFAULT_CARD_WIDTH / 2 - 5 + 6 * (i % 2), DEFAULT_CARD_HEIGHT / 2 - (3 * rows + Math.max(0, rows - 1)) / 2 + 4 * (i / 2));
                canvas.resetColor();
                i++;
            }
        } else if (card instanceof PlayCard playCard) {

            if (onBackSide) {
                canvas.setTextColor(cornerToColorMap.get(playCard.getType().getCorner()));
                canvas.putStringMatrix(cornerToStringsMap.get(playCard.getType().getCorner()), DEFAULT_CARD_WIDTH / 2 - 2, DEFAULT_CARD_HEIGHT / 2 - 1);
                canvas.resetColor();
            } else {
                String scoring = playCard.getScoringStrategy().toString();

                canvas.putString(scoring, DEFAULT_CARD_WIDTH / 2 - scoring.length() / 2 + scoring.length() % 2, 1);

                List<Corner> constraints = playCard.getConstraint().toList();
                for (int i = 0; i < constraints.size(); i++) {
                    canvas.setTextColor(cornerToColorMap.get(constraints.get(i)));
                    canvas.putChar(constraints.get(i).toString().charAt(0), DEFAULT_CARD_WIDTH / 2 - constraints.size() / 2 + constraints.size() % 2 + i, DEFAULT_CARD_HEIGHT - 2);
                    canvas.resetColor();
                }

            }

            Corner cardType = playCard.getType().getCorner();
            canvas.setTextColor(cornerToColorMap.get(cardType));

            canvas.putString(
                    cardType.toString(),
                    DEFAULT_CARD_WIDTH/2 - cardType.toString().length()/2,
                    0
            );

            canvas.resetColor();
        }


        return canvas;
    }

    /**
     * Prints the specified side of the given Card.
     *
     * @param card card that needs to be printed
     * @param onBackSide {@code true} if the back side of the card is the one that needs to be printed, {@code false} otherwise
     */
    public static void printCard(Card card, boolean onBackSide) {
        System.out.println(getCardCanvas(card, onBackSide));
    }

    /**
     * Prints the player's hand.
     *
     * @param cards the list of cards that represents the player's hand that needs to be printed
     */
    public static void printHand(PlayCard[] cards) {
        final int SPACING_BETWEEN_CARDS = 2;
        Canvas canvas = new Canvas(DEFAULT_CARD_WIDTH * cards.length + SPACING_BETWEEN_CARDS * (cards.length - 1) + 2, DEFAULT_CARD_HEIGHT + 2);

        for (int i = 0; i < cards.length; i++) {
            canvas.putString("Card n°" + i, (DEFAULT_CARD_WIDTH + SPACING_BETWEEN_CARDS) * i + DEFAULT_CARD_WIDTH / 2 - 4, 0);

            canvas.putCanvas(getCardCanvas(cards[i], false), (DEFAULT_CARD_WIDTH + SPACING_BETWEEN_CARDS) * i, 2);
        }

        System.out.println(canvas);
    }

    /**
     * Checks if a card can be placed in a specific location on the player's board.
     *
     * @param board the player's board
     * @param cl the location that needs ot be checked
     * @return {@code true} of cards can be place in the provided location, {@code false} otherwise
     */
    private static boolean placeable(Map<CardLocation, CardSlot> board, CardLocation cl ){
        if(board.get(cl) != null) return false;

        if(board.containsKey(cl.bottomLeftNeighbour())
                && board.get(cl.bottomLeftNeighbour()).getTopRightCorner()==null)
            return false;
        if(board.containsKey(cl.topLeftNeighbour())
                && board.get(cl.topLeftNeighbour()).getBottomRightCorner()==null)
            return false;
        if(board.containsKey(cl.bottomRightNeighbour())
                && board.get(cl.bottomRightNeighbour()).getTopLeftCorner()==null )
            return false;
        return !board.containsKey(cl.topRightNeighbour())
                || board.get(cl.topRightNeighbour()).getBottomLeftCorner() != null;
    }

    private static final int[] POSS = {-1, 1};

    /**
     * Allows to compute the CardLocation where the user could place a card
     *
     * @param board the player's Board
     * @param cl the starting cardLocation (when calling 0,0)
     * @param seen the set of location that were already checked (provide empty set)
     * @param placeHolderLocations the set where the result of the computation will be put
     */
    private static void computePlaceHolders(Map<CardLocation, CardSlot> board , CardLocation cl, Set<CardLocation> seen, ArrayList<CardLocation> placeHolderLocations){
        if(board.containsKey(cl)){
            for(int i: POSS){
                for(int j: POSS){
                    CardLocation p = new CardLocation(cl.x() + i, cl.y() + j);
                    if (!seen.contains(p)) {
                        seen.add(p);
                        if(placeable(board,p)) placeHolderLocations.add(p);
                        computePlaceHolders(board, p, seen, placeHolderLocations);
                    }
                }
            }
        }
    }

    /**
     * Prints the player's board.
     *
     * @param board the Board that needs to be printed
     * @param xOffset the x offset (in terms of cards) of the center of the board's position
     * @param yOffset the y offset (in terms of cards) of the center of the board's position
     */
    public static void printBoard(Map<CardLocation, CardSlot> board, int xOffset, int yOffset) {
        HashMap<Integer, CardLocation> t = new HashMap<>();

        Canvas canvas = new Canvas(BOARD_WIDTH, BOARD_HEIGHT);

        int centerX = BOARD_WIDTH/2 - DEFAULT_CARD_WIDTH/2 - xOffset * (DEFAULT_CARD_WIDTH - 7);
        int centerY = BOARD_HEIGHT/2 - DEFAULT_CARD_HEIGHT/2 + yOffset * (DEFAULT_CARD_HEIGHT - 5);

        ArrayList<CardLocation> placeHolderLocations = new ArrayList<>();
        computePlaceHolders(board, new CardLocation(0,0), new HashSet<>(), placeHolderLocations);

        for(CardLocation cardLocation : placeHolderLocations) {
            int x, y;
            x = centerX + cardLocation.x() * (DEFAULT_CARD_WIDTH-7);
            y = centerY - cardLocation.y() * (DEFAULT_CARD_HEIGHT-5);

            canvas.putCanvas(
                    getCardPlaceholder(cardLocation),
                    x,y
            );
        }

        for (CardLocation cardLocation : board.keySet()) {
            t.put(board.get(cardLocation).placementTurn(), cardLocation);
        }

        int i = 0;
        while (t.get(i) != null) {
            int x, y;
            x = centerX + t.get(i).x() * (DEFAULT_CARD_WIDTH-7);
            y = centerY - t.get(i).y() * (DEFAULT_CARD_HEIGHT-5);

            canvas.putCanvas(
                    getCardCanvas(board.get(t.get(i)).card(), board.get(t.get(i)).onBackSide()),
                    x,y
            );

            i++;
        }



        for(i = 1; i < BOARD_WIDTH-1; i++) {
            canvas.putChar('─', i, 0);
            canvas.putChar('─', i, BOARD_HEIGHT-1);
        }
        for(i = 1; i < BOARD_HEIGHT-1; i++) {
            canvas.putChar('│', 0, i);
            canvas.putChar('│', BOARD_WIDTH-1, i);
        }


        System.out.println(canvas);
    }

    /**
     * Prints the drawable cards (decks and visible cards)
     *
     * @param resourceDeck the resource card deck that needs to be printed
     * @param goldDeck the gold card deck that needs to be printed
     * @param visibleCards the visible cards that need to be printed
     */
    public static void printDrawableCards(Deck<PlayCard> resourceDeck, Deck<PlayCard> goldDeck, PlayCard[] visibleCards) {
        if (visibleCards.length != 4) throw new RuntimeException("unexpected amount of visible cards");

        Canvas canvas = new Canvas(3 * DEFAULT_CARD_WIDTH + 5 + 2, 2 + DEFAULT_CARD_HEIGHT * 2 + 3);

        canvas.putString("Deck n°0 (resource):", 0, 1);
        canvas.putCanvas(getPlayCardDeckCanvas(resourceDeck), 0, 2);

        canvas.putString("Deck n°1 (gold)", 0, 2 + DEFAULT_CARD_HEIGHT + 2);
        canvas.putCanvas(getPlayCardDeckCanvas(goldDeck), 0, 2 + DEFAULT_CARD_HEIGHT + 3);


        for (int i = 0; i < visibleCards.length; i++) {
            canvas.putString("Card n°" + (i+2), DEFAULT_CARD_WIDTH + 5 + (DEFAULT_CARD_WIDTH + 2) * (i % 2) + DEFAULT_CARD_WIDTH / 2 - 4, 1 + (DEFAULT_CARD_HEIGHT + 3) * (i / 2));

            canvas.putCanvas(getCardCanvas(visibleCards[i], false), DEFAULT_CARD_WIDTH + 5 + (DEFAULT_CARD_WIDTH + 2) * (i % 2), 2 + (DEFAULT_CARD_HEIGHT + 3) * (i / 2));
        }

        System.out.println(canvas);
    }

    /**
     * Prints the scoreboard.
     *
     * @param scoreBoard ScoreBoard containing the scores that needs to be placed
     * @param players the list of players whose score needs to be displayed
     */
    public static void printScoreBoard(ScoreBoard scoreBoard, List<Player> players) {
        if (scoreBoard == null || players == null) return;
        ArrayList<String> nicknames = new ArrayList<>();

        int lmax = 0;
        for (Player p : players) {
            nicknames.add(p.nickname);
            if (lmax < p.nickname.length()) lmax = p.nickname.length();
        }


        nicknames.sort(
                Comparator.comparingInt(scoreBoard::getScore)
        );


        // width = 4 (indentation) + 4 (score) + 1 (space) + lmax (longest nickname)
        // no need to check if the "Score:" string fits since 9 is enough anyway
        Canvas canvas = new Canvas(9 + lmax, nicknames.size() + 1);

        canvas.putString("Score:", 0, 0);
        int i = 1;
        for (String nick : nicknames) {
            StringBuilder score = new StringBuilder(Integer.toString(scoreBoard.getScore(nick)));

            while (score.length() < 4) {
                score.insert(0, ' ');
            }

            // space
            score.append(' ');
            score.append(nick);

            canvas.putString(score.toString(), 4, i++);
        }

        System.out.println(canvas);
    }

    /**
     * Prints a chat.
     *
     * @param chat Chat object that contains the messages
     * @param localPlayer the local Player
     * @param remotePlayer the remote player whose chat with the local player needs to be placed, {@code null} if the public chat needs to printed
     */
    public static void printChat(Chat chat, Player localPlayer, Player remotePlayer) {
        List<ChatMessage> messages;
        if(remotePlayer != null) messages= chat.getMessagesChat(localPlayer.nickname, remotePlayer.nickname);
        else messages = chat.getMessagesChat(localPlayer.nickname, null);

        System.out.println("Messages:");
        for (ChatMessage msg : messages) {
            System.out.println("\t" + msg.getSenderNick() + ": " + msg.getText());
        }
    }

    /**
     * Allows to print a textual representation of a game's goal.
     *
     * @param goal Goal that needs to be printed
     */
    public static void printGoal(Goal goal) {
        Canvas canvas = getCardFrame();

        if(goal instanceof ItemGoal itemGoal) {
            List<Corner> items = itemGoal.getItems().toList();

            int i = 0;

            int rows = (items.size() / 2 + items.size() % 2);
            for (Corner corner : items) {
                canvas.setTextColor(cornerToColorMap.get(corner));
                canvas.putStringMatrix(cornerToStringsMap.get(corner), DEFAULT_CARD_WIDTH / 2 - 5 + 6 * (i % 2), DEFAULT_CARD_HEIGHT / 2 - (3 * rows + Math.max(0, rows - 1)) / 2 + 4 * (i / 2));
                canvas.resetColor();
                i++;
            }
        }
        else if(goal instanceof PatternGoal patternGoal) {
            Resource[][] pattern = patternGoal.getPattern();

            Canvas c = new Canvas(pattern[0].length * 3, pattern.length * 3);

            for(int i = 0; i < pattern.length; i++) {
                for(int j = 0; j < pattern[0].length; j++) {
                    if(pattern[i][j] != null) {
                        c.putChar(
                                pattern[i][j].toString().charAt(0),
                                1 + 3*j,
                                j + i*3
                        );
                    }
                }
            }

            canvas.putCanvas(c,
                    DEFAULT_CARD_WIDTH/2 - c.width/2,
                    DEFAULT_CARD_HEIGHT/2 - c.height/2
                    );
        }

        System.out.println(canvas);
    }

    /**
     * Canvas allows to handles dynamic matrices of characters.
     * It simplifies the process of "drawing" using characters.
     * Single characters can also have a color, if colors are enabled.
     */
    public static class Canvas {
        // the Canvas content
        char[][] content;

        // the matching color for each canvas cell
        TextColor[][] contentColor;

        // Canvas size
        public final int width;
        public final int height;

        // the currently selected color
        private TextColor currentColor;

        /**
         * Builds a new Canvas with a specified size
         *
         * @param width the width (in characters) of the canvas
         * @param height teh height (in characters/lines) of the canvas
         */
        private Canvas(int width, int height) {
            currentColor = TextColor.WHITE;

            this.width = width;
            this.height = height;

            this.content = new char[height][width];
            this.contentColor = new TextColor[height][width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    content[i][j] = ' ';
                    contentColor[i][j] = currentColor;
                }
            }

        }

        /**
         * Selects the current colors.
         * Only works if colors are enabled.
         *
         * @param color TextColor that needs to be used for the next added characters
         */
        public void setTextColor(TextColor color) {
            if(!ENABLE_COLORS) return;
            this.currentColor = color;
        }

        /**
         * Resets the currently selected color to WHITE
         */
        public void resetColor() {
            this.currentColor = TextColor.WHITE;
        }

        /**
         * Puts a single char on the canvas at a specified location
         *
         * @param c character that needs to be added
         * @param x the horizontal coordinate of the cell that needs to be updated
         * @param y the vertical (top to bottom) coordinate of the cell that needs to be updated
         */
        public void putChar(char c, int x, int y) {
            if (x < 0 || y < 0 || x >= width || y >= height) {
                return;
            }

            this.content[y][x] = c;
            this.contentColor[y][x] = this.currentColor;
        }

        /**
         * Puts a string on the canvas starting at a specified location
         *
         * @param string string that needs to be added
         * @param x the horizontal coordinate of the cell where the first character of the string need to be put
         * @param y the line where the string needs to be put
         */
        public void putString(String string, int x, int y) {
            if (x < 0 || y < 0 || x >= width || y >= height || string.length() + x > width) {
                throw new ArrayIndexOutOfBoundsException();
            }

            for (Character stringChar : string.toCharArray()) {
                this.content[y][x] = stringChar;
                this.contentColor[y][x] = this.currentColor;
                x++;
            }
        }

        /**
         * Puts a matrix of characters on the canvas starting at a specified location
         *
         * @param strings array of strings that defines the matrix of characters
         * @param x the horizontal coordinate of the cell where the first character of the matrix need to be put
         * @param y the vertical coordinate of the cell where the first character of the matrix need to be put
         */
        public void putStringMatrix(String[] strings, int x, int y) {
            if (strings.length == 0) return;

            int sx = x;
            for (String string : strings) {
                x = sx;
                for (Character stringChar : string.toCharArray()) {
                    this.putChar(stringChar, x,y);
                    x++;
                }
                y++;
            }
        }

        /**
         * Puts a canvas on the current one starting at a specified location
         *
         * @param canvas canvas that needs to be put on the current canvas
         * @param x the horizontal coordinate of the cell where the first character of the canvas need to be put
         * @param y the vertical coordinate of the cell where the first character of the canvas need to be put
         */
        public void putCanvas(Canvas canvas, int x, int y) {
            for (int i = 0; i < canvas.height; i++) {
                for (int j = 0; j < canvas.width; j++) {
                    this.putChar(canvas.content[i][j], x+j, y+i);
                }
            }
        }

        /**
         * Allows to retrieve a printable version of the Canvas.
         *
         * @return the string representing the canvas
         */
        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            TextColor color = TextColor.WHITE;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (contentColor[i][j] != color) {
                        color = contentColor[i][j];
                        stringBuilder.append(color.getCode());
                    }
                    stringBuilder.append(content[i][j]);
                }
                stringBuilder.append("\n");
            }
            stringBuilder.append(TextColor.WHITE.getCode());
            return stringBuilder.toString();
        }
    }

    /**
     * Helper enum that helps to map the wanted text color
     * to the code that needs to be added to the string in order
     * to display the color.
     */
    public enum TextColor {
        CYAN("\u001B[36m"),
        GREEN("\u001B[32m"),
        RED("\u001B[31m"),
        PURPLE("\u001B[35m"),
        YELLOW("\u001B[33m"),
        WHITE("\u001B[0m");

        final String code;

        /**
         * Builds a TextColor constant with the matching code
         * that needs to be printed in order for the color
         * to be displayed.
         *
         * @param colorString the code that needs to be printed to display the color
         */
        TextColor(String colorString) {
            code = colorString;
        }

        /**
         * Retrieves the color code from the TextColor constant
         *
         * @return color code matching the TextColor value
         */
        public String getCode() {
            return code;
        }
    }
}
