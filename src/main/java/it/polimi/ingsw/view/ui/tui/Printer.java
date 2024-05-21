package it.polimi.ingsw.view.ui.tui;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.corners.Corner;

import java.util.HashMap;

final class Printer {
    private final static HashMap<Corner, String[]> cornerToStringsMap;
    private final static HashMap<Corner, String> cornerToColorMap;
    public static final String COLOR_RESET = "\u001B[0m";

    static {
        cornerToStringsMap = new HashMap<>();
        cornerToColorMap = new HashMap<>();

        cornerToStringsMap.put(
                Corner.ANIMAL,
                new String[] {
                        "/|_|\\",
                        "\\   /",
                        " \\_/ "
                }
        );
        cornerToStringsMap.put(
                Corner.FUNGUS,
                new String[] {
                        " ___ ",
                        "/   \\",
                        " |_| "
                }
        );
        cornerToStringsMap.put(
                Corner.PLANT,
                new String[] {
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
                "\u001B[36m"
        );
        cornerToColorMap.put(
                Corner.PLANT,
                "\u001B[32m"
        );
        cornerToColorMap.put(
                Corner.FUNGUS,
                "\u001B[31m"
        );
        cornerToColorMap.put(
                Corner.INSECT,
                "\u001B[35m"
        );
        cornerToColorMap.put(
                Corner.INK,
                "\u001B[33m"
        );
        cornerToColorMap.put(
                Corner.FEATHER,
                "\u001B[33m"
        );
        cornerToColorMap.put(
                Corner.SCROLL,
                "\u001B[33m"
        );
        cornerToColorMap.put(
                Corner.EMPTY,
                "\u001B[33m"
        );
        cornerToColorMap.put(
                null,
                "\u001B[33m"
        );

    }

    public static void printCard(Card card, boolean onBackSide) {
        if(onBackSide ^ card.isOnBackSide()) card.flip();

        System.out.println(" ________________________ ");

        Corner cl, cr;
        cl = card.getTopLeftCorner();
        cr = card.getTopRightCorner();

        String line;


        for(int i = 0; i < 3; i++) {
            line = "|";
            line += cornerToColorMap.get(cl);
            line += cornerToStringsMap.get(cl)[i];
            line += COLOR_RESET;
            if(cl!=null) line += "|";
            else line += " ";
            line += "            ";

            if(cr!=null) line += "|";
            else line += " ";
            line += cornerToColorMap.get(cr);
            line += cornerToStringsMap.get(cr)[i];
            line += COLOR_RESET;
            line += "|";

            System.out.println(line);
        }

        if(cl != null) {
            line = "|_____|            ";
        }
        else {
            line = "|                  ";
        }

        if(cr != null) {
            line += "|_____|";
        }
        else {
            line += "      |";
        }
        System.out.println(line);

        cl = card.getBottomLeftCorner();
        cr = card.getBottomRightCorner();

        for(int i = 0; i < 3; i++) {
            line = "|";
            line += cornerToColorMap.get(cl);
            line += cornerToStringsMap.get(cl)[i];
            line += COLOR_RESET;
            if(cl!=null) line += "|";
            else line += " ";
            line += "            ";

            if(cr!=null) line += "|";
            else line += " ";
            line += cornerToColorMap.get(cr);
            line += cornerToStringsMap.get(cr)[i];
            line += COLOR_RESET;
            line += "|";

            System.out.println(line);
        }

        if(cl != null) {
            line = "|_____|____________";
        }
        else {
            line = "|__________________";
        }

        if(cr != null) {
            line += "|_____|";
        }
        else {
            line += "______|";
        }
        System.out.println(line);
    }
}
