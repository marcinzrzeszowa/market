package com.mj.market.config;

import com.mj.market.app.user.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    //TODO change
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .authorizeHttpRequests()
                .anyRequest().permitAll()
                .and().httpBasic()
                .and()
                .authenticationProvider(authenticationProvider())
                .formLogin()
                .loginProcessingUrl("/login").usernameParameter("username").passwordParameter("password")
                .defaultSuccessUrl("/home")
                .loginPage("/login").permitAll()
                .and().rememberMe()
                .and().logout().logoutUrl("/logout")
                .deleteCookies("JSESSIONID","remember-me" )
                .logoutSuccessUrl("/home");

        return http.build();
    }

}
