package it.polimi.ingsw.view.ui.tui;

import it.polimi.ingsw.model.ScoreBoard;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.CardSlot;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.chat.Chat;
import it.polimi.ingsw.model.chat.ChatMessage;
import it.polimi.ingsw.model.decks.Deck;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.CardLocation;

import java.util.*;

public final class Printer {
    private final static int DEFAULT_CARD_WIDTH = 27;
    private final static int DEFAULT_CARD_HEIGHT = 11;

    private final static HashMap<Corner, String[]> cornerToStringsMap;
    private final static HashMap<Corner, TextColor> cornerToColorMap;

    static {
        cornerToStringsMap = new HashMap<>();
        cornerToColorMap = new HashMap<>();

        cornerToStringsMap.put(
                Corner.ANIMAL,
                new String[]{
                        "/|_|\\",
                        "\\   /",
                        " \\_/ "
                }
        );
        cornerToStringsMap.put(
                Corner.FUNGUS,
                new String[]{
                        " ___ ",
                        "/   \\",
                        " |_| "
                }
        );
        cornerToStringsMap.put(
                Corner.PLANT,
                new String[]{
                        "  ^  ",
                        "_/|\\_",
                        "\\   /"
                }
        );
        cornerToStringsMap.put(
                Corner.INSECT,
                new String[]{
                        "|\\\"/|",
                        "| _ |",
                        "|/ \\|"
                }
        );
        cornerToStringsMap.put(
                Corner.INK,
                new String[]{
                        " ___ ",
                        " | | ",
                        "[___]"
                }
        );
        cornerToStringsMap.put(
                Corner.SCROLL,
                new String[]{
                        " ___ ",
                        "|   |",
                        "|___|"
                }
        );
        cornerToStringsMap.put(
                Corner.FEATHER,
                new String[]{
                        "   /\\",
                        "  / /",
                        " /-°"
                }
        );
        cornerToStringsMap.put(
                Corner.EMPTY,
                new String[]{
                        "     ",
                        "     ",
                        "     "
                }
        );
        cornerToStringsMap.put(
                null,
                new String[]{
                        "     ",
                        "     ",
                        "     "
                }
        );

        cornerToColorMap.put(
                Corner.ANIMAL,
                TextColor.CYAN
        );
        cornerToColorMap.put(
                Corner.PLANT,
                TextColor.GREEN
        );
        cornerToColorMap.put(
                Corner.FUNGUS,
                TextColor.RED
        );
        cornerToColorMap.put(
                Corner.INSECT,
                TextColor.PURPLE
        );
        cornerToColorMap.put(
                Corner.INK,
                TextColor.YELLOW
        );
        cornerToColorMap.put(
                Corner.FEATHER,
                TextColor.YELLOW
        );
        cornerToColorMap.put(
                Corner.SCROLL,
                TextColor.YELLOW
        );
        cornerToColorMap.put(
                Corner.EMPTY,
                TextColor.WHITE
        );
        cornerToColorMap.put(
                null,
                TextColor.WHITE
        );

    }

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

    private static Canvas getPlayCardDeckCanvas(Deck<PlayCard> deck) {
        if (deck == null) return new Canvas(0, 0);
        Canvas canvas = getCardFrame();

        canvas.setTextColor(cornerToColorMap.get(deck.getTopOfTheStack().getCorner()));
        canvas.putStringMatrix(cornerToStringsMap.get(deck.getTopOfTheStack().getCorner()), DEFAULT_CARD_WIDTH / 2 - 2, DEFAULT_CARD_HEIGHT / 2 - 1);
        canvas.resetColor();

        return canvas;
    }

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
        }


        return canvas;
    }

    public static void printCard(Card card, boolean onBackSide) {
        System.out.println(getCardCanvas(card, onBackSide));
    }

    public static void printHand(PlayCard[] cards) {
        final int SPACING_BETWEEN_CARDS = 2;
        Canvas canvas = new Canvas(DEFAULT_CARD_WIDTH * cards.length + SPACING_BETWEEN_CARDS * (cards.length - 1) + 2, 2 + DEFAULT_CARD_HEIGHT * 2 + 3);

        canvas.putString("Front:", 0, 0);
        canvas.putString("Back:", 0, 2 + DEFAULT_CARD_HEIGHT + 1);
        for (int i = 0; i < cards.length; i++) {
            canvas.putString("Card n°" + i, (DEFAULT_CARD_WIDTH + SPACING_BETWEEN_CARDS) * i + DEFAULT_CARD_WIDTH / 2 - 4, 0);

            canvas.putCanvas(getCardCanvas(cards[i], false), (DEFAULT_CARD_WIDTH + SPACING_BETWEEN_CARDS) * i, 2);

            canvas.putCanvas(getCardCanvas(cards[i], true), (DEFAULT_CARD_WIDTH + SPACING_BETWEEN_CARDS) * i, 2 + DEFAULT_CARD_HEIGHT + 3);
        }

        System.out.println(canvas);
    }

    public static void printBoard(Map<CardLocation, CardSlot> board) {
        HashMap<Integer, CardLocation> t = new HashMap<>();

        int left = 0;
        int right = 0;
        int top = 0;
        int bottom = 0;

        for (CardLocation cardLocation : board.keySet()) {
            t.put(board.get(cardLocation).placementTurn(), cardLocation);

            if (cardLocation.x() < left) left = cardLocation.x();
            if (cardLocation.x() > right) right = cardLocation.x();
            if (cardLocation.y() < bottom) bottom = cardLocation.y();
            if (cardLocation.y() > top) top = cardLocation.y();
        }

        int dx = right - left;
        int dy = top - bottom;

        Canvas canvas = new Canvas(DEFAULT_CARD_WIDTH + dx * (DEFAULT_CARD_WIDTH - 7), DEFAULT_CARD_HEIGHT + dy * (DEFAULT_CARD_HEIGHT - 5));

        int i = 0;

        while (t.get(i) != null) {
            int x, y;
            x = t.get(i).x() - left;
            y = top - t.get(i).y();

            canvas.putCanvas(
                    getCardCanvas(board.get(t.get(i)).card(), board.get(t.get(i)).onBackSide()),
                    x * (DEFAULT_CARD_WIDTH - 7),
                    y * (DEFAULT_CARD_HEIGHT - 5)
            );

            i++;
        }

        System.out.println(canvas);
    }

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

    public static void printChat(Chat chat, Player localPlayer, Player remotePlayer) {
        List<ChatMessage> messages;
        if(remotePlayer != null) messages= chat.getMessagesChat(localPlayer.nickname, remotePlayer.nickname);
        else messages = chat.getMessagesChat(localPlayer.nickname, null);

        System.out.println("Messages:");
        for (ChatMessage msg : messages) {
            System.out.println("\t" + msg.getSenderNick() + ": " + msg.getText());
        }
    }

    public static class Canvas {
        char[][] content;
        TextColor[][] contentColor;
        private final int width;
        private final int height;

        private TextColor currentColor;

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

        public void setTextColor(TextColor color) {
            this.currentColor = color;
        }

        public void resetColor() {
            this.currentColor = TextColor.WHITE;
        }

        public void putChar(char c, int x, int y) {
            if (x < 0 || y < 0 || x >= width || y >= height) {
                throw new ArrayIndexOutOfBoundsException();
            }

            this.content[y][x] = c;
            this.contentColor[y][x] = this.currentColor;
        }

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

        public void putStringMatrix(String[] strings, int x, int y) {
            if (strings.length == 0) return;
            if (x < 0 || y < 0 || x >= width || y >= height || strings[0].length() + x > width || strings.length + y > height) {
                throw new ArrayIndexOutOfBoundsException();
            }

            int sx = x;
            for (String string : strings) {
                x = sx;
                for (Character stringChar : string.toCharArray()) {
                    this.content[y][x] = stringChar;
                    this.contentColor[y][x] = this.currentColor;
                    x++;
                }
                y++;
            }
        }

        public void putCanvas(Canvas canvas, int x, int y) {
            if (x < 0 || y < 0 || x >= width || y >= height || canvas.getWidth() + x > width || canvas.getHeight() + y > height) {
                throw new ArrayIndexOutOfBoundsException();
            }

            for (int i = 0; i < canvas.height; i++) {
                for (int j = 0; j < canvas.width; j++) {
                    content[y + i][x + j] = canvas.content[i][j];
                    this.contentColor[y + i][x + j] = canvas.contentColor[i][j];
                }
            }
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

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

    public enum TextColor {
        CYAN("\u001B[36m"),
        GREEN("\u001B[32m"),
        RED("\u001B[31m"),
        PURPLE("\u001B[35m"),
        YELLOW("\u001B[33m"),
        WHITE("\u001B[0m");

        final String code;

        TextColor(String colorString) {
            code = colorString;
        }

        public String getCode() {
            return code;
        }
    }
}
