package com.priceComparison.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        System.out.println("password ");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("CHeck auth");
        http.authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/").hasAnyAuthority( "MERCHANT", "ADMIN","USER")
                                .requestMatchers("/CurrentLiveProduct/**").permitAll()
                                .requestMatchers("mainProduct/**").permitAll()
                                .requestMatchers("/merchant/**").permitAll()
                                .requestMatchers("/product/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/merchant/login").permitAll()
                                .requestMatchers(HttpMethod.POST,"/USER/MERCHANT").permitAll()
                                .requestMatchers(HttpMethod.GET,"/loginSignup").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin.permitAll())
                .logout(logout -> logout.permitAll())
                .exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedPage("/403"));

        return http.build();
    }




}