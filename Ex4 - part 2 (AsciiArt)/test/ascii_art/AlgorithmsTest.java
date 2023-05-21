package ascii_art;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmsTest {

    @ParameterizedTest
    @MethodSource("provideArgsForFindDuplicate")
    void findDuplicate(int[] numList, int expected) {
        assertEquals(expected, Algorithms.findDuplicate(numList));
    }

    @ParameterizedTest
    @MethodSource("provideArgsForUniqueMorseRepresentation")
    void uniqueMorseRepresentations(String[] words, int expected) {
        assertEquals(expected, Algorithms.uniqueMorseRepresentations(words));
    }

    private static Stream<Arguments> provideArgsForFindDuplicate() {
        return Stream.of(
                Arguments.of(new int[] {1,2,2,3},2),
                Arguments.of(new int[] {1,3,2,2},2),
                Arguments.of(new int[] {1,2,2,3, 2, 5},2),
                Arguments.of(new int[] {1,3,2,3},3),
                Arguments.of(new int[] {1,2,7,3, 4, 6, 5, 1},1),
                Arguments.of(new int[] {1,7,2,3, 4, 6, 5, 7},7),
                Arguments.of(new int[] {1,2,3,4, 4},4),
                Arguments.of(new int[] {1,1},1),
                Arguments.of(new int[] {1,1,1,1},1),
                Arguments.of(new int[] {1,2,2,2},2),
                Arguments.of(new int[] {3,3,3,1},3),
                Arguments.of(new int[] {1,2,6,4,9,5,3,7,8,9},9),
                Arguments.of(new int[] {9,2,6,4,9,5,3,7,8,9},9)
        );
    }

    private static Stream<Arguments> provideArgsForUniqueMorseRepresentation() {
        return Stream.of(
                Arguments.of(new String[] {"sos", "abc", "sss", "ooo"}, 4),
                Arguments.of(new String[] {"sos", "sos", "sos", "sos"}, 1),
                Arguments.of(new String[] {"sos", "eewb", "eeettteee", "vtb"}, 1),
                Arguments.of(new String[] {"hello"}, 1),
                Arguments.of(new String[] {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
                                "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"}, 26),
                Arguments.of(new String[] {}, 0)
        );
    }

}