package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private Integer id;
    private Integer botId;
    private String botCode;  // -1 表示人工
    private Integer sx;  // 起始坐标
    private Integer sy;
    private List<Integer> steps;  //移动路径

    private boolean check_tail_increasing(int step) {
        if (step <= 10) return true;
        if (step % 3 == 1) return true;
        return false;
    }

    public List<Cell> getCells() {
        List<Cell> list = new ArrayList<>();

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;
        list.add(new Cell(sx, sy));
        for (int d : steps) {
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

    public String getStepsToString() {
        StringBuffer sb = new StringBuffer();
        for (int d : steps) {
            sb.append(d);
        }

        return sb.toString();
    }
}
