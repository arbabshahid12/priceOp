package com.priceComparison.config;

import com.priceComparison.model.SuperUser;
import com.priceComparison.repositories.SuperUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppConfig {

    @Autowired
    private SuperUserRepository superUserRepository;

    @Bean
    public CommandLineRunner demoData() {
        return args -> {
            // Check if the default user already exists
            if (superUserRepository.findByEmail("admin") == null) {
                // Create a default SuperUser
                System.out.println("in");
                SuperUser defaultSuperUser = SuperUser.builder()
                        .email("admin")
                        .password(new BCryptPasswordEncoder().encode("admin")) // Encode password
                        .name("Admin")
                        .build();

                superUserRepository.save(defaultSuperUser);
            }
        };
    }
}
