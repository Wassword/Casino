import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicPlayer {
    private MediaPlayer mediaPlayer;

    public void playBackgroundMusic() {
        String musicFile = getClass().getClassLoader().getResource("music/Quirky-Dog(chosic.com).mp3").toString();
        Media sound = new Media(musicFile);
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the music indefinitely
        mediaPlayer.play();
    }

    public void stopBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}
