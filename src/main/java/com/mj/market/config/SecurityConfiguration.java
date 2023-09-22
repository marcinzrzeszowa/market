package com.mj.market.config;

import com.mj.market.app.user.JwtAuthenticationFilter;
import com.mj.market.app.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .authorizeHttpRequests()
                .anyRequest().permitAll()
                .and()
                .httpBasic()

                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .formLogin()
                .loginProcessingUrl("/login").usernameParameter("username").passwordParameter("password")
                .defaultSuccessUrl("/home")
                .loginPage("/login").permitAll()

                .and().rememberMe()

                .and().logout().logoutUrl("/logout")
                .deleteCookies("JSESSIONID","remember-me" )
                .logoutSuccessUrl("/home")
        ;

        return http.build();
    }


    /*@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET,"/css/**").permitAll() //static resources
                .requestMatchers("/auth/**", "/reg").permitAll()
                .requestMatchers("/", "/articles", "/home").permitAll()
                .requestMatchers(HttpMethod.POST, "/articles").hasAnyRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/articles").hasRole(Role.ADMIN.name())
                .requestMatchers("/users", "/register").hasRole(Role.ADMIN.name())
                .requestMatchers("/alerts").authenticated()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .formLogin()
                .loginProcessingUrl("/login").usernameParameter("username").passwordParameter("password")
                .defaultSuccessUrl("/home")
                .loginPage("/login").permitAll()

                .and().rememberMe()

                .and().logout().logoutUrl("/logout")
                .deleteCookies("JSESSIONID","remember-me" )
                .logoutSuccessUrl("/home")
        ;

        return http.build();
    }*/

}
