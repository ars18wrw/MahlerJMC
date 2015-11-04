//package markov;
//
//import jm.music.data.Part;
//import jm.music.data.Phrase;
//
//public class PartEx2 {
//    private static final String PRE_TEXT =
//            "========================================================\n";
//    private Part part;
//    private double[][][] probabilities;
//    private int[] times;
//
//    PartEx2(Part part) {
//        this.part = part.copy();
//        probabilities = new double[127][127][127];
//        for (int i = 0; i < part.getSize(); i++) {
//            Phrase phrase = part.getPhrase(i);
//            if (phrase.getSize() < 2) {
//                continue;
//            }
//            int lastlastIndex = 0;
//            while (phrase.getNote(lastlastIndex).getPitch() < 0) {
//                lastlastIndex++;
//            }
//            int lastIndex = lastlastIndex;
//            while (phrase.getNote(lastIndex).getPitch() < 0) {
//                lastIndex++;
//            }
//
//            for (int j = lastlastIndex + 1; j < phrase.getSize(); j++) {
//                // TODO getNote(int) is very slow
//                if (phrase.getNote(j).getPitch() < 0) {
//                    continue;
//                }
//                probabilities[phrase.getNote(lastlastIndex).getPitch()][phrase.getNote(lastIndex).getPitch()][phrase.getNote(j).getPitch()]++;
//                lastlastIndex = lastIndex;
//                lastIndex = j;
//            }
//        }
//        times = new int[127*127];
//        for (int i = 0 ; i < 127; i++) {
//            for (int j = 0; j < 127; j++) {
//                for (int k = 0; k < 127; k++) {
//                    times[127*i+j] += probabilities[i][j][k];
//                }
//                for (int k = 0; k < 127; k++) {
//                    probabilities[i][j][k] /= times[127*i+j];
//                }
//            }
//        }
////        for (int i = 0; i < 127; i++) {
////            for (int j = 0; j < 127; j++) {
////                times[i] += probabilities[i][j];
////            }
////            for (int j = 0; j < 127; j++) {
////                if (0 != times[i]) {
////                    probabilities[i][j] /= times[i];
////                }
////            }
////        }
//
//    }
//
//    public void printProbabilities() {
//        System.out.println(PRE_TEXT);
//        StringBuffer buffer;
//        for (int i = 0; i < 127; i++) {
//            for (int j = 0; j < 127; j++) {
//                buffer = new StringBuffer();
//                for (int ii = 0; ii < 127; ii++){
//                    buffer.append(probabilities[i][j][ii] + " ");
//                }
//                System.out.println(buffer);
//            }
//        }
//    }
//
//    public int[] getTimes() {
//        return times;
//    }
//
//    public double[][][] getProbabilities() {
//        return probabilities;
//    }
//
//    public Part getPart() {
//        return part;
//    }
//}
