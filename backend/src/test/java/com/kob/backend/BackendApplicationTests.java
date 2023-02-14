package com.kob.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("p2"));
        System.out.println(passwordEncoder.encode("pc"));

        System.out.println(passwordEncoder.matches("p2", "$2a$10$Rl9XSbaLHPPzQtVXq86c6OhT5b8pBAdJXaFFLrpRZY6/6I3Im0/Me"));
    }

}
