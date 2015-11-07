package grammar;

import jm.constants.Pitches;
import jm.music.data.Part;
import jm.music.data.Score;
import jm.util.Read;
import jm.util.Write;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Grammar {
    private static boolean DEBUG = true;

    private static final String FOLDER = "originals\\";
    private static final String NAME = "fantaisie";
    private static final String EXT = ".mid";

    private final String className;

    private Score s = new Score();

    public Grammar() {
        className = getClass().getName().toString();
    }


    public static void main(String[] args) {
        new Grammar().process();
    }

    public void process() {
        /* Read midi */
        Read.midi(s, FOLDER + NAME + EXT);
        s.setTitle(NAME);
        /* Create folders */
        new File("music/" + className).mkdir();
        new File("music/" + className + "/" + NAME).mkdir();
        Write.midi(s, "music/" + className + "/" + NAME + "/original.mid");
        // for each part let's find rules
        for (int ii = 0; ii < s.getSize(); ii++) {
            // TODO Change List with Set because getting in List is very expensive operation
            Map<Long, Set<Long>> preRules = new HashMap<>();
            Part part = s.getPart(ii);
            // for each phrase
            for (int jj = 0; jj < part.getSize(); jj++) {
                int[] pitchesWithRests = part.getPhrase(jj).getPitchArray();
                long[] pitches = getNormalPitchArray(pitchesWithRests);
                long value = 0;
                /** Rules are like this: a->(bcd, e),
                 * while in a score this fact
                 * is rendered as ...bcdae...
                 * In this example:
                 * a - the current pitch
                 * bcd - previous pithes
                 * e - the result pitch*/
                for (int k = 0; k < pitches.length - 1; k++) {
                    if (k == 0) {
                        value = pitches[k + 1];
                    } else {
                        // Subtract the previous result pitch
                        value -= pitches[k];
                        // Drop the oldest pitch of "previous pitches"
                        value = (value % (128 * 128 * 128)) * 128;
                        // Add the first letter
                        value += 128 * pitches[k - 1];
                        // Add the result pitch
                        value += pitches[k + 1];
                    }
                    if (DEBUG) {
                        Set<Long> valueSet = new HashSet<>();
                        valueSet.add(value);
                        System.out.print(getThirdLetters(valueSet)[0]);
                        System.out.print(" ");
                        System.out.print(getSecondLetters(valueSet)[0]);
                        System.out.print(" ");
                        System.out.print(getFirstLetters(valueSet)[0]);
                        System.out.print(" ");
                        System.out.println(getResultLetters(valueSet)[0]);

                    }


                    Set<Long> list;
                    // first rule or not
                    if (!preRules.containsKey(pitches[k])) {
                        list = new HashSet<>();
                    } else {
                        list = preRules.get(pitches[k]);
                    }
                    list.add(value);
                    preRules.put(pitches[k], list);
                }
            }

            Map<Long, Long> rules = new HashMap<>();


            Map<Long, Set<Long>> nextStepRules = new HashMap<>();
            // for each pitch let's build One Letter rules
            Set<Long> prefixes;
            for (long analyzedPitch : preRules.keySet()) {
                prefixes = preRules.get(analyzedPitch); // value, rules like XYZ -> analyzedPitch
                boolean isGood = true;
                Long[] firstLetters = getFirstLetters(prefixes);
                Long[] secondLetters = getSecondLetters(prefixes);
                Long[] thirdLetters = getThirdLetters(prefixes);
                Long[] resultLetters = getResultLetters(prefixes);
                for (int i = 0; i < prefixes.size() - 1; i++) {
                    // there is no one letter rule
                    if (resultLetters[i] != resultLetters[i + 1]) {
                        for (int j = 0; j < prefixes.size(); j++) {
                            long key = analyzedPitch + firstLetters[j] * 128;
                            long value = secondLetters[j] * 128 * 128 + thirdLetters[j] * 128 * 128 * 128 + resultLetters[j];
                            Set<Long> list;
                            // first rule or not
                            if (!nextStepRules.containsKey(key)) {
                                list = new HashSet<>();
                            } else {
                                list = nextStepRules.get(key);
                            }
                            list.add(value);
                            nextStepRules.put(key, list);
                            isGood = false;
                        }
                    }
                }
                // there is one letter rule
                if (isGood) {
                    rules.put(analyzedPitch, resultLetters[0]);
                }
                isGood = true;
                break;
            }

            preRules = nextStepRules;
            nextStepRules = new HashMap<>();
            // for each pitch let's build Two Letter rules
            for (long analyzedPitch : preRules.keySet()) {
                prefixes = preRules.get(analyzedPitch); // value, rules like XYZ -> analyzedPitch
                boolean isGood = true;
                Long[] firstLetters = getFirstLetters(prefixes);
                Long[] secondLetters = getSecondLetters(prefixes);
                Long[] thirdLetters = getThirdLetters(prefixes);
                Long[] resultLetters = getResultLetters(prefixes);
                for (int i = 0; i < prefixes.size() - 1; i++) {
                    // there is no two letter rule
                    if (resultLetters[i] != resultLetters[i + 1]) {
                        for (int j = 0; j < prefixes.size(); j++) {
                            long key = analyzedPitch + secondLetters[j] * 128 * 128;
                            long value = thirdLetters[j] * 128 * 128 * 128;
                            Set<Long> list;
                            // first rule or not
                            if (!nextStepRules.containsKey(key)) {
                                list = new HashSet<>();
                            } else {
                                list = nextStepRules.get(key);
                            }
                            list.add(value);
                            nextStepRules.put(key, list);
                            isGood = false;
                            break;
                        }
                    }
                }
                // there is one lette rule
                if (isGood) {
                    rules.put(analyzedPitch, resultLetters[0]);
                }
                isGood = true;
            }


            preRules = nextStepRules;
            nextStepRules = new HashMap<>();
            // for each pitch let's build Three Letter rules
            for (long analyzedPitch : preRules.keySet()) {
                prefixes = preRules.get(analyzedPitch); // value, rules like XYZ -> analyzedPitch
                boolean isGood = true;
                Long[] firstLetters = getFirstLetters(prefixes);
                Long[] secondLetters = getSecondLetters(prefixes);
                Long[] thirdLetters = getThirdLetters(prefixes);
                Long[] resultLetters = getResultLetters(prefixes);
                for (int i = 0; i < prefixes.size() - 1; i++) {
                    // there is no two letter rule
                    if (resultLetters[i] != resultLetters[i + 1]) {
                        break;
//                        for (int j = 0; j < prefixes.size(); j++) {
//                            int key = analyzedPitch +secondLetters[j]*128*128;
//                            int value = thirdLetters[j]*128*128*128;
//                            Set<Integer> list;
//                            // first rule or not
//                            if (!nextStepRules.containsKey(key)) {
//                                list = new HashSet<>();
//                            } else {
//                                list = nextStepRules.get(key);
//                            }
//                            list.add(value);
//                            nextStepRules.put(key, list);
//                            isGood = false;
//                            break;
//                        }
                    }
                }
                // there is one lette rule
                if (isGood) {
                    rules.put(analyzedPitch, resultLetters[0]);
                }
                isGood = true;
            }


            for (long key : rules.keySet()) {
                System.out.println(key + "->" + rules.get(key));
            }


        }
    }

    protected Long[] getFirstLetters(Set<Long> set) {
        Long[] resultArray = new Long[set.size()];
        resultArray = set.toArray(resultArray);
        for (int i = 0; i < resultArray.length; i++) {
            resultArray[i] = (resultArray[i] / 128) % 128;
        }
        return resultArray;
    }

    protected Long[] getSecondLetters(Set<Long> set) {
        Long[] resultArray = new Long[set.size()];
        resultArray = set.toArray(resultArray);
        for (int i = 0; i < resultArray.length; i++) {
            resultArray[i] = (resultArray[i] / (128 * 128)) % 128;
        }
        return resultArray;

    }

    protected Long[] getThirdLetters(Set<Long> set) {
        Long[] resultArray = new Long[set.size()];
        resultArray = set.toArray(resultArray);
        for (int i = 0; i < resultArray.length; i++) {
            resultArray[i] /= (128 * 128 * 128);
        }
        return resultArray;
    }

    protected Long[] getFourthLetters(Set<Long> set) {
        Long[] resultArray = new Long[set.size()];
        resultArray = set.toArray(resultArray);
        for (int i = 0; i < resultArray.length; i++) {
            resultArray[i] /= (128 * 128 * 128);
        }
        return resultArray;
    }

    protected Long[] getFifthLetters(Set<Long> set) {
        Long[] resultArray = new Long[set.size()];
        resultArray = set.toArray(resultArray);
        for (int i = 0; i < resultArray.length; i++) {
            resultArray[i] /= ((long) 128 * 128 * 128 * 128 * 128);
        }
        return resultArray;
    }


    protected Long[] getResultLetters(Set<Long> set) {
        Long[] resultArray = new Long[set.size()];
        resultArray = set.toArray(resultArray);
        for (int i = 0; i < resultArray.length; i++) {
            resultArray[i] %= 128;
        }
        return resultArray;
    }

    protected static long[] getNormalPitchArray(int[] pitches) {
        int size = pitches.length;
        for (int i = 0; i < pitches.length; i++) {
            if (Pitches.REST == pitches[i]) {
                size--;
            }
        }
        long[] result = new long[size];
        int j = 0;
        for (int i = 0; i < pitches.length; i++) {
            if (Pitches.REST != pitches[i]) {
                result[j] = pitches[i];
                j++;
            }
        }
        return result;
    }

}
