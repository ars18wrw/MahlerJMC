//package accords;
//
//import jm.music.data.*;
//import jm.music.tools.Mod;
//import jm.util.Read;
//import jm.util.Write;
//
//import java.util.*;
//
///**
// * Created by Уладзімір Асіпчук on 23.04.16.
// */
//public final class Harmonization {
//    private static final int COMPOSE_PIECE_SIZE = 150;
//
//    private static final String FOLDER = "melodies\\";
//    private static final String NAME = "152194";
//    private static final String EXT = ".mid";
//
//    protected Map<ChordType, PriorityQueue<ChordType>> rules;
//    protected Map<Degrees, List<ChordType>> neighbourhood;
//
//
//    private final String className;
//
//    private Score s = new Score();
//
//    public Harmonization() {
//        className = getClass().getName().toString();
//
//
//        // rules
//        rules = new HashMap<>();
//        PriorityQueue<ChordType> queue;
//
//        // T
//        queue = new PriorityQueue<>();
//        queue.add(ChordType.T);
//        queue.add(ChordType.S);
//        queue.add(ChordType.D);
//        rules.put(ChordType.T, queue);
//
//        // S
//        queue = new PriorityQueue<>();
//        queue.add(ChordType.S);
//        queue.add(ChordType.D);
//        queue.add(ChordType.T);
//        rules.put(ChordType.S, queue);
//
//        // D
//        queue = new PriorityQueue<>();
//        queue.add(ChordType.D7);
//        queue.add(ChordType.T);
//        queue.add(ChordType.D);
//        rules.put(ChordType.D, queue);
//
//        // D7
//        queue = new PriorityQueue<>();
//        queue.add(ChordType.T);
//        queue.add(ChordType.D7);
//        rules.put(ChordType.D7, queue);
//
//
//        // neighbourhood
//        neighbourhood = new HashMap<>();
//
//        // I
//        queue = new PriorityQueue<>();
//        queue.add(ChordType.T);
//        queue.add(ChordType.S);
//        neighbourhood.put(Degrees.I, queue);
//
//        // II
//        queue = new ArrayList<>();
//        queue.add(ChordType.D);
//        queue.add(ChordType.D7);
//        neighbourhood.put(Degrees.II, queue);
//
//        // III
//        queue = new ArrayList<>();
//        queue.add(ChordType.T);
//        neighbourhood.put(Degrees.III, queue);
//
//        // IV
//        queue = new ArrayList<>();
//        queue.add(ChordType.S);
//        queue.add(ChordType.D7);
//        neighbourhood.put(Degrees.IV, queue);
//
//        // V
//        queue = new ArrayList<>();
//        queue.add(ChordType.T);
//        queue.add(ChordType.D);
//        queue.add(ChordType.D7);
//        neighbourhood.put(Degrees.V, queue);
//
//        // VI
//        queue = new ArrayList<>();
//        queue.add(ChordType.S);
//        neighbourhood.put(Degrees.VI, queue);
//
//        // VII
//        queue = new ArrayList<>();
//        queue.add(ChordType.D);
//        queue.add(ChordType.D7);
//        neighbourhood.put(Degrees.VII, queue);
//
//    }
//
//    public static void main(String[] args) {
//        new Harmonization().process();
//    }
//
//    public void process() {
//        List<Note> pitchesToHarmonize = new ArrayList<>();
//
//        /* Read midi */
//        Read.midi(s, FOLDER + NAME + EXT);
////
//        Part part;
//        for (int jj = 0; jj < s.size(); jj++) {
//            part = s.getPart(jj);
//            for (int i = 0; i < part.getSize(); i++) {
//                Phrase phrase = part.getPhrase(i);
//                if (phrase.getSize() < 1) {
//                    continue;
//                }
//                // TODO You should define the Mode; We temporary process only Major
//                Mod.transpose(phrase, -2);
//                for (int j = 0; j < phrase.getSize(); j++) {
//                    if (phrase.getNote(j).getPitch() != Note.REST) {
//                        pitchesToHarmonize.add(new Note(phrase.getNote(j).getPitch()+12, phrase.getNote(j).getRhythmValue()));
//                    }
//                }
//            }
//
//        }
//
//
//        List<ChordType> possibleChords = new ArrayList<>();
//        possibleChords.add(ChordType.T);
//        possibleChords.add(ChordType.S);
//        possibleChords.add(ChordType.D);
//
//        ChordType[] result = new ChordType[pitchesToHarmonize.size()];
//        recursion(pitchesToHarmonize, possibleChords, 0, result);
//
//        for (ChordType chord : result) {
//            System.out.println(chord);
//        }
//        Score score = new Score();
//        Part accompaniment = new Part();
//        Part solo = new Part();
//        for (int i = 0; i < result.length; i++) {
//
//            CPhrase cPhraseAccompaniment = SimpleAccords.buildAccord(pitchesToHarmonize.get(i), result[i]);
//            accompaniment.addCPhrase(cPhraseAccompaniment);
//            CPhrase cPhraseSolo = new CPhrase();
//            cPhraseSolo.addPhrase(new Phrase(pitchesToHarmonize.get(i)));
//            solo.addCPhrase(cPhraseSolo);
//
//
//        }
//        solo.setChannel(2);
//        solo.setInstrument(Part.VIOLIN);
//        score.add(solo);
//        score.add(accompaniment);
//        score.setTempo(60);
//        Write.midi(score, "Harmonization.mid");
//    }
//
//
//    public boolean recursion(List<Note> pitches, List<ChordType> accords, int currentPitchNumberInList, ChordType[] result) {
//        if (pitches.size() == currentPitchNumberInList) {
//            return true;
//        }
//        accords.retainAll(neighbourhood.get(getDegreeByPitch(pitches.get(currentPitchNumberInList).getPitch())));
//        if (0 == accords.size()) {
//            // should harmonize with other accord before
//            return false;
//        }
//
//        // the last in most cases should be tonic
//        if (currentPitchNumberInList-1 == pitches.size() && accords.contains(ChordType.T)) {
//            result[currentPitchNumberInList] = ChordType.T;
//            return true;
//        }
//
//        int numberOfHarmonyUsed = 0;
//        while (numberOfHarmonyUsed < accords.size() && !recursion(pitches, new ArrayList<>(rules.get(accords.get(numberOfHarmonyUsed))), currentPitchNumberInList + 1, result)) {
//            numberOfHarmonyUsed++;
//        }
//        if (numberOfHarmonyUsed == accords.size()) {
//            return false;
//        }
//        result[currentPitchNumberInList] = accords.get(numberOfHarmonyUsed);
//        return true;
//    }
//
//    public Degrees getDegreeByPitch(int pitch) {
//        int i = (pitch) % 12;
//        switch (i) {
//            case 0:
//                return Degrees.I;
//            case 2:
//                return Degrees.II;
//            case 4:
//                return Degrees.III;
//            case 5:
//                return Degrees.IV;
//            case 7:
//                return Degrees.V;
//            case 9:
//                return Degrees.VI;
//            case 11:
//                return Degrees.VII;
//            default:
//                return Degrees.I;
//        }
//    }
//
//}
//
