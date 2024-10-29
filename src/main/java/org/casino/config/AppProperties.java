package org.casino.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    // Getters and Setters
    private String defaultUsername;
    private String defaultPassword;
    private int defaultBalance;
}
