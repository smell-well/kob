package com.kob.matchingsystem.service.impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;


@Component
public class MatchPool extends Thread{
    private static List<Player> players = new ArrayList<>();
    private ReentrantLock lock = new ReentrantLock();

    private static RestTemplate restTemplate;
    private final static String startGameUrl = "http://127.0.0.1:3000/pk/start/game/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        MatchPool.restTemplate = restTemplate;
    }

    public void addPlayer(Integer userId, Integer rating, Integer bot_id) {
        lock.lock();
        try {
            players.add(new Player(userId, rating, 0, bot_id));
        } finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer userId) {
        lock.lock();
        try {
            List<Player> newplayers = new ArrayList<>();
            for (Player player : players) {
                if (!player.getUserId().equals(userId)) {
                    newplayers.add(player);
                }
            }
            players = newplayers;
        } finally {
            lock.unlock();
        }
    }

    private void increasingWaitingTime() {
        for (Player player : players) {
            player.setWaitingTime(player.getWaitingTime() + 1);
        }
    }

    private void matchPlayers() {
        System.out.println("match players: " + players.toString());
        boolean[] used = new boolean[players.size()];
        for (int i = 0; i < players.size(); i++) {
            if (used[i]) continue;
            for (int j = i + 1; j < players.size(); j++) {
                if (used[j]) continue;
                Player a = players.get(i), b = players.get(j);
                if (checkMatched(a, b)) {
                    used[i] = true;
                    used[j] = true;
                    sendResult(a, b);
                    break;
                }
            }
        }
        List<Player> newplayers = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (!used[i]) {
                newplayers.add(players.get(i));
            }
        }
        players = newplayers;
    }

    private boolean checkMatched(Player a, Player b) {
        int ratingDelta = Math.abs(a.getRating() - b.getRating());
        int minWaitingTime = Math.min(a.getWaitingTime(), b.getWaitingTime());
        return ratingDelta <= minWaitingTime * 10;
    }

    private void sendResult(Player a, Player b) {
        System.out.println("send result " + a.getUserId() + " " + b.getUserId());
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("aId", a.getUserId().toString());
        data.add("aBotId", a.getBotId().toString());
        data.add("bId", b.getUserId().toString());
        data.add("bBotId", b.getBotId().toString());
        restTemplate.postForObject(startGameUrl, data, String.class);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    increasingWaitingTime();
                    matchPlayers();
                } finally {
                     lock.unlock();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
