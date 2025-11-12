package com.meucurriculo.configurations;

import com.meucurriculo.services.DatabaseService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("development")
@Configuration
public class InitializationConfiguration {

    private final DatabaseService databaseService;

    public InitializationConfiguration(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Bean
    @Primary
    public boolean createMyResume() {
        return databaseService.createMyResume();
    }
}
