package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> add(Map<String, String> data) {
        Map<String, String> map = new HashMap<>();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl userDetails = (UserDetailsImpl) usernamePasswordAuthenticationToken.getPrincipal();
        User user = userDetails.getUser();

        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        if (title == null || title.length() == 0) {
            map.put("error_message", "名称不能为空");
            return map;
        }

        if (title.length() > 100) {
            map.put("error_message", "名称过长");
        }

        if (description == null || description.length() == 0) {
            description = "这个人很懒~";
        }

        if (description.length() > 300) {
            map.put("error_message", "描述过长");
            return map;
        }

        if (content == null || content.length() == 0) {
            map.put("error_message", "代码内容不能为空");
            return map;
        }

        if (content.length() > 10000) {
            map.put("error_message", "代码过长");
            return map;
        }
        Date now = new Date();
        Bot bot = new Bot(null, user.getId(), title, description, content, null,
                now, now);

        botMapper.insert(bot);
        map.put("error_message", "success");

        return map;
    }
}
