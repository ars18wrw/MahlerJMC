
import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.View;
import jm.util.Write;

/*
* This class is an example of rule based composition
* Run the program and pass it a sentence
* The program will pick out letters and map them to pitches.
* This is based on ideas by the renaissance composer Guido
*/

public class LetterMelody implements JMC{

    public static void main(String[] args){
        Phrase phr = new Phrase();
        Part p = new Part();
        Score s = new Score("Letter Melody");

        // check if there is a some text typed in
        if (args.length == 0) {
            String[] arg2 = {"a"}; // {"This", "melody", "came", "from", "words"};
            args = arg2;
            System.out.println("You can type in your own words as arguments when you run this class next time.");
        }
        //iteracte through the words in thr string arg
        for(int i=0; i<args.length;i++) {
            System.out.println("Word "+i+" is "+args[i]);
            String word = args[i];
            //interat through the letters in each word
            for(int j=0;j<word.length();j++) {
                System.out.println("Letter "+j+" is "+ word.charAt(j));

                if(word.charAt(j) == 'a') {
                    Note n = new Note(A4,Q);
                    phr.addNote(n);
                }

                if(word.charAt(j) == 'b') {
                    Note n = new Note(B4,Q);
                    phr.addNote(n);
                }

                if(word.charAt(j) == 'c') {
                    Note n = new Note(C4,Q);
                    phr.addNote(n);
                }

                if(word.charAt(j) == 'd') {
                    Note n = new Note(D4,Q);
                    phr.addNote(n);
                }

                if(word.charAt(j) == 'e') {
                    Note n = new Note(E4,Q);
                    phr.addNote(n);
                }

                if(word.charAt(j) == 'f') {
                    Note n = new Note(F4,Q);
                    phr.addNote(n);
                }

                if(word.charAt(j) == 'g') {
                    Note n = new Note(G4,Q);
                    phr.addNote(n);
                }

                if(word.charAt(j) == ',') {
                    Note n = new Note(REST,Q);
                    phr.addNote(n);
                }
            }
        }

        p.addPhrase(phr);
        s.addPart(p);

        Write.midi(s, "LetterMelody.mid");

        View.show(s);
    }
}
