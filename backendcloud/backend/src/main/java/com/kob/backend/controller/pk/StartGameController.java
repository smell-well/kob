package com.kob.backend.controller.pk;

import com.kob.backend.service.pk.StartGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class StartGameController {
    @Autowired
    private StartGameService startGameService;

    @PostMapping("/pk/start/game/")
    public String startgame(@RequestParam MultiValueMap<String, String> data) {
        Integer a = Integer.parseInt(Objects.requireNonNull(data.getFirst("aId")));
        Integer b = Integer.parseInt(Objects.requireNonNull(data.getFirst("bId")));

        return startGameService.startGame(a, b);
    }
}
