package com.priceComparison.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

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
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                                .requestMatchers(HttpMethod.GET,"/**").permitAll()
                                .requestMatchers("/CurrentLiveProduct/**").permitAll()
                                .requestMatchers("/mainProduct/**").permitAll()
                                .requestMatchers("/superuser/**").permitAll()
                                .requestMatchers("/merchants/**").permitAll()
                                .requestMatchers("/products/**").hasRole("MERCHANT")
                                .requestMatchers("/categories/**").permitAll()
                                .requestMatchers("/superuser/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/merchants/login").permitAll()
                                .requestMatchers(HttpMethod.POST,"/superuser/login").permitAll()
                                .requestMatchers(HttpMethod.GET, "/loginSignup").permitAll()
//                                .requestMatchers("/loginSignup", "/register", "/login").permitAll()
                                .requestMatchers(HttpMethod.GET, "/merchant_section").hasRole("MERCHANT")
                                .anyRequest().permitAll()

                )

                .formLogin(form -> form
                                .loginPage("/loginSignup")  // Customize login page if you have one, or remove it to use default
                                .defaultSuccessUrl("/merchant_section", true)
                                .failureUrl("/loginSignup?error=true")  // Redirect to this on failure
                                .permitAll()
                )
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }



}