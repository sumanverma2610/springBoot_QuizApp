package com.Quiz.QuizApplication.config;

import com.Quiz.QuizApplication.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfig(
            CustomUserDetailsService customUserDetailsService,
            PasswordEncoder passwordEncoder) {

        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(customUserDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // Public pages
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        // ADMIN ONLY
                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        // AI ONLY FOR ADMIN
                        .requestMatchers("/ai/**")
                        .hasRole("ADMIN")

                        // STUDENT ONLY
                        .requestMatchers(
                                "/quiz",
                                "/submitQuiz",
                                "/result",
                                "/my-results"
                        )
                        .hasRole("STUDENT")

                        // Everything else requires login
                        .anyRequest()
                        .authenticated()
                )

                .formLogin(form -> form

                        .loginPage("/login")

                        .loginProcessingUrl("/login")

                        .defaultSuccessUrl("/", true)

                        .permitAll()
                )

                .logout(logout -> logout

                        .logoutUrl("/logout")

                        .logoutSuccessUrl("/login?logout")

                        .permitAll()
                );

        return http.build();
    }
}