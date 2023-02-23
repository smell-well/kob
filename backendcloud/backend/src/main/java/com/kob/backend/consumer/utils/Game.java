package com.kob.backend.consumer.utils;


import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread{
    private final Integer rows;
    private final Integer cols;
    private final Integer inner_walls_count;
    int[][] g;
    private final static int[] dx = {0, -1, 0, 1}, dy = {1, 0, -1, 0};
    private final Player playerA, playerB;
    private Integer nextStepA = null;
    private Integer nextStepB = null;
    private ReentrantLock lock = new ReentrantLock();
    private String status = "playing";  //playing -> finished
    private String loser = "";  // all 表示平局
    private final static String SendBotCodeUrl = "http://127.0.0.1:3002/bot/add/";

    public Game(
            int rows,
            int cols,
            int inner_walls_count,
            int idA,
            Bot botA,
            int idB,
            Bot botB
    ) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        g = new int[rows][cols];
        Integer botIdA = -1, botIdB = -1;
        String botCodeA = "", botCodeB = "";
        if (botA != null) {
            botIdA = botA.getId();
            botCodeA = botA.getContent();
        }
        if (botB != null) {
            botIdB = botB.getId();
            botCodeB = botB.getContent();
        }

        playerA = new Player(idA, botIdA, botCodeA, rows - 2, 1, new ArrayList<>());
        playerB = new Player(idB, botIdB, botCodeB, 1, cols - 2, new ArrayList<>());
    }

    public Player getPlayerA() {
        return this.playerA;
    }

    public Player getPlayerB() {
        return this.playerB;
    }

    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
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

    public String getInput(Player player) {
        Player me, you;
        if (playerA.getId().equals(player.getId())) {
            me = playerA;
            you = playerB;
        } else {
            me = playerB;
            you = playerA;
        }

        return getMapToString() + "#"
                + me.getSx() + "#"
                + me.getSy() + "#"
                + "(" + me.getStepsToString() + ")" + "#"
                + you.getSx() + "#"
                + you.getSy() + "#"
                + "(" + you.getStepsToString() + ")";
    }

    private void sendBotCode(Player player) {
        if (player.getBotId() == -1) return;  // 人工

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", player.getId().toString());
        data.add("bot_code", player.getBotCode());
        data.add("input", getInput(player));

        WebSocketServer.restTemplate.postForObject(SendBotCodeUrl, data, String.class);
    }

    private boolean nextSteps() {
        try {  // 防止多次操作
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendBotCode(playerA);
        sendBotCode(playerB);

        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);  // 总共5s的判断时间
                lock.lock();
                try {
                    if (nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //向两个client传递信息
    private void sendMove() {
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            nextStepA = nextStepB = null;
            sendAllMessage(resp.toJSONString());
        } finally {
            lock.unlock();
        }
    }

    private boolean check_vaild(List<Cell> A, List<Cell> B) {
        int n = A.size();
        Cell cell = A.get(n - 1);  //取出蛇头
        if (g[cell.getX()][cell.getY()] == 1) return false;

        for (int i = 0; i < n - 1; i++) {
            if (cell.getX() == A.get(i).getX() && cell.getY() == A.get(i).getY()) {
                return false;
            }
            if (cell.getX() == B.get(i).getX() && cell.getY() == B.get(i).getY()) {
                return false;
            }
        }
        return true;
    }

    // 判断两名玩家下一步操作的合法性
    private void judge() {
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean validA = check_vaild(cellsA, cellsB);
        boolean validB = check_vaild(cellsB, cellsA);

        if (!validA || !validB) {
            status = "finished";

            if (!validA && !validB) {
                loser = "all";
            } else if (!validA) {
                loser = "A";
            } else if (!validB) {
                loser = "B";
            }
        }
    }

    private void sendAllMessage(String message) {
        if (WebSocketServer.users.get(playerA.getId()) != null)
            WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        if (WebSocketServer.users.get(playerB.getId()) != null)
            WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    private void updateUserRating(Player player, Integer rating) {
        User user = WebSocketServer.userMapper.selectById(player.getId());
        user.setRating(rating);
        WebSocketServer.userMapper.updateById(user);
    }

    private void saveToDatabase() {
        Integer ratingA = WebSocketServer.userMapper.selectById(playerA.getId()).getRating();
        Integer ratingB = WebSocketServer.userMapper.selectById(playerB.getId()).getRating();

        if ("A".equals(loser)) {
            ratingA -= 20;
            ratingB += 50;
        } else if ("B".equals(loser)) {
            ratingA += 50;
            ratingB -= 20;
        }

        updateUserRating(playerA, ratingA);
        updateUserRating(playerB, ratingB);

        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsToString(),
                playerB.getStepsToString(),
                getMapToString(),
                loser,
                new Date()
        );

        WebSocketServer.recordMapper.insert(record);
    }

    private String getMapToString() {
        StringBuffer sb = new StringBuffer();
        for (int[] row : g) {
            for (int d : row) {
//                System.out.print(d + " ");
                sb.append(d);
            }
//            System.out.println();
        }
        return sb.toString();
    }

    // 向两个client发送结果
    private void sendResult() {
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        saveToDatabase();
        sendAllMessage(resp.toJSONString());
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            if (nextSteps()) {  // 是否还有下一步
                judge();
                if ("playing".equals(status)) {
                    sendMove();
                } else if ("finished".equals(status)){
                    sendResult();
                    break;
                }
            } else {
                lock.lock();
                try {
                    status = "finished";
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";
                    } else if (nextStepA == null) {
                        loser = "A";
                    } else {
                        loser = "B";
                    }
                } finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
