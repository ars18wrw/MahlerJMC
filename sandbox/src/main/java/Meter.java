import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Write;

import java.util.Vector;

//=========================================================
//The accents method puts strong dynamics at the
//beginning of every bar assuming the meter is
// 7/8, 7/8, 7/8, 3/8
//Other metres can be made by modifying the == statements
//in the accents() method
//@author Andrew Brown and Marian Collier
//=========================================================

public final class Meter implements JMC{

    public static void main(String[] args) {
        new Meter();
    }

    public Meter() {
        Part p = new Part();

        for(int j=0;j<10;j++) { //make lots of phrases
            Phrase phr = new Phrase((double)(int)(Math.random()*8.0));
            int pitch = (int)(Math.random()*50)+50;
            for(int i = 0; i<100; i ++) { //make lots of notes
                Note n2 = new Note(pitch,(int)(Math.random()*2+1)*0.5,(int)(Math.random()*30+50));
                phr.addNote(n2);
            }
            //do accents
            accents(phr);
            p.addPhrase(phr);
        }

        Score s= new Score();
        s.addPart(p);

        Write.midi(s, "Meter.mid");
    }

    public static void accents(Phrase phrase) {
        double beatCounter = phrase.getStartTime();
        Vector v = phrase.getNoteList();
        for(int i=0;i<v.size();i++) {
            Note n = (Note)v.elementAt(i);
            if (beatCounter%12 == 0.0 || beatCounter%12 == 3.5 || beatCounter%12 == 7.0 || beatCounter%12 == 10.5) n.setDynamic(127);
            beatCounter += n.getRhythmValue();
        }
    }

}
