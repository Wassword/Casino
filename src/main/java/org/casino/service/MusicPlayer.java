package org.casino.service;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class MusicPlayer {

    private MediaPlayer mediaPlayer;

    // Method to play background music
    public void playBackgroundMusic() {
        try {
            URL resource = getClass().getClassLoader().getResource("music/BossaBossa(chosic.com).wav");
            if (resource != null) {
                Media sound = new Media(resource.toString());
                mediaPlayer = new MediaPlayer(sound);

                // Loop the music indefinitely
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                mediaPlayer.setVolume(1.0);  // Adjust the volume

                // Start the music
                mediaPlayer.play();
                System.out.println("Music started.");
            } else {
                System.out.println("Music file not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();  // Print error stack trace for debugging
        }
    }

    // Method to stop background music
    public void stopBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();  // Stop the music if it's playing
        }
    }
}
