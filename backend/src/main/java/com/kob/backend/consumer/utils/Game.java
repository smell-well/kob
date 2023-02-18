package com.kob.backend.consumer.utils;

import java.util.Random;

public class Game {
    private final Integer rows;
    private final Integer cols;
    private final Integer inner_walls_count;
    int[][] g;
    final private static int[] dx = {0, -1, 0, 1}, dy = {1, 0, -1, 0};

    public Game(int rows, int cols, int inner_walls_count) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        g = new int[rows][cols];
    }

    public int[][] getG() {
        return this.g;
    }

    private boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = 1;
        for (int i = 0; i < 4; i++) {
            int nx = sx + dx[i], ny = sy + dy[i];
            if (nx >= 0 && nx < rows && ny >= 0 && ny < rows && g[nx][ny] == 0) {
                if (check_connectivity(nx, ny, tx, ty)) {
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }
        g[sx][sy] = 0;
        return false;
    }

    private boolean draw() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                g[i][j] = 0;
            }
        }

        for (int i = 0; i < this.rows; i++) {
            g[i][0] = g[i][this.cols - 1] = 1;
        }

        for (int j = 0; j < this.cols; j++) {
            g[0][j] = g[this.rows - 1][j] = 1;
        }

        Random random = new Random();
        for (int i = 0; i < this.inner_walls_count / 2; i++) {
            for (int j = 0; j < 1000; j++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);
                if (g[r][c] != 0 || g[this.rows - 1 - r][this.cols - 1 - c] != 0) continue;
                if ((r == 1 && c == this.cols - 2) || (r == this.rows - 2 && c == 1)) {
                    continue;
                }
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createMap() {
        for (int i = 0; i < 1000; i++) {
            if (draw()) {
                break;
            }
        }
    }

}
