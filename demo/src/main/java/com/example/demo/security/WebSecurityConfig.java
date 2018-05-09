package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig
        extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService() {
        return new SpringSecurityUserDetails();
    };

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    };

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/css/**").permitAll()
            .antMatchers("/img/**").permitAll()
            .antMatchers("/js/**").permitAll()
            .antMatchers("/webjars/**").permitAll()
            .antMatchers("/").permitAll()
            .antMatchers("/customer/**").permitAll()
            .antMatchers("/product/productimage/**").permitAll()
            .anyRequest().authenticated()
            .and()
        .formLogin()
            .loginPage("/identity/login")
            .defaultSuccessUrl("/identity/dashboard", true)
            .permitAll()
            .and()
        .logout()
            .permitAll();
//            .logoutUrl("/identity/logout")
//            .logoutSuccessUrl("/identity/login");
    }
}
