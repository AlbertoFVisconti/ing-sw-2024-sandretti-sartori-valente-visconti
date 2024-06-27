package it.polimi.ingsw.model.player;

import javafx.scene.paint.Color;

/**
 * Enum representing the possible colors for players.
 */
public enum PlayerColor {
    RED, YELLOW, GREEN, BLUE;

    /**
     * Retrieves a Color (javafx) representation of the PlayerColor constant
     *
     * @param color PlayerColor that needs to be converted
     * @return the Color that matches the provided PlayerColor
     */
    public static Color playerColorToColor(PlayerColor color) {
        return switch (color) {
            case RED -> Color.color(1, 0, 0);
            case YELLOW -> Color.color(1, 1, 0);
            case GREEN -> Color.color(0, 1, 0);
            case BLUE -> Color.color(0, 0, 1);
        };
    }

    /**
     * Retrieves the player's pawn whose color matches the provided PlayerColor constant
     *
     * @param color PlayerColor whose pawn is needed
     * @return the player's pawn whose color matches the provided PlayerColor
     */
    public static String playerColorToImagePath(PlayerColor color) {
        return switch (color) {
            case RED -> "/image/RedCircle.png";
            case YELLOW -> "/image/YellowCircle.png";
            case GREEN -> "/image/GreenCircle.png";
            case BLUE -> "/image/BlueCircle.png";
        };
    }

}
