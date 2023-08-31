package com.mj.market.config;

import com.mj.market.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;



public class SecurityConfig  {
//extends WebSecurityConfigurerAdapter
    private final UserService userService;

    private static final String ROLE_MODERATOR = "ROLE_MODERATOR";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ADMIN = "ADMIN";
    private static final String MODERATOR = "MODERATOR";

    @Autowired
    public SecurityConfig(UserService userDetailsService) {
        this.userService = userDetailsService;

    }

    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
         auth.userDetailsService(userService);
        //auth.authenticationProvider(daoAuthenticationProvider());
    }*/
/*

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(UserService.passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }
*/
/*
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and().authorizeRequests()

                .antMatchers("/","/articles","/home").permitAll()
                .antMatchers(HttpMethod.POST,"/articles").hasAnyRole(ADMIN)
                .antMatchers(HttpMethod.DELETE,"/articles").hasRole(ADMIN)
                .antMatchers("/users","/register").hasRole(ADMIN)
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

    }*/



  /*  @EventListener(ApplicationReadyEvent.class)
    public void initUser(){
        User user1 = new User("Jan",passwordEncoder().encode("123"),ROLE_MODERATOR, "marcinzbrzozowa@gmail.com");
        User user2 = new User("Admin",passwordEncoder().encode("123"),ROLE_ADMIN, "marcinzbrzozowa@gmail.com");
        userRepository.save(user1);
        userRepository.save(user2);
    }*/
}
