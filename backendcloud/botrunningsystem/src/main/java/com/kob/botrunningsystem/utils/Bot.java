package com.kob.botrunningsystem.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

public class Bot implements Supplier<Integer> {
    static class Cell {
        public int x, y;
        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    @Override
    public Integer get() {
        File file = new File("input.txt");
        try {
            Scanner sc = new Scanner(file);
            return nextMove(sc.next());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean check_tail_increasing(int step) {
        if (step <= 10) return true;
        if (step % 3 == 1) return true;
        return false;
    }

    public List<Cell> getCells(int sx, int sy, String steps) {
        steps = steps.substring(1, steps.length() - 1);
        List<Cell> list = new ArrayList<>();
        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;
        list.add(new Cell(sx, sy));
        for (int i = 0; i < steps.length(); i++) {
            int d = steps.charAt(i) - '0';
            x += dx[d];
            y += dy[d];
            list.add(new Cell(x, y));
            step++;
            if (!check_tail_increasing(step)) {
                list.remove(0);
            }
        }

        return list;
    }

    public Integer nextMove(String input) {
        String[] strs = input.split("#");
        int[][] g = new int[13][14];
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 14; j++) {
                if (strs[0].charAt(i * 14 + j) == '1') {
                    g[i][j] = 1;
                }
            }
        }

        int aSx = Integer.parseInt(strs[1]), aSy = Integer.parseInt(strs[2]);
        int bSx = Integer.parseInt(strs[4]), bSy = Integer.parseInt(strs[5]);

        List<Cell> alist = getCells(aSx, aSy, strs[3]);
        List<Cell> blist = getCells(bSx, bSy, strs[6]);

        for (Cell c : alist) g[c.x][c.y] = 1;
        for (Cell c : blist) g[c.x][c.y] = 1;

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        for (int i = 0; i < 4; i++) {
            int x = alist.get(alist.size() - 1).x + dx[i];
            int y = alist.get(alist.size() - 1).y + dy[i];
            if (x >= 0 && x < 13 && y >= 0 && y < 14 && g[x][y] != 1) {
                return i;
            }
        }

        return 0;  //向上走
    }
}
