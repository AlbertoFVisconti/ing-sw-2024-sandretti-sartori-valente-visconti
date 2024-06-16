package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ScoreBoardTest {
    ScoreBoard s1, s2, s3, s4, s5;

    @BeforeEach
    void setup() {
        s1 = new ScoreBoard();

        s2 = new ScoreBoard(
                new ArrayList<>(Arrays.stream(new Player[]{
                        new Player("p1-s2", null),
                        new Player("p2-s2", null),
                        new Player("p3-s2", null),
                        new Player("p4-s2", null)
                }).toList())
        );

        s3 = new ScoreBoard(
                new ArrayList<>(Arrays.stream(new Player[]{
                        new Player("p1-s3", null),
                        new Player("p2-s3", null),
                        new Player("p3-s3", null)
                }).toList())
        );

        s4 = new ScoreBoard(
                new ArrayList<>(Arrays.stream(new Player[]{
                        new Player("p1-s4", null),
                        new Player("p2-s4", null)
                }).toList())
        );

        s5 = new ScoreBoard(
                new ArrayList<>(Arrays.stream(new Player[]{
                        new Player("p1-s5", null)
                }).toList())
        );

        s2.setScore("p1-s2", 102);
        s2.setScore("p2-s2", 202);
        s2.setScore("p3-s2", 302);
        s2.setScore("p4-s2", 402);

        s3.setScore("p1-s3", 103);
        s3.setScore("p2-s3", 203);
        s3.setScore("p3-s3", 303);

        s4.setScore("p1-s4", 0);
        s4.setScore("p2-s4", 43);

        s5.setScore("p1-s5", 1);
    }

    @Test
    void getScore() {
        assertThrows(NullPointerException.class, () -> s1.getScore("p0-s1"));

        assertThrows(NoSuchElementException.class, () -> s2.getScore("p0-s2"));
        assertEquals(102 ,s2.getScore("p1-s2"));
        assertEquals(202 ,s2.getScore("p2-s2"));
        assertEquals(302 ,s2.getScore("p3-s2"));
        assertEquals(402 ,s2.getScore("p4-s2"));

        assertThrows(NoSuchElementException.class, () -> s3.getScore("p0-s3"));
        assertEquals(103 ,s3.getScore("p1-s3"));
        assertEquals(203 ,s3.getScore("p2-s3"));
        assertEquals(303 ,s3.getScore("p3-s3"));

        assertThrows(NoSuchElementException.class, () -> s4.getScore("p0-s4"));
        assertEquals(0 ,s4.getScore("p1-s4"));
        assertEquals(43,s4.getScore("p2-s4"));

        assertThrows(NoSuchElementException.class, () -> s5.getScore("p0-s5"));
        assertEquals(1 ,s5.getScore("p1-s5"));
    }

    @Test
    void setScore() {
        assertThrows(NullPointerException.class, () -> s1.setScore("p0-s1",0 ));

        assertThrows(NoSuchElementException.class, () -> s2.setScore("p0-s2",0));
        s2.setScore("p1-s2", 1);
        s2.setScore("p2-s2", 2);
        s2.setScore("p3-s2", 3);
        s2.setScore("p4-s2", 4);

        assertThrows(NoSuchElementException.class, () -> s3.setScore("p0-s2", 0));
        s3.setScore("p1-s3", 5);
        s3.setScore("p2-s3", 6);
        s3.setScore("p3-s3", 7);

        assertThrows(NoSuchElementException.class, () -> s4.setScore("p0-s2",0));
        s4.setScore("p1-s4", 8);
        s4.setScore("p2-s4", 9);

        assertThrows(NoSuchElementException.class, () -> s5.setScore("p0-s2", 0));
        s5.setScore("p1-s5", 10);

        assertEquals(1 ,s2.getScore("p1-s2"));
        assertEquals(2 ,s2.getScore("p2-s2"));
        assertEquals(3 ,s2.getScore("p3-s2"));
        assertEquals(4 ,s2.getScore("p4-s2"));

        assertEquals(5 ,s3.getScore("p1-s3"));
        assertEquals(6 ,s3.getScore("p2-s3"));
        assertEquals(7 ,s3.getScore("p3-s3"));

        assertEquals(8 ,s4.getScore("p1-s4"));
        assertEquals(9,s4.getScore("p2-s4"));

        assertEquals(10 ,s5.getScore("p1-s5"));
    }

    @Test
    void addScore() {
        assertThrows(NullPointerException.class, () -> s1.addScore("p0-s1",0 ));

        assertThrows(NoSuchElementException.class, () -> s2.addScore("p0-s2",0));
        s2.addScore("p1-s2", 1);
        s2.addScore("p2-s2", 2);
        s2.addScore("p3-s2", 3);
        s2.addScore("p4-s2", 4);

        assertThrows(NoSuchElementException.class, () -> s3.addScore("p0-s2", 0));
        s3.addScore("p1-s3", 5);
        s3.addScore("p2-s3", 6);
        s3.addScore("p3-s3", 7);

        assertThrows(NoSuchElementException.class, () -> s4.addScore("p0-s2",0));
        s4.addScore("p1-s4", 8);
        s4.addScore("p2-s4", 9);

        assertThrows(NoSuchElementException.class, () -> s5.addScore("p0-s2", 0));
        s5.addScore("p1-s5", 10);

        assertEquals(103 ,s2.getScore("p1-s2"));
        assertEquals(204 ,s2.getScore("p2-s2"));
        assertEquals(305 ,s2.getScore("p3-s2"));
        assertEquals(406 ,s2.getScore("p4-s2"));

        assertEquals(108 ,s3.getScore("p1-s3"));
        assertEquals(209 ,s3.getScore("p2-s3"));
        assertEquals(310 ,s3.getScore("p3-s3"));

        assertEquals(8 ,s4.getScore("p1-s4"));
        assertEquals(52,s4.getScore("p2-s4"));

        assertEquals(11 ,s5.getScore("p1-s5"));
    }

    @Test
    void copyScore() {
        ScoreBoard n1 = new ScoreBoard(),
                n2 = new ScoreBoard(),
                n3 = new ScoreBoard(),
                n4 = new ScoreBoard(),
                n5 = new ScoreBoard();

        assertThrows(NullPointerException.class, () -> n1.copyScore(s1));

        n2.copyScore(s2);
        n3.copyScore(s3);
        n4.copyScore(s4);
        n5.copyScore(s5);

        assertThrows(NoSuchElementException.class, () -> n2.getScore("p0-s2"));
        assertEquals(102 ,n2.getScore("p1-s2"));
        assertEquals(202 ,n2.getScore("p2-s2"));
        assertEquals(302 ,n2.getScore("p3-s2"));
        assertEquals(402 ,n2.getScore("p4-s2"));

        assertThrows(NoSuchElementException.class, () -> n3.getScore("p0-s3"));
        assertEquals(103 ,n3.getScore("p1-s3"));
        assertEquals(203 ,n3.getScore("p2-s3"));
        assertEquals(303 ,n3.getScore("p3-s3"));

        assertThrows(NoSuchElementException.class, () -> n4.getScore("p0-s4"));
        assertEquals(0 ,n4.getScore("p1-s4"));
        assertEquals(43,n4.getScore("p2-s4"));

        assertThrows(NoSuchElementException.class, () -> n5.getScore("p0-s5"));
        assertEquals(1 ,n5.getScore("p1-s5"));

    }

    @Test
    void testToString() {
        assertTrue(s1.toString().isEmpty());

        String s2String = s2.toString();
        String s3String = s3.toString();
        String s4String = s4.toString();
        String s5String = s5.toString();

        assertTrue(s2String.contains("p1-s2: 102\n"));
        assertTrue(s2String.contains("p2-s2: 202\n"));
        assertTrue(s2String.contains("p3-s2: 302\n"));
        assertTrue(s2String.contains("p4-s2: 402\n"));

        assertTrue(s3String.contains("p1-s3: 103\n"));
        assertTrue(s3String.contains("p2-s3: 203\n"));
        assertTrue(s3String.contains("p3-s3: 303\n"));

        assertTrue(s4String.contains("p1-s4: 0\n"));
        assertTrue(s4String.contains("p2-s4: 43\n"));

        assertTrue(s5String.contains("p1-s5: 1\n"));
    }
}