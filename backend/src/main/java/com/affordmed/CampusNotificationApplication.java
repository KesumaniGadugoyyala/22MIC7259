package com.affordmed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@EnableCaching
@ConfigurationPropertiesScan
public class CampusNotificationApplication {
    public static void main(String[] args) {
        SpringApplication.run(CampusNotificationApplication.class, args);
    }
}
