package org.casino;

import org.casino.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class CasinoApplication {
    public static void main(String[] args) {
        SpringApplication.run(CasinoApplication.class, args);
    }
}


