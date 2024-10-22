package org.casino;

import lombok.Getter;
import org.casino.config.AppProperties;
import org.casino.service.BlackjackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Scanner;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class CasinoApplication {
    public static void main(String[] args) {
        SpringApplication.run(CasinoApplication.class, args);
    }
}


