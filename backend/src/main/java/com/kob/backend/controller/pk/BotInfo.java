package com.kob.backend.controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pk/")
public class BotInfo {
    @RequestMapping("getbotinfo/")
    public Map<String, Integer> getBotInfor() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("y", 1000);
        map.put("w", 1500);
        return map;
    }
}
