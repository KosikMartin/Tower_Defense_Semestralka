package towerdefense.gamelogic;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.File;
import java.io.IOException;

/**
 * Trieda pre prehrávanie zvukov v hre Tower Defense.
 *
 * Autor: Martin Košík
 * Dátum: 19.05.2024
 */
public class SoundPlayer {

    /**
     * Konštruktor triedy SoundPlayer.
     */
    public SoundPlayer() {

    }

    /**
     * Prehrá zvukový súbor zo zadanej cesty.
     *
     * @param filePath cesta k zvukovému súboru
     */
    public void play(String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile); //StackOverflow
            Clip clip = AudioSystem.getClip(); //StackOverflow
            clip.open(audioIn); //StackOverflow
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) { //StackOverflow
            e.printStackTrace();
        }
    }
}
