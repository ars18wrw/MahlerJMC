package accords;

import jm.music.data.*;
import jm.util.Read;
import jm.util.Write;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Уладзімір Асіпчук on 23.04.16.
 */
public final class MajorHarmonization {
    // TODO Specify
    private static final String FOLDER = "melodies\\";
    private static final String NAME = "papageno";
    private static final String EXT = ".mid";

    protected Map<ChordType, List<ChordType>> rules;
    protected Map<Degree, List<ChordType>> neighbourhood;

    private Score s = new Score();

    public MajorHarmonization() {
        // rules
        rules = new HashMap<>();
        List<ChordType> tempList;

        // T
        tempList = new ArrayList<>();
        tempList.add(ChordType.T);
        tempList.add(ChordType.S);
        tempList.add(ChordType.D);
        rules.put(ChordType.T, tempList);

        // S
        tempList = new ArrayList<>();
        tempList.add(ChordType.S);
        tempList.add(ChordType.D);
        tempList.add(ChordType.T);
        rules.put(ChordType.S, tempList);

        // D
        tempList = new ArrayList<>();
        tempList.add(ChordType.D7);
        tempList.add(ChordType.T);
        tempList.add(ChordType.D);
        tempList.add(ChordType.II);
        // The last, cause D->S is not commonly used
        tempList.add(ChordType.S);
        rules.put(ChordType.D, tempList);

        // D7
        tempList = new ArrayList<>();
        tempList.add(ChordType.T);
        tempList.add(ChordType.D7);
        tempList.add(ChordType.D);
        rules.put(ChordType.D7, tempList);

        // II
        tempList = new ArrayList<>();
        tempList.add(ChordType.T);
        tempList.add(ChordType.D);
        rules.put(ChordType.II, tempList);


        // neighbourhood
        neighbourhood = new HashMap<>();

        // I
        tempList = new ArrayList<>();
        tempList.add(ChordType.T);
        tempList.add(ChordType.S);
        neighbourhood.put(Degree.I, tempList);

        // II
        tempList = new ArrayList<>();
        tempList.add(ChordType.D);
        tempList.add(ChordType.D7);
        tempList.add(ChordType.II);
        neighbourhood.put(Degree.II, tempList);

        // III
        tempList = new ArrayList<>();
        tempList.add(ChordType.T);
        neighbourhood.put(Degree.III, tempList);

        // IV
        tempList = new ArrayList<>();
        tempList.add(ChordType.S);
        tempList.add(ChordType.D7);
        tempList.add(ChordType.II);
        neighbourhood.put(Degree.IV, tempList);

        // V
        tempList = new ArrayList<>();
        tempList.add(ChordType.T);
        tempList.add(ChordType.D);
        tempList.add(ChordType.D7);
        neighbourhood.put(Degree.V, tempList);

        // VI
        tempList = new ArrayList<>();
        tempList.add(ChordType.S);
        tempList.add(ChordType.II);
        neighbourhood.put(Degree.VI, tempList);

        // VII
        tempList = new ArrayList<>();
        tempList.add(ChordType.D);
        tempList.add(ChordType.D7);
        neighbourhood.put(Degree.VII, tempList);

    }

    public static void main(String[] args) {
        new MajorHarmonization().process();
    }

    public void process() {
        List<Note> pitchesToHarmonize = new ArrayList<>();

        /* Read midi */
        Read.midi(s, FOLDER + NAME + EXT);

        Part part;
        for (int jj = 0; jj < 1 /* only the solo voice */; jj++) {
            part = s.getPart(jj);
            for (int i = 0; i < part.getSize(); i++) {
                Phrase phrase = part.getPhrase(i);
                if (phrase.getSize() < 1) {
                    continue;
                }
                // TODO Specify the tonality by transposition to C
//                Mod.transpose(phrase, -7 - 12);
                for (int j = 0; j < phrase.getSize(); j++) {
                    if (phrase.getNote(j).getPitch() != Note.REST) {
                        pitchesToHarmonize.add(new Note(phrase.getNote(j).getPitch(), phrase.getNote(j).getRhythmValue()));
                    }
                }
            }

        }

        List<ChordType> possibleChords = new ArrayList<>();
        possibleChords.add(ChordType.T);
        possibleChords.add(ChordType.S);
        possibleChords.add(ChordType.D);

        ChordType[] result = new ChordType[pitchesToHarmonize.size()];
        recursion(pitchesToHarmonize, possibleChords, 0, result);

        Score score = new Score();
        Part accompaniment = new Part();
        Part solo = new Part();
        for (int i = 0; i < result.length; i++) {
            CPhrase cPhraseAccompaniment = SimpleAccords.buildAccord(pitchesToHarmonize.get(i), result[i]);
            accompaniment.addCPhrase(cPhraseAccompaniment);
            CPhrase cPhraseSolo = new CPhrase();
            cPhraseSolo.addPhrase(new Phrase(pitchesToHarmonize.get(i)));
            solo.addCPhrase(cPhraseSolo);
        }
        solo.setChannel(2);
        solo.setInstrument(Part.VIOLIN);
        score.add(solo);
        score.add(accompaniment);

        Write.midi(score, "Harmonization.mid");
    }


    public boolean recursion(List<Note> pitches, List<ChordType> accords, int currentPitchNumberInList, ChordType[] result) {
        if (pitches.size() == currentPitchNumberInList) {
            return true;
        }
        Degree degreeByPitch;
        try {
            degreeByPitch = getDegreeByPitch(pitches.get(currentPitchNumberInList).getPitch());
        } catch (ModulationException e) {
            degreeByPitch = e.getDegreeInModulatedTonality();
            // Make Modulation
        }
        accords.retainAll(neighbourhood.get(degreeByPitch));
        if (0 == accords.size()) {
            // should harmonize with other accord before
            return false;
        }

        // the last in most cases should be tonic
        if (currentPitchNumberInList-1 == pitches.size() && accords.contains(ChordType.T)) {
            result[currentPitchNumberInList] = ChordType.T;
            return true;
        }

        int numberOfHarmonyUsed = 0;
        while (numberOfHarmonyUsed < accords.size() && !recursion(pitches, new ArrayList<>(rules.get(accords.get(numberOfHarmonyUsed))), currentPitchNumberInList + 1, result)) {
            numberOfHarmonyUsed++;
        }
        if (numberOfHarmonyUsed == accords.size()) {
            // TODO : do not harmonize this
            result[currentPitchNumberInList] = result[currentPitchNumberInList-1];
//            return false;
        } else {
            result[currentPitchNumberInList] = accords.get(numberOfHarmonyUsed);
        }
        return true;
    }

    public Degree getDegreeByPitch(int pitch) throws ModulationException {
        int i = (pitch) % 12;
        switch (i) {
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
            case 6:
                // TODO Modulation to Dominant
                throw new ModulationException(Degree.VII);
            // TODO
            default:
                return Degree.I;
        }
    }
}

