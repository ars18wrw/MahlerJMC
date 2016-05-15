package accords;

import jm.constants.Pitches;
import jm.music.data.Score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Уладзімір Асіпчук on 23.04.16.
 */
public final class SimpleHarmonization {
    private static final int COMPOSE_PIECE_SIZE = 150;

    private static final String FOLDER = "originals\\";
    private static final String NAME = "fantaisie";
    private static final String EXT = ".mid";

    protected Map<Degree, List<Degree>> rules;
    protected Map<Degree, List<Degree>> neighbourhood;




    private final String className;

    private Score s = new Score();

    public SimpleHarmonization() {
        className = getClass().getName().toString();


        // rules
        rules = new HashMap<>();
        List<Degree> tempSet;

        // I
        tempSet = new ArrayList<>();
        tempSet.add(Degree.I);
        tempSet.add(Degree.II);
        tempSet.add(Degree.III);
        tempSet.add(Degree.IV);
        tempSet.add(Degree.V);
        tempSet.add(Degree.VI);
        tempSet.add(Degree.VII);
        rules.put(Degree.I, tempSet);

        // II
        tempSet = new ArrayList<>();
        tempSet.add(Degree.I);
        tempSet.add(Degree.III);
        tempSet.add(Degree.IV);
        rules.put(Degree.II, tempSet);

        // III
        tempSet = new ArrayList<>();
        tempSet.add(Degree.I);
        tempSet.add(Degree.IV);
        tempSet.add(Degree.V);
        tempSet.add(Degree.VI);
        tempSet.add(Degree.VII);
        rules.put(Degree.III, tempSet);

        // IV
        tempSet = new ArrayList<>();
        tempSet.add(Degree.I);
        tempSet.add(Degree.II);
        tempSet.add(Degree.V);
        rules.put(Degree.IV, tempSet);

        // V
        tempSet = new ArrayList<>();
        tempSet.add(Degree.I);
        tempSet.add(Degree.III);
        tempSet.add(Degree.IV);
        tempSet.add(Degree.VII);
        rules.put(Degree.V, tempSet);

        // VI
        tempSet = new ArrayList<>();
        tempSet.add(Degree.I);
        tempSet.add(Degree.II);
        tempSet.add(Degree.III);
        tempSet.add(Degree.IV);
        rules.put(Degree.VI, tempSet);

        // VII
        tempSet = new ArrayList<>();
        tempSet.add(Degree.I);
        rules.put(Degree.VII, tempSet);

        // neighbourhood
        neighbourhood = new HashMap<>();

        // I
        tempSet = new ArrayList<>();
        tempSet.add(Degree.I);
        tempSet.add(Degree.IV);
        tempSet.add(Degree.VI);
        neighbourhood.put(Degree.I, tempSet);

        // II
        tempSet = new ArrayList<>();
        tempSet.add(Degree.II);
        tempSet.add(Degree.V);
        tempSet.add(Degree.VII);
        neighbourhood.put(Degree.II, tempSet);

        // III
        tempSet = new ArrayList<>();
        tempSet.add(Degree.I);
        tempSet.add(Degree.III);
        tempSet.add(Degree.VI);
        neighbourhood.put(Degree.III, tempSet);

        // IV
        tempSet = new ArrayList<>();
        tempSet.add(Degree.II);
        tempSet.add(Degree.IV);
        tempSet.add(Degree.VII);
        neighbourhood.put(Degree.IV, tempSet);

        // V
        tempSet = new ArrayList<>();
        tempSet.add(Degree.I);
        tempSet.add(Degree.III);
        tempSet.add(Degree.V);
        neighbourhood.put(Degree.V, tempSet);

        // VI
        tempSet = new ArrayList<>();
        tempSet.add(Degree.II);
        tempSet.add(Degree.IV);
        tempSet.add(Degree.VI);
        neighbourhood.put(Degree.VI, tempSet);

        // VII
        tempSet = new ArrayList<>();
        tempSet.add(Degree.III);
        tempSet.add(Degree.V);
        tempSet.add(Degree.VII);

        neighbourhood.put(Degree.VII, tempSet);

    }

    public static void main(String[] args) {
        new SimpleHarmonization().process();
    }

    public void process() {
//        /* Read midi */
//        Read.midi(s, FOLDER + NAME + EXT);
//        s.setTitle(NAME);
//        /* Create folders */
//        new File("music/" + className).mkdir();

        List<Integer> pitchesToHarmonize = new ArrayList<>();



//
//
//        // TODO Harmonize every part
//        Part part = s.getPart(0);
//        for (int i = 0; i < part.getSize(); i++) {
//            Phrase phrase = part.getPhrase(i);
//            if (phrase.getSize() < 1) {
//                continue;
//            }
//            for (int j = 0; j < phrase.getSize(); j++) {
//                if (phrase.getNote(j).getPitch() < 0) {
//                    pitchesToHarmonize.add(phrase.getNote(j).getPitch());
//                }
//            }
//        }

        pitchesToHarmonize.add(Pitches.c4);
        pitchesToHarmonize.add(Pitches.d4);
        pitchesToHarmonize.add(Pitches.e4);
        pitchesToHarmonize.add(Pitches.f4);
        pitchesToHarmonize.add(Pitches.g4);
        pitchesToHarmonize.add(Pitches.a4);
        pitchesToHarmonize.add(Pitches.b4);
        pitchesToHarmonize.add(Pitches.c4);



        List<Degree> allDegs = new ArrayList<>();
        allDegs.add(Degree.I);
        allDegs.add(Degree.II);
        allDegs.add(Degree.III);
        allDegs.add(Degree.IV);
        allDegs.add(Degree.V);
        allDegs.add(Degree.VI);
        allDegs.add(Degree.VII);

        Degree[] degreeses = new Degree[pitchesToHarmonize.size()];
        recursion(pitchesToHarmonize, allDegs, 0, degreeses);

        for (Degree deg : degreeses) {
            System.out.println(deg);
        }





//        Write.midi(scoreTogether, "music/" + className + "/" + NAME + "/tutti.mid");
    }


    public boolean recursion(List<Integer> pitches, List<Degree> accords, int currentPitchNumberInList, Degree[] result) {
        if (pitches.size() == currentPitchNumberInList) {
            return true;
        }
        accords.retainAll(neighbourhood.get(getDegreesByPitch(pitches.get(currentPitchNumberInList))));
        if (0 == accords.size()) {
            // should harmonize with other accord before
            return false;
        }
        int numberOfHarmonyUsed = 0;
        while (numberOfHarmonyUsed < accords.size() && !recursion(pitches, rules.get(accords.get(numberOfHarmonyUsed)), currentPitchNumberInList+1, result)) {
            numberOfHarmonyUsed++;
        }
        result[currentPitchNumberInList] = accords.get(numberOfHarmonyUsed);
        return true;
    }

    public Degree getDegreesByPitch(int pitch) {
        int i = pitch % 12;
        switch(i) {
            case 0:
                return Degree.I;
            case 2:
                return Degree.II;
            case 4:
                return Degree.III;
            case 5:
                return Degree.IV;
            case 7:
                return Degree.V;
            case 9:
                return Degree.VI;
            case 11:
                return Degree.VII;
            default:
                return Degree.I;
        }
    }

}

