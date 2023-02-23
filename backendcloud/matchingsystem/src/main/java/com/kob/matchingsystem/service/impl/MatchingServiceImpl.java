package com.kob.matchingsystem.service.impl;

import com.kob.matchingsystem.service.MatchingService;
import com.kob.matchingsystem.service.impl.utils.MatchPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {
    public final static MatchPool matchpool = new MatchPool();

    @Override
    public String addPlayer(Integer user_id, Integer rating, Integer bot_id) {
        System.out.println("add player " + user_id + " " + rating);
        matchpool.addPlayer(user_id, rating, bot_id);
        return "add player success";
    }

    @Override
    public String removePlayer(Integer user_id) {
        System.out.println("remove player " + user_id);
        matchpool.removePlayer(user_id);
        return "remove player success";
    }
}
