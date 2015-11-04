import jm.music.data.*;
import jm.music.tools.*;
import jm.JMC;
import jm.util.*;

public class RepeatTest implements JMC {
    public static void main(String[] args) {
        Phrase phr = new Phrase();

        //create a random walk
        int pitch = 60;
        for(int i = 0; i< 12; i++) {
            Note n = new Note(pitch+= (int)(Math.random() * 8 - 4), SQ *
                    (int)(Math.random() * 2 + 1) );
            phr.addNote(n);
        }

        // repeat the whole thing three times
        Mod.repeat(phr, 3);

        // see the result at this stage
        View.show(phr);

        // repeat beats 2 - 4 three times
        Mod.repeat(phr, 3 ,2.0, 4.0);

        // see the result of this change
        View.show(phr, 50, 50);

        // hear the result
        Part p = new Part();
        Score s = new Score();
        p.addPhrase(phr);
        s.addPart(p);
        s.setTempo(140.0);

        Play.midi(s, false);
        //false so that it doesn't close everything, the view.show() window 
        //in particular

        Write.midi(s, "Repeat.mid");
    }
}
        
            
