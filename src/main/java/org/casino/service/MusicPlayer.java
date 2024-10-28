package org.casino.service;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class MusicPlayer {

    private MediaPlayer mediaPlayer;

    // Method to play background music
    public void playBackgroundMusic() {
        try {
            // Load the music file from the resources directory
            URL resource = getClass().getClassLoader().getResource("music/Quirky-Dog(chosic.com).mp3");

            // Check if the resource is available
            if (resource != null) {
                String musicFile = resource.toString(); // Convert URL to String
                Media sound = new Media(musicFile);
                mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the music indefinitely
                mediaPlayer.play(); // Start the music
            } else {
                System.out.println("Music file not found!");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print error stack trace for debugging
        }
    }

    // Method to stop background music
    public void stopBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // Stop the music if it's playing
        }
    }
}
