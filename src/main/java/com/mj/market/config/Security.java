package com.mj.market.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class Security {
    private static final String ROLE_MODERATOR = "ROLE_MODERATOR";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";
    private static final String MODERATOR = "MODERATOR";

    @Bean
    public PasswordEncoder getBcryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager get(){
        UserDetails user = User.withUsername("test")
                .password(getBcryptPasswordEncoder().encode("test"))
                .roles(USER)
                .build();
        UserDetails admin = User.withUsername("admin")
                .password(getBcryptPasswordEncoder().encode("123"))
                .roles(ADMIN)
                .build();
        return new InMemoryUserDetailsManager(Arrays.asList(user,admin));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/articles", "/home").permitAll()
                        .requestMatchers(HttpMethod.POST, "/articles").hasAnyRole(ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/articles").hasRole(ADMIN)
                        .requestMatchers("/users", "/register").hasRole(ADMIN)
                        .requestMatchers("/alerts").authenticated()
                )
                .formLogin((form)-> form
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/home")
                )

                .logout((logout) -> logout
                        .permitAll()
                        .logoutUrl("/logout")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                        .logoutSuccessUrl("/home")
                )
                .rememberMe((rememberMe)->rememberMe
                        .alwaysRemember(true)
                        )
                .csrf((csrf)->csrf.disable());
        return http.build();
    }
}
