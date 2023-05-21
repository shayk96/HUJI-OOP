package ascii_art;

import java.util.HashSet;
import java.util.Set;

/**
 * contains static functions that solve algorithm questions
 */
public class Algorithms {

    private static final Set<String> wordsInMorse = new HashSet<>();
    private static final String[] charsInMorse = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....",
            "..", ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-",
            ".--", "-..-", "-.--", "--.."};
    private static final int BASE_ASCII = 97;

    /**
     * Finds the duplicate number in an array. The algorithm treats the array as a linked list that contains a
     * circle. The function iterates through the array twice. The first time it finds if there is a duplicate.
     * The second time it finds the number that exits more than one time.
     *
     * @param numList array holding numbers
     * @return the duplicate number
     */
    public static int findDuplicate(int[] numList) {
        int slowerJump = numList[numList[0]];
        int fasterJump = numList[numList[numList[0]]];
        while (slowerJump != fasterJump) {
            slowerJump = numList[slowerJump];
            fasterJump = numList[numList[fasterJump]];
        }
        slowerJump = numList[0];
        while (slowerJump != fasterJump) {
            slowerJump = numList[slowerJump];
            fasterJump = numList[fasterJump];
        }
        return fasterJump;
    }


    /**
     * finds the number of unique morse representations of the words given
     *
     * @param words an array of words
     * @return number of unique representation of words in morse
     */
    public static int uniqueMorseRepresentations(String[] words) {
        StringBuilder wordInMorse = new StringBuilder();
        for (String word : words) {
            for (int i = 0; i < word.length(); i++) {
                wordInMorse.append(charsInMorse[word.charAt(i) % BASE_ASCII]);
            }
            wordsInMorse.add(wordInMorse.toString());
            wordInMorse = new StringBuilder();
        }
        int size = wordsInMorse.size();
        wordsInMorse.clear();
        return size;
    }


}
