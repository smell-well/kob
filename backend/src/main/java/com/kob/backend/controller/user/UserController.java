package com.kob.backend.controller.user;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserMapper userMapper;

    @GetMapping("/user/all/")
    public List<User> getAll() {
        return userMapper.selectList(null);
    }

    @GetMapping("/user/{user_id}/")
    public User getuser(@PathVariable int user_id) {
        return userMapper.selectById(user_id);
    }

    @GetMapping("/user/range/")
    public List<User> getRange() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("id", 1).le("id", 2);
        return userMapper.selectList(queryWrapper);
    }

    @GetMapping("/user/add/{user_id}/{username}/{password}")
    public String addUser(@PathVariable int user_id,
                          @PathVariable String username,
                          @PathVariable String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(user_id, username, encodedPassword);
        userMapper.insert(user);
        return "add user successfully";
    }

    @GetMapping("/user/remove/{user_id}")
    public String removeUser(@PathVariable int user_id) {
        userMapper.deleteById(user_id);
        return "remove user successfully";
    }
}
