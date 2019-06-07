package com.hw.cannon;

import javafx.scene.paint.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void simpleIsCloseTest() {
        var firstBomb = new Bomb(0, 0, 10, Color.RED, 1, 1, null);
        var secondBomb = new Bomb(1, 1, 20, Color.BLACK, 1, 1, null);
        assertTrue(firstBomb.isClose(secondBomb));
        assertTrue(secondBomb.isClose(firstBomb));
    }

    @Test
    void isCloseInOneTest() {
        var firstBomb = new Bomb(0, 0, 1, Color.RED, 1, 1, null);
        var secondBomb = new Bomb(1.1, 1.1, 20, Color.BLACK, 1, 1, null);
        assertFalse(firstBomb.isClose(secondBomb));
        assertFalse(secondBomb.isClose(firstBomb));
    }

    @Test
    void isCloseOutOfRadiusTest() {
        var firstBomb = new Bomb(0, 0, 10, Color.RED, 1, 1, null);
        var secondBomb = new Bomb(100, 100, 20, Color.BLACK, 1, 1, null);
        assertFalse(firstBomb.isClose(secondBomb));
        assertFalse(secondBomb.isClose(firstBomb));
    }

    @Test
    void simpleCorYTest() {
        var xs = new Double[] {0.0, 1.0, 2.0};
        var ys = new Double[] {0.0, 1.0, 0.0};
        var mount = new Mount(xs, ys);
        for (int i = 0; i < 3; i++) {
            assertEquals(mount.getYCoordinate(xs[i]), ys[i]);
        }
    }

    @Test
    void complexCorYTest() {
        var xs = new Double[] {0.0, 2.0};
        var ys = new Double[] {0.0, 0.0};
        var mount = new Mount(xs, ys);
        assertEquals(Double.valueOf(0.0), mount.getYCoordinate(1.0));
    }

    @Test
    void simpleIsUnderTest() {
        var xs = new Double[] {0.0, 1.0, 2.0};
        var ys = new Double[] {0.0, 1.0, 0.0};
        var mount = new Mount(xs, ys);
        for (int i = 0; i < 3; i++) {
            assertFalse(mount.isUnder(xs[i], ys[i]));
        }
    }

    @Test
    void underIsUnderTest() {
        var xs = new Double[] {0.0, 1.0, 2.0};
        var ys = new Double[] {0.0, 1.0, 0.0};
        var mount = new Mount(xs, ys);
        assertTrue(mount.isUnder(1.5, 0.0));
    }

    @Test
    void aboveIsUnderTest() {
        var xs = new Double[] {0.0, 1.0, 2.0};
        var ys = new Double[] {0.0, 1.0, 0.0};
        var mount = new Mount(xs, ys);
        assertFalse(mount.isUnder(0.5, 1.0));
    }
}