package com.task.tiktak;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class PairFindTest {

    private final int n = 10;
    private PairFind pairFind = new PairFind(n);

    @Test
    void simpleIsEndWinTest() {
        for (int i = 0; i < n; i += 2) {
            pairFind.closePair();
        }
        assertTrue(pairFind.isEnd());
    }

    @Test
    void simpleIsEndPlaingTest() {
        for (int i = 0; i < n - 1; i += 2) {
            pairFind.closePair();
        }
        assertFalse(pairFind.isEnd());
    }

    @Test
    void simpleWinPossibilityTest() {
        boolean[][] found = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int i1 = 0; i1 < n; i1++) {
                    for (int j1 = 0; j1 < n; j1++) {
                        if (pairFind.same(i, j, i1, j1)) {
                            found[i][j] = true;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                assertTrue(found[i][j]);
            }
        }
    }

}