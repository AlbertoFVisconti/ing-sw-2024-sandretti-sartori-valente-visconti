package it.polimi.ingsw.model.player;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerColorTest {
    @Test
    void playerColorToColor() {
        assertEquals(Color.color(1,0,0),PlayerColor.playerColorToColor(PlayerColor.RED));
        assertEquals(Color.color(0,1,0),PlayerColor.playerColorToColor(PlayerColor.GREEN));
        assertEquals(Color.color(0,0,1),PlayerColor.playerColorToColor(PlayerColor.BLUE));
        assertEquals(Color.color(1,1,0),PlayerColor.playerColorToColor(PlayerColor.YELLOW));
    }

    @Test
    void playerColorToImagePath() {
        assertEquals("/image/RedCircle.png",PlayerColor.playerColorToImagePath(PlayerColor.RED));
        assertEquals("/image/GreenCircle.png",PlayerColor.playerColorToImagePath(PlayerColor.GREEN));
        assertEquals("/image/BlueCircle.png",PlayerColor.playerColorToImagePath(PlayerColor.BLUE));
        assertEquals("/image/YellowCircle.png",PlayerColor.playerColorToImagePath(PlayerColor.YELLOW));
    }
}