package com.Quiz.QuizApplication.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner testPassword(PasswordEncoder passwordEncoder) {

        return args -> {

            String password = "admin123";

            String encoded = passwordEncoder.encode(password);

            System.out.println("=================================");
            System.out.println("PASSWORD: " + password);
            System.out.println("BCrypt: " + encoded);
            System.out.println("MATCH: " +
                    passwordEncoder.matches(password, encoded));
            System.out.println("=================================");
        };
    }
}