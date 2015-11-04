////package markov;
////
////import jm.JMC;
////import jm.constants.Durations;
////import jm.music.data.Note;
////import jm.music.data.Part;
////import jm.music.data.Phrase;
////import jm.music.data.Score;
////import jm.util.Read;
////import jm.util.Write;
////
////import java.awt.*;
////
////public final class Markov1 extends Frame implements JMC {
////
////    private Score s = new Score();
////
////
////    public static void main(String[] args) {
////        new Markov1();
////    }
////
////    public Markov1() {
////        /* Open and read MIDI file */
////        FileDialog fd;
////        Frame f = new Frame();
////        fd = new FileDialog(f,
////                "Open MIDI file or choose cancel to"
////                        + " finish.",
////                FileDialog.LOAD);
////        fd.show();
////        if (fd.getFile() == null) {
////            return;
////        }
////        Read.midi(s, fd.getDirectory() + fd.getFile());
////        s.setTitle(fd.getFile());
////
////        /* Fill probability matrix*/
////        Score scoreToWrite = null;
////        PartEx1[] partExes = new PartEx1[s.getSize()];
////        for (int i = 0; i < s.getSize(); i++) {
////            partExes[i] = new PartEx1(s.getPart(i));
////            partExes[i].printProbabilities();
////        }
////
////        /* Let's build music */
////        for (int ii = 0; ii < partExes.length; ii++) {
////            Score score = new Score();
////            Part part = new Part();
////            Phrase phrase = new Phrase();
////            int[] times = partExes[ii].getTimes();
////            double[][] probabilities = partExes[ii].getProbabilities();
////
////            int lastIndex;
////            do {
////                lastIndex = (int) (Math.random() * 127);
////            } while (times[lastIndex] < 5);
////            phrase.add(new Note(lastIndex, Durations.Q));
////
////            for (int i = 0; i < 50; i++) {
////                double randNum = Math.random();
//////                if (1 == times[lastIndex] || 0 == times[lastIndex]) {
//////                    do {
//////                        lastIndex = (int) (Math.random() * 127);
//////                    } while (0 != times[lastIndex] && 1 !=times[lastIndex]);
//////                    phrase.add(new Note(lastIndex,Durations.Q));
//////                    continue;
//////                }
////                // TODO This is so slow; get rid of such iteration
////                for (int j = 0; j < 127; j++) {
////                    // Get rid of playing the same pitch again and again
////                    randNum -= probabilities[lastIndex][j];
////                    if (randNum <= 0) {
//////                        while (times[j] <= 1) {
//////                            j++;
//////                            if (j == 127) {
//////                                j = 0;
//////                            }
//////                        }
////                        phrase.add(new Note(j, Durations.Q));
////                        lastIndex = j;
////                        break;
////                    }
////                }
////            }
////            part.add(phrase);
////            score.add(part);
////            score.setTempo(200);
////            Write.midi(score, String.format("compose" + fd.getFile() + "%d.mid", ii));
////        }
////    }
////    class PartEx1 {
////        private static final String PRE_TEXT =
////                "========================================================\n";
////        private Part part;
////        private double[][] probabilities;
////        private int[] times;
////
////        PartEx1(Part part) {
////            this.part = part.copy();
////            probabilities = new double[127][127];
////            for (int i = 0; i < part.getSize(); i++) {
////                Phrase phrase = part.getPhrase(i);
////                if (phrase.getSize() < 1) {
////                    continue;
////                }
////                int last_index = 0;
////                while (phrase.getNote(last_index).getPitch() < 0) {
////                    last_index++;
////                }
////                for (int j = last_index + 1; j < phrase.getSize(); j++) {
////                    // TODO getNote(int) is very slow
////                    if (phrase.getNote(j).getPitch() < 0) {
////                        continue;
////                    }
////                    probabilities[phrase.getNote(last_index).getPitch()][phrase.getNote(j).getPitch()]++;
////                    last_index = j;
////                }
////            }
////            times = new int[127];
////            for (int i = 0; i < 127; i++) {
////                for (int j = 0; j < 127; j++) {
////                    times[i] += probabilities[i][j];
////                }
////                for (int j = 0; j < 127; j++) {
////                    if (0 != times[i]) {
////                        probabilities[i][j] /= times[i];
////                    }
////                }
////
////            }
////
////        }
////
////        public void printProbabilities() {
////            System.out.println(PRE_TEXT);
////            StringBuffer buffer;
////            for (int i = 0; i < 127; i++) {
////                buffer = new StringBuffer();
////                for (int j = 0; j < 127; j++) {
////                    buffer.append(probabilities[i][j] + " ");
////                }
////                System.out.println(buffer);
////            }
////        }
////
////        public int[] getTimes() {
////            return times;
////        }
////
////        public double[][] getProbabilities() {
////            return probabilities;
////        }
////
////        public Part getPart() {
////            return part;
////        }
////    }
////}
//
//
//
//package markov;
//
//import jm.JMC;
//import jm.constants.Durations;
//import jm.music.data.Note;
//import jm.music.data.Part;
//import jm.music.data.Phrase;
//import jm.music.data.Score;
//import jm.util.Read;
//import jm.util.Write;
//
//import java.io.File;
//
///* This class differs from Markov1 and Markov2:
//* Since the amount of data is limited,
//* one should use not all 127 pitches,
//* but a reduced number, for instance, 50. */
//public final class Markov3 implements JMC {
//    private static final int COMPOSE_PIECE_SIZE = 150;
//
//    private static final String FOLDER = "originals\\";
//    private static final String NAME = "fantaisie";
//    private static final String EXT = ".mid";
//
//    private final String className;
//
//    private Score s = new Score();
//
//    public Markov3() {
//        /* Prepare constant for creating folder */
//        className = getClass().getName().toString();
//    }
//
//    public static void main(String[] args) {
//        new Markov3().process();
//    }
//
//    public void process() {
//        /* Read midi */
//        Read.midi(s, FOLDER + NAME + EXT);
//        s.setTitle(NAME);
//        /* Create folders */
//        new File("music/" + className).mkdir();
//        new File("music/" + className + "/" + NAME).mkdir();
//        /* Fill probability matrix*/
//        Score scoreToWrite = null;
//        PartEx3[] partExes = new PartEx3[s.getSize()];
//        for (int i = 0; i < s.getSize(); i++) {
//            partExes[i] = new PartEx3(s.getPart(i));
//            //partExes[i].printProbabilities();
//            scoreToWrite = new Score();
//            scoreToWrite.add(s.getPart(i));
//            scoreToWrite.setTempo(180);
//            Write.midi(scoreToWrite, String.format("music/" + className + "/" + NAME + "/ori_voice" + "%d.mid", i));
//        }
//        /* Let's build music */
//        Score scoreTogether = new Score();
//        for (int ii = 0; ii < partExes.length; ii++) {
//            Score score = new Score();
//            Part part = new Part();
//            Phrase phrase = new Phrase();
//            int[] times = partExes[ii].getTimes();
//            double[][][][] probabilities = partExes[ii].getProbabilities();
//            int[] pitches = partExes[ii].getPart().getPhrase(0).getPitchArray();
//            int jj = 0;
//            while (REST == pitches[jj]) {
//                jj++;
//            }
//            int lastlastlastIndex = pitches[jj];
//            phrase.add(new Note(lastlastlastIndex, Durations.Q));
//            jj++;
//            while (REST == pitches[jj]) {
//                jj++;
//            }
//            int lastlastIndex = pitches[jj];
//            phrase.add(new Note(lastlastIndex, Durations.Q));
//            jj++;
//            while (REST == pitches[jj]) {
//                jj++;
//            }
//            int lastIndex = pitches[jj];
//            phrase.add(new Note(lastIndex, Durations.Q));
//            int i = 0;
//            while (COMPOSE_PIECE_SIZE >= i) {
//                double randNum = Math.random();
//                // TODO This is so slow; get rid of such iteration
//                for (int j = 50; j < 100; j++) {
//                    // Get rid of playing the same pitch again and again
//                    randNum -= probabilities[lastlastlastIndex - 50][lastlastIndex - 50][lastIndex - 50][j - 50];
//                    if (randNum <= 0.00001) {
//                        phrase.add(new Note(j, Durations.Q));
//                        lastlastlastIndex = lastlastIndex;
//                        lastlastIndex = lastIndex;
//                        lastIndex = j;
//                        i++;
//                        break;
//                    }
//                }
//            }
//            // do not forget about
//            part.add(phrase);
//            scoreTogether.add(part);
//            score.add(part);
//            score.setTempo(180);
//            Write.midi(score, String.format("music/" + className + "/" + NAME + "/com_voice" + "%d.mid", ii));
//        }
//        scoreTogether.setTempo(180);
//        Write.midi(scoreTogether, "music/" + className + "/" + NAME + "/tutti.mid");
//        return;
//    }
//
//    class PartEx3 {
//        private Part part;
//        private double[][][][] probabilities;
//        private int[] times;
//
//        PartEx3(Part part) {
//            this.part = part.copy();
//            probabilities = new double[50][50][50][50]; // from 50 to 100
//            int minPitch = 127;
//            int maxPitch = 0;
//            for (int i = 0; i < part.getSize(); i++) {
//                Phrase phrase = part.getPhrase(i);
//                if (phrase.getSize() < 3) {
//                    continue;
//                }
//                int lastlastlastIndex = 0;
//                while (lastlastlastIndex < phrase.getSize() && (phrase.getNote(lastlastlastIndex).getPitch() < 50 || phrase.getNote(lastlastlastIndex).getPitch() >= 100)) {
//                    if (phrase.getNote(lastlastlastIndex).getPitch() > maxPitch) {
//                        maxPitch = phrase.getNote(lastlastlastIndex).getPitch();
//                    } else if (phrase.getNote(lastlastlastIndex).getPitch() > 0 && phrase.getNote(lastlastlastIndex).getPitch() < minPitch) {
//                        minPitch = phrase.getNote(lastlastlastIndex).getPitch();
//                    }
//                    lastlastlastIndex++;
//                }
//                if (phrase.getNote(lastlastlastIndex).getPitch() > maxPitch) {
//                    maxPitch = phrase.getNote(lastlastlastIndex).getPitch();
//                } else if (phrase.getNote(lastlastlastIndex).getPitch() > 0 && phrase.getNote(lastlastlastIndex).getPitch() < minPitch) {
//                    minPitch = phrase.getNote(lastlastlastIndex).getPitch();
//                }
//                int lastlastIndex = lastlastlastIndex + 1;
//                while (lastlastIndex < phrase.getSize() && (phrase.getNote(lastlastIndex).getPitch() < 50 || phrase.getNote(lastlastIndex).getPitch() >= 100)) {
//                    if (phrase.getNote(lastlastIndex).getPitch() > maxPitch) {
//                        maxPitch = phrase.getNote(lastlastIndex).getPitch();
//                    } else if (phrase.getNote(lastlastIndex).getPitch() > 0 && phrase.getNote(lastlastIndex).getPitch() < minPitch) {
//                        minPitch = phrase.getNote(lastlastIndex).getPitch();
//                    }
//                    lastlastIndex++;
//                }
//                if (phrase.getNote(lastlastIndex).getPitch() > maxPitch) {
//                    maxPitch = phrase.getNote(lastlastIndex).getPitch();
//                } else if (phrase.getNote(lastlastIndex).getPitch() > 0 && phrase.getNote(lastlastIndex).getPitch() < minPitch) {
//                    minPitch = phrase.getNote(lastlastIndex).getPitch();
//                }
//                int lastIndex = lastlastIndex + 1;
//                while (lastIndex < phrase.getSize() && (phrase.getNote(lastIndex).getPitch() < 50 || phrase.getNote(lastIndex).getPitch() >= 100)) {
//                    if (phrase.getNote(lastIndex).getPitch() > maxPitch) {
//                        maxPitch = phrase.getNote(lastIndex).getPitch();
//                    } else if (phrase.getNote(lastIndex).getPitch() > 0 && phrase.getNote(lastIndex).getPitch() < minPitch) {
//                        minPitch = phrase.getNote(lastIndex).getPitch();
//                    }
//                    lastIndex++;
//                }
//                if (lastIndex >= phrase.getSize()) {
//                    continue;
//                }
//
//                if (phrase.getNote(lastIndex).getPitch() > maxPitch) {
//                    maxPitch = phrase.getNote(lastIndex).getPitch();
//                } else if (phrase.getNote(lastIndex).getPitch() > 0 && phrase.getNote(lastIndex).getPitch() < minPitch) {
//                    minPitch = phrase.getNote(lastIndex).getPitch();
//                }
//
//                for (int j = lastIndex + 1; j < phrase.getSize(); j++) {
//                    // TODO getNote(int) is very slow
//                    if (phrase.getNote(j).getPitch() < 50 || phrase.getNote(j).getPitch() >= 100) {
//                        if (phrase.getNote(j).getPitch() > maxPitch) {
//                            maxPitch = phrase.getNote(j).getPitch();
//                        } else if (phrase.getNote(j).getPitch() > 0 && phrase.getNote(j).getPitch() < minPitch) {
//                            minPitch = phrase.getNote(j).getPitch();
//                        }
//                        continue;
//                    }
//                    if (phrase.getNote(j).getPitch() > maxPitch) {
//                        maxPitch = phrase.getNote(j).getPitch();
//                    } else if (phrase.getNote(j).getPitch() > 0 && phrase.getNote(j).getPitch() < minPitch) {
//                        minPitch = phrase.getNote(j).getPitch();
//                    }
//                    System.out.println(phrase.getNote(lastlastlastIndex).getPitch() + " " + phrase.getNote(lastlastIndex).getPitch() + " " + phrase.getNote(lastIndex).getPitch() + " " + phrase.getNote(j).getPitch());
//                    probabilities[phrase.getNote(lastlastlastIndex).getPitch() - 50][phrase.getNote(lastlastIndex).getPitch() - 50][phrase.getNote(lastIndex).getPitch() - 50][phrase.getNote(j).getPitch() - 50]++;
//                    lastlastlastIndex = lastlastIndex;
//                    lastlastIndex = lastIndex;
//                    lastIndex = j;
//                }
//            }
//            times = new int[50 * 50 * 50];
//            for (int i = 0; i < 50; i++) {
//                for (int j = 0; j < 50; j++) {
//                    for (int k = 0; k < 50; k++) {
//                        for (int l = 0; l < 50; l++) {
//                            times[50 * 50 * i + 50 * j + k] += probabilities[i][j][k][l];
//                        }
//                        for (int l = 0; l < 50; l++) {
//                            if (0 != times[50 * 50 * i + 50 * j + k]) {
//                                probabilities[i][j][k][l] /= times[50 * 50 * i + 50 * j + k];
//                            }
//                        }
//                    }
//                }
//            }
//            System.out.println(minPitch + " " + maxPitch);
//        }
//
//        public int[] getTimes() {
//            return times;
//        }
//
//        public double[][][][] getProbabilities() {
//            return probabilities;
//        }
//
//        public Part getPart() {
//            return part;
//        }
//    }
//}
