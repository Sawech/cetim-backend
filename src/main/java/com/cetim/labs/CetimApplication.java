package com.cetim.labs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.File;

@SpringBootApplication
public class CetimApplication {
    private static final Logger logger = LoggerFactory.getLogger(CetimApplication.class);


    public static void main(String[] args) {
        File mysqlConfig = new File("src/main/resources/config.properties");

        if (mysqlConfig.exists()) {
            logger.info("Using PROFILE: config");
            System.setProperty("spring.profiles.active", "config");
            new SpringApplicationBuilder(CetimApplication.class)
                .properties("spring.config.location=file:src/main/resources/config.properties")
                .run(args);
        } else {
            logger.info("Using PROFILE: h2");
            System.setProperty("spring.profiles.active", "h2");
            SpringApplication.run(CetimApplication.class, args);
        }
    }
}

