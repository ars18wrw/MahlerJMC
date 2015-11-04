//package markov;
//
//import jm.music.data.Part;
//import jm.music.data.Phrase;
//
//public class PartEx3 {
//    private static final String PRE_TEXT =
//            "========================================================\n";
//    private Part part;
//    private double[][][][] probabilities;
//    private int[] times;
//
//    PartEx3(Part part) {
//        this.part = part.copy();
//        probabilities = new double[50][50][50][50]; // from 40 to 90
//        for (int i = 0; i < part.getSize(); i++) {
//            Phrase phrase = part.getPhrase(i);
//            if (phrase.getSize() < 3) {
//                continue;
//            }
//            int lastlastlastIndex = 0;
//            while (phrase.getNote(lastlastlastIndex).getPitch() < 0) {
//                lastlastlastIndex++;
//            }
//            int lastlastIndex = lastlastlastIndex;
//            while (phrase.getNote(lastlastIndex).getPitch() < 0) {
//                lastlastIndex++;
//            }
//            int lastIndex = lastlastIndex;
//            while (phrase.getNote(lastIndex).getPitch() < 0) {
//                lastIndex++;
//            }
//
//
//            for (int j = lastlastIndex + 1; j < phrase.getSize(); j++) {
//                // TODO getNote(int) is very slow
//                if (phrase.getNote(j).getPitch() < 40 || phrase.getNote(j).getPitch() >= 90) {
//                    continue;
//                }
//                probabilities[phrase.getNote(lastlastlastIndex).getPitch()-40][phrase.getNote(lastlastIndex).getPitch()-40][phrase.getNote(lastIndex).getPitch()-40][phrase.getNote(j).getPitch()-40]++;
//                lastlastlastIndex = lastlastIndex;
//                lastlastIndex = lastIndex;
//                lastIndex = j;
//            }
//        }
//        times = new int[50*50*50];
//        for (int i = 0 ; i < 50; i++) {
//            for (int j = 0; j < 50; j++) {
//                for (int k = 0; k < 50; k++) {
//                    for (int l = 0; l < 50; l++) {
//                        times[50*50*i+50*j+k] += probabilities[i][j][k][l];
//                    }
//                    for (int l = 0; l < 50; l++) {
//                        probabilities[i][j][k][l] /= times[50*50*i+50*j+k];
//                    }
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
//    public int[] getTimes() {
//        return times;
//    }
//
//    public double[][][][] getProbabilities() {
//        return probabilities;
//    }
//
//    public Part getPart() {
//        return part;
//    }
//}
