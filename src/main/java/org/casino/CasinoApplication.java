package org.casino;

import javafx.application.Application;
import javafx.stage.Stage;
import org.casino.config.AppProperties;
import org.casino.service.MusicPlayer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class CasinoApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // JavaFX start method (can leave empty if no GUI is needed)
        // JavaFX initialization can happen here if you're creating any windows or stages
    }

    public static void main(String[] args) {
        // Start Spring Boot application
        SpringApplication.run(CasinoApplication.class, args);

        // Initialize JavaFX Application (ensures the JavaFX runtime is set up)
        launch(args);

        // Play background music after JavaFX is initialized
        MusicPlayer musicPlayer = new MusicPlayer();
        musicPlayer.playBackgroundMusic();
    }
}
