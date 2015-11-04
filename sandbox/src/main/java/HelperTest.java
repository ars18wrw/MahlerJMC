import jm.JMC;
import jm.audio.Instrument;
import jm.gui.helper.HelperGUI;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;

/**
 * @author Andrew Brown
 */
public final class HelperTest extends HelperGUI implements JMC{

    public static void main(String[] args){
        new HelperTest();
    }

    public HelperTest() {
        super();
        setVariableA(10, "Range");
        setVariableB(60, "Note length");
    }

    public Score compose() {
        Score s = new Score("Test");
        Part p = new Part("Guitar", 0, 0);
        Phrase  scale = new Phrase();

        //create the scale phrase note by note
        for(int i=0;i<12;){
            int pitch = C4+(int)(Math.random() * variableA + 1) - variableA / 2;
            if (pitch>=0&&pitch<=127){
                Note n = new Note(pitch, DEMI_SEMI_QUAVER *
                        (int)(Math.random()*variableB + 1));
                scale.addNote(n);
                i++;
            }
        }

        p.addPhrase(scale);
        s.addPart(p);

        Instrument sine = new SineInst(44100);
        Instrument[] instArray = {sine};
        insts = instArray;

        return s;
    }
}