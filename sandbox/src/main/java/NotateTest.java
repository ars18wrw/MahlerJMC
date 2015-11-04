import jm.music.data.*;
import jm.JMC;
import jm.util.*;

public class NotateTest implements JMC {
    public static void main(String[] args) {
        Phrase phr = new Phrase();
        for(int i = 0; i< 50; i++) {
            Note n = new Note((int)(Math.random()*60 + 30), SQ * (int)(Math.random()*8 + 1));
            phr.addNote(n);
        }
        View.notate(phr);
    }
}