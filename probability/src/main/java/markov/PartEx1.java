//package markov;
//
//import jm.music.data.Part;
//import jm.music.data.Phrase;
//
//public class PartEx1 {
//    private static final String PRE_TEXT =
//            "========================================================\n";
//    private Part part;
//    private double[][] probabilities;
//    private int[] times;
//
//    PartEx1(Part part) {
//        this.part = part.copy();
//        probabilities = new double[127][127];
//        for (int i = 0; i < part.getSize(); i++) {
//            Phrase phrase = part.getPhrase(i);
//            if (phrase.getSize() < 1) {
//                continue;
//            }
//            int last_index = 0;
//            while (phrase.getNote(last_index).getPitch() < 0) {
//                last_index++;
//            }
//            for (int j = last_index + 1; j < phrase.getSize(); j++) {
//                // TODO getNote(int) is very slow
//                if (phrase.getNote(j).getPitch() < 0) {
//                    continue;
//                }
//                probabilities[phrase.getNote(last_index).getPitch()][phrase.getNote(j).getPitch()]++;
//                last_index = j;
//            }
//        }
//        times = new int[127];
//        for (int i = 0; i < 127; i++) {
//            for (int j = 0; j < 127; j++) {
//                times[i] += probabilities[i][j];
//            }
//            for (int j = 0; j < 127; j++) {
//                if (0 != times[i]) {
//                    probabilities[i][j] /= times[i];
//                }
//            }
//
//        }
//
//    }
//
//    public void printProbabilities() {
//        System.out.println(PRE_TEXT);
//        StringBuffer buffer;
//        for (int i = 0; i < 127; i++) {
//            buffer = new StringBuffer();
//            for (int j = 0; j < 127; j++) {
//                buffer.append(probabilities[i][j] + " ");
//            }
//            System.out.println(buffer);
//        }
//    }
//
//    public int[] getTimes() {
//        return times;
//    }
//
//    public double[][] getProbabilities() {
//        return probabilities;
//    }
//
//    public Part getPart() {
//        return part;
//    }
//}
