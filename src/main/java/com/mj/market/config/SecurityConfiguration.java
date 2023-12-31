package com.mj.market.config;

import com.mj.market.app.user.CustomUserDetailsService;
import com.mj.market.app.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //TODO custom Authority Provider impl
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    private static final String[] AUTHENTICATED_ENDPOINTS = {
            "/alerts", "/user", "/articles/new", "/price_alerts", "/price_alert_new", "/price_alert"};

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.httpBasic()
                .and().authorizeRequests()

                .antMatchers("/","/articles","/home").permitAll()
                .antMatchers(HttpMethod.POST,"/articles").hasAnyRole(Role.ADMIN.name())
                .antMatchers(HttpMethod.DELETE,"/articles").hasRole(Role.ADMIN.name())
                .antMatchers("/users","/register").hasRole(Role.ADMIN.name())
                .antMatchers("/alerts").authenticated()

                .and().formLogin()
                .loginProcessingUrl("/login").usernameParameter("username").passwordParameter("password")
                .defaultSuccessUrl("/home")
                .loginPage("/login").permitAll()

                .and().rememberMe()

                .and().logout().logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID","remember-me" )
                .logoutSuccessUrl("/home")

                .and().csrf().disable();

    }

/*    @Bean  //Spring Boot v3..
    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.POST, "/articles").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE,"/articles").hasAuthority(Role.ADMIN.name())
                        .requestMatchers("/users","/register").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(AUTHENTICATED_ENDPOINTS).authenticated()
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .formLogin(formLogin -> formLogin
                            .loginProcessingUrl("/login")
                            .usernameParameter("username")
                            .passwordParameter("password")
                            .loginPage("/login")
                            .defaultSuccessUrl("/home")
                            .permitAll())
                .logout(logout -> logout
                            .logoutUrl("/home")
                            .permitAll()

        );
        return http.build();
    }*/
}
