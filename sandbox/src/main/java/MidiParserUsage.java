package main.java;

import org.jfugue.Pattern;
import org.jfugue.Player;

import javax.sound.midi.InvalidMidiDataException;
import java.io.File;
import java.io.IOException;

/**
 * Created by Уладзімір Асіпчук on 28.09.15.
 */
public class MidiParserUsage {
    private static final String MIDI = "/sandbox/src/main/resources/mz_331_1.mid";
    public static void  main(String[] args) {
        Player player = new Player();
        System.out.println();
        Pattern pattern = null;
        try {
            pattern = player.loadMidi(
                    new File(new File( "." ).getCanonicalPath()+ MIDI));
        } catch (IOException | InvalidMidiDataException e) {
            e.printStackTrace();
        }
        System.out.println(pattern.getMusicString());
        //player.play(pattern);
    }
}
