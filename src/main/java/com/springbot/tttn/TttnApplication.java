package com.springbot.tttn;

import com.springbot.tttn.application.configs.LogFileConfig;
import com.springbot.tttn.infrastructure.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TttnApplication {
    public static void main(String[] args) {
        SpringApplication.run(TttnApplication.class, args);
    }

}
