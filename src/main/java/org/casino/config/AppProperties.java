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


    // New environment variables
    private String accessKeyId;          // Maps to ACCESS_KEY_ID
    private String amplifyAppId;         // Maps to AMPLIFY_APP_ID
    private String amplifyEnvironment;   // Maps to AMPLIFY_ENVIRONMENT
    private String amplifyRegion;        // Maps to AMPLIFY_REGION
    private String backendApiUrl;        // Maps to BACKEND_API_URL
    private String buildCommand;         // Maps to BUILD_COMMAND
    private String databaseDriver;       // Maps to DATABASE_DRIVER
    private String databaseUrl;          // Maps to DATABASE_URL
    private String databaseUser;         // Maps to DATABASE_USER
    private String databasePassword;     // Maps to DATABASE_PASSWORD
    private String frontendUrl;          // Maps to FRONTEND_URL
    private int port;                    // Maps to PORT
    private String sqliteDbPath;         // Maps to SQLITE_DB_PATH
}
