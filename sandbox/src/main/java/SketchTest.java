import jm.music.data.*;
import jm.JMC;
import jm.util.View;

//@author Andrew Brown

public class SketchTest implements JMC {
    public static void main(String[] args) {
        Score s = new Score("Sketch test");
        Part m = new Part("Melody", 1, FLUTE);
        Phrase phr = new Phrase();
        int pitch = 60;
        for(int i = 0; i< 50; i++) {
            Note n = new Note(pitch, DSQ * (int)(Math.random()*8 + 1));
            phr.addNote(n);
            pitch += (int)(Math.random()*20 - 10);
            if(pitch < 0 || pitch > 127) pitch = 60;
        }
        // display the phrase
        View.sketch(phr);
    }
}     
            
