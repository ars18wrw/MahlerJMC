package accords;

import jm.constants.Durations;
import jm.constants.Pitches;
import jm.constants.ProgramChanges;
import jm.constants.RhythmValues;
import jm.music.data.*;
import jm.util.Write;

import java.util.*;

/**
 * Created by Уладзімір Асіпчук on 08.11.15.
 */
public class SimpleAccords {
    protected Set<Integer> tonicPitches;
    protected Set<Integer> subdominantPitches;
    protected Set<Integer> dominantPitches;
    protected Set<Integer> sixPitches;
    protected Set<Integer> dominant7Pitches;


    protected Map<ChordType, List<ChordType>> rules;

    protected static final int TONIC_ROOT = Pitches.C4;
    protected static final int SUBDOMINANT_ROOT = Pitches.F4;
    protected static final int DOMINANT_ROOT = Pitches.G4;
    protected static final int SIX_ROOT = Pitches.A4;
    protected static final int SECOND_ROOT = Pitches.D4;



    protected Score s = new Score();

    SimpleAccords() {
        // root accord pitches
        tonicPitches = new TreeSet<Integer>();
        subdominantPitches = new TreeSet<Integer>();
        dominantPitches = new TreeSet<Integer>();
        sixPitches = new TreeSet<Integer>();
        dominant7Pitches = new TreeSet<Integer>();

        // by T53
        addPitches(tonicPitches, 0);
        addPitches(tonicPitches, 4);
        addPitches(tonicPitches, 4 + 3);
        // by S64
        addPitches(subdominantPitches, 0);
        addPitches(subdominantPitches, 5);
        addPitches(subdominantPitches, 5 + 4);
        // by D64
        addPitches(dominantPitches, 2);
        addPitches(dominantPitches, 2 + 5);
        addPitches(dominantPitches, 2 + 5 + 4);
        // by VI6
        addPitches(sixPitches, 0);
        addPitches(sixPitches, 4);
        addPitches(sixPitches, 4 + 5);
        // by D43
        addPitches(dominant7Pitches, 2);
        addPitches(dominant7Pitches, 2 + 3);
        addPitches(dominant7Pitches, 2 + 5);
        addPitches(dominant7Pitches, 2 + 5 + 4);


        // rules
        rules = new HashMap<>();
        List<ChordType> tempSet;
        // T
        tempSet = new ArrayList<>();
        tempSet.add(ChordType.T);
        tempSet.add(ChordType.S);
        tempSet.add(ChordType.D);
        rules.put(ChordType.T, tempSet);
        // S
        tempSet = new ArrayList<>();
        tempSet.add(ChordType.T);
        tempSet.add(ChordType.S);
        tempSet.add(ChordType.D);
        rules.put(ChordType.S, tempSet);

        // D
        // !!! D -> S is forbidden
        tempSet = new ArrayList<>();
        tempSet.add(ChordType.D7);
        tempSet.add(ChordType.D);
        //   tempSet.add(ChordType.VI);
        rules.put(ChordType.D, tempSet);

        // D7
        // !!! D -> S is forbidden
        tempSet = new ArrayList<>();
        tempSet.add(ChordType.T);
        tempSet.add(ChordType.D7);
        rules.put(ChordType.D7, tempSet);

        // VI
//        tempSet = new ArrayList<>();
//        tempSet.add(ChordType.T);
//       // tempSet.add(ChordType.VI);
//        rules.put(ChordType.VI, tempSet);
    }

    public static void main(String[] args) {
        new SimpleAccords().process();
    }

    public void process() {
        Note root = new Note(Pitches.C4, RhythmValues.EIGHTH_NOTE);
        Part accompaniment = new Part();
        Part solo = new Part(ProgramChanges.VIOLA);

        Part part = new Part();

        int accordsNum = 0;
        ChordType type = ChordType.T;
        System.out.print(type);
        Random randomGenerator = new Random();
        do {
            if (type != ChordType.D7) {
                CPhrase cPhrase = buildAccord(root, type);
                part.addCPhrase(cPhrase);

//                accompaniment.addCPhrase(getAccompaniment(cPhrase));
//                solo.addCPhrase(getTheHighestVoice(cPhrase));
//
            } else {
                CPhrase cPhrase = build7Accord(root, type);
//                accompaniment.addCPhrase(getAccompaniment(cPhrase));
//                solo.addCPhrase(getTheHighestVoice(cPhrase));

                part.addCPhrase(cPhrase);
            }
            List<ChordType> list = rules.get(type);
            // next accord type (the harmony of the next accord)
            type = list.get(randomGenerator.nextInt(rules.get(type).size()));
            System.out.print(type);

            // whether the next accord should be higher or not
            boolean isUp = randomGenerator.nextBoolean();
            // do not go very high
            if (isUp && root.getPitch() > Pitches.C5) {
                isUp = false;
            }
            // do not go very high
            if (!isUp && root.getPitch() < Pitches.C3) {
                isUp = true;
            }
            // TODO MAke donot return to the same accord
            // TODO make some distribution. for example, from D to D7 is more obvious, then to T.
            switch (type) {
                case T:
                    root = new Note(getClosestPitchOfAccord(tonicPitches, root.getPitch(), isUp), root.getDuration());
                    break;
                case S:
                    root = new Note(getClosestPitchOfAccord(subdominantPitches, root.getPitch(), isUp), root.getDuration());
                    break;
                case D:
                    root = new Note(getClosestPitchOfAccord(dominantPitches, root.getPitch(), isUp), root.getDuration());
                    break;
                case VI:
                    root = new Note(getClosestPitchOfAccord(sixPitches, root.getPitch(), isUp), root.getDuration());
                    break;
                case D7:
                    root = new Note(getClosestPitchOfAccord(dominant7Pitches, root.getPitch(), isUp), root.getDuration());
                    break;
            }
            accordsNum++;
        } while (accordsNum < 100);
       //  accompaniment.setChannel(0);
//        s.add(accompaniment);
//        solo.setChannel(1);
//        s.add(solo);
        s.add(part);
        Write.midi(s, "ChordSequence.mid");
    }

    public int getClosestPitchOfAccord(Set<Integer> accordPitches, int pitch, boolean isUp) {
        int resultPitch = 0;
        int distance = Integer.MAX_VALUE;
        for (int elem : accordPitches) {
            if (Math.abs(pitch - elem) < distance && pitch < elem) {
                if (isUp) {
                    distance = Math.abs(pitch - elem);
                    resultPitch = elem;
                }
            }
            if (Math.abs(pitch - elem) < distance && pitch > elem) {
                if (!isUp) {
                    distance = Math.abs(pitch - elem);
                    resultPitch = elem;
                }
            }
        }
        return resultPitch;
    }

    public static CPhrase buildAccord(Note rootNote, ChordType type) {
        switch (type) {
            case T:
                return buildAccord(rootNote, TONIC_ROOT);
            case IV:
            case S:
                return buildAccord(rootNote, SUBDOMINANT_ROOT);
            case D:
                return buildAccord(rootNote, DOMINANT_ROOT);
            case VI:
                return buildAccord(rootNote, SIX_ROOT);
            case D7:
                return build7Accord(rootNote, type);
            case II:
                return buildAccord(rootNote, SECOND_ROOT);
            default:
                return null;
        }
    }


    public static CPhrase buildAccord(Note rootNote, int type) {
        int distance = Math.abs(rootNote.getPitch() - type);
        switch (distance % 12) {
            case 0:
                return build53(rootNote);
            // minor 3
            case 3:
                // major 3
            case 4:
                // minor 6
            case 8:
                // major 6
            case 9:
                return build6(rootNote);
            case 4 + 3:
            case 5:
                return build64(rootNote);
            default:
                // TODO
                return build53(new Note(Pitches.c4, RhythmValues.QUARTER_NOTE));
        }
    }

    public static CPhrase build7Accord(Note rootNote, ChordType type) {
        switch (type) {
            case T:
                return build7Accord(rootNote, TONIC_ROOT);
            case S:
                return build7Accord(rootNote, SUBDOMINANT_ROOT);
            case D:
                return build7Accord(rootNote, DOMINANT_ROOT);
            case D7:
                return build7Accord(rootNote, DOMINANT_ROOT);
            case VI:
                return build7Accord(rootNote, SIX_ROOT);
            default:
                return null;
        }
    }


    public static CPhrase build7Accord(Note rootNote, int type) {
        int distance = Math.abs(rootNote.getPitch() - type);
        switch (distance % 12) {
            case 0:
                return build7(rootNote);
            case 4:
            case 8:
                return build65(rootNote);
            case 4 + 3:
            case 5:
                return build43(rootNote);
            case 2:
            case 10:
                return build2(rootNote);
            default:
                return null;
        }
    }

    private static CPhrase build53(Note rootNote) {
        int[] pitches = new int[3];
        pitches[0] = rootNote.getPitch();
        pitches[1] = rootNote.getPitch() + 4;
        pitches[2] = rootNote.getPitch() + 4 + 3;
        CPhrase result = new CPhrase();
        result.addChord(pitches, rootNote.getRhythmValue());
        System.out.println(53);
        return result;
    }

    private static CPhrase build6(Note rootNote) {
        int[] pitches = new int[3];
        pitches[0] = rootNote.getPitch();
        pitches[1] = rootNote.getPitch() + 3;
        pitches[2] = rootNote.getPitch() + 3 + 5;
        CPhrase result = new CPhrase();
        result.addChord(pitches, rootNote.getRhythmValue());
        System.out.println(6);
        return result;
    }

    private static CPhrase build64(Note rootNote) {
        int[] pitches = new int[3];
        pitches[0] = rootNote.getPitch();
        pitches[1] = rootNote.getPitch() + 5;
        pitches[2] = rootNote.getPitch() + 5 + 4;
        CPhrase result = new CPhrase();
        result.addChord(pitches, rootNote.getRhythmValue());
        System.out.println(64);
        return result;
    }

    private static CPhrase build7(Note rootNote) {
        int[] pitches = new int[4];
        pitches[0] = rootNote.getPitch();
        pitches[1] = rootNote.getPitch() + 4;
        pitches[2] = rootNote.getPitch() + 4 + 3;
        pitches[3] = rootNote.getPitch() + 4 + 3 + 3;
        CPhrase result = new CPhrase();
        result.addChord(pitches, rootNote.getRhythmValue());
        System.out.println(7);
        return result;
    }

    private static CPhrase build65(Note rootNote) {
        int[] pitches = new int[4];
        pitches[0] = rootNote.getPitch();
        pitches[1] = rootNote.getPitch() + 3;
        pitches[2] = rootNote.getPitch() + 3 + 3;
        pitches[3] = rootNote.getPitch() + 3 + 3 + 2;

        CPhrase result = new CPhrase();
        result.addChord(pitches, rootNote.getRhythmValue());
        System.out.println(65);
        return result;
    }

    private static CPhrase build43(Note rootNote) {
        int[] pitches = new int[4];
        pitches[0] = rootNote.getPitch();
        pitches[1] = rootNote.getPitch() + 3;
        pitches[2] = rootNote.getPitch() + 3 + 2;
        pitches[3] = rootNote.getPitch() + 3 + 2 + 4;

        CPhrase result = new CPhrase();
        result.addChord(pitches, rootNote.getRhythmValue());
        System.out.println(43);
        return result;
    }

    private static CPhrase buildRest(double rhytmValue) {
        CPhrase result = new CPhrase();
        Phrase phrase = new Phrase();
        phrase.addRest(new Rest(rhytmValue));
        result.addPhrase(phrase);
        System.out.println("rest");
        return result;
    }


    private static CPhrase build2(Note rootNote) {
        int[] pitches = new int[4];
        pitches[0] = rootNote.getPitch();
        pitches[1] = rootNote.getPitch() + 2;
        pitches[2] = rootNote.getPitch() + 2 + 4;
        pitches[3] = rootNote.getPitch() + 2 + 4 + 3;

        CPhrase result = new CPhrase();
        result.addChord(pitches, rootNote.getRhythmValue());
        System.out.println(2);
        return result;
    }

    public static CPhrase getAccompaniment(CPhrase accord) {
        Vector phraseList = accord.getPhraseList();
        int[] pitches = new int[phraseList.size() - 1];
        for (int i = 0; i < phraseList.size() - 1; i++) {
            pitches[i] = ((Phrase) phraseList.get(i)).getNote(0).getPitch();
        }
        CPhrase result = new CPhrase();
        result.addChord(pitches, Durations.Q);
        return result;
    }

    public static CPhrase getTheHighestVoice(CPhrase accord) {
        CPhrase result = new CPhrase();
        result.addPhrase(((Phrase) accord.getPhraseList().get(0)));
        return result;

    }

    public static void addPitches(Set<Integer> set, int root) {
        while (root < 128) {
            set.add(root);
            root += 12;
        }
    }

}
