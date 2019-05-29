package com.task.tiktak;

import org.jetbrains.annotations.*;

import java.util.*;

public class PairFind {
    private final @NotNull int[][] cards;
    private int closed;
    private final int all;

    public PairFind(int n) {
        cards = new int[n][n];
        all = n * n;
        List<Integer> numbers = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < n * n / 2; i++) {
            int cur = random.nextInt(n * n / 2);
            numbers.add(cur);
            numbers.add(cur);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int index = random.nextInt(numbers.size());
                cards[i][j] = numbers.get(index);
                numbers.remove(index);
            }
        }
    }

    public int getNum(int i, int j) {
        return cards[i][j];
    }

    public boolean same(int i1, int j1, int i2, int j2) {
        if (i1 == i2 && j1 == j2) {
            return false;
        }
        return cards[i1][j1] == cards[i2][j2];
    }

    public void closePair() {
        closed += 2;
    }

    public boolean isEnd() {
        return closed == all;
    }
}
