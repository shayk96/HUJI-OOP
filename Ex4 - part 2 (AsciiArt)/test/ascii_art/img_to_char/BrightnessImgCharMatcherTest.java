package ascii_art.img_to_char;

import image.Image;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BrightnessImgCharMatcherTest {

    private BrightnessImgCharMatcher binaryBoard;
    private BrightnessImgCharMatcher nonBinaryBoard;
    private BrightnessImgCharMatcher coloredBoard;

    @BeforeEach
    void setUp() {
        this.binaryBoard = new BrightnessImgCharMatcher(Image.fromFile("test/board.jpeg"), "Arial");
        this.nonBinaryBoard = new BrightnessImgCharMatcher(Image.fromFile("test/boardNonBinary.jpeg"),
                "Arial");
        this.coloredBoard = new BrightnessImgCharMatcher(Image.fromFile("test/boardColored.jpeg"),
                "Arial");
    }

    @ParameterizedTest
    @MethodSource("provideArgsForChooseChars_TwoChars")
    void chooseChars_BinaryTwoChars(int numCharsInRow, Character[] charSet, Character[][] expected) {
        assertEquals(Arrays.deepToString(expected), Arrays.deepToString(binaryBoard.chooseChars(numCharsInRow, charSet)));
    }

    @ParameterizedTest
    @MethodSource("provideArgsForChooseChars_BinaryFourChars")
    void chooseChars_BinaryFourChars(int numCharsInRow, Character[] charSet, Character[][] expected) {
        assertEquals(Arrays.deepToString(expected), Arrays.deepToString(binaryBoard.chooseChars(numCharsInRow, charSet)));
    }

    @ParameterizedTest
    @MethodSource("provideArgsForChooseChars_TwoChars")
    void chooseChars_NonBinaryTwoChars(int numCharsInRow, Character[] charSet, Character[][] expected) {
        assertEquals(Arrays.deepToString(expected), Arrays.deepToString(nonBinaryBoard.chooseChars(numCharsInRow,
                charSet)));
    }

    @ParameterizedTest
    @MethodSource("provideArgsForChooseChars_NonBinaryFourChars")
    void chooseChars_NonBinaryFourChars(int numCharsInRow, Character[] charSet, Character[][] expected) {
        assertEquals(Arrays.deepToString(expected), Arrays.deepToString(nonBinaryBoard.chooseChars(numCharsInRow, charSet)));
    }

    @ParameterizedTest
    @MethodSource("provideArgsForChooseChars_TwoChars")
    void chooseChars_ColoredTwoChars(int numCharsInRow, Character[] charSet, Character[][] expected) {
        assertEquals(Arrays.deepToString(expected), Arrays.deepToString(coloredBoard.chooseChars(numCharsInRow,
                charSet)));
    }

    @ParameterizedTest
    @MethodSource("provideArgsForChooseChars_NonBinaryFourChars")
    void chooseChars_ColoredFourChars(int numCharsInRow, Character[] charSet, Character[][] expected) {
        assertEquals(Arrays.deepToString(expected), Arrays.deepToString(coloredBoard.chooseChars(numCharsInRow,
                charSet)));
    }

    @ParameterizedTest
    @MethodSource("provideArgsForChooseChars_OneChar")
    void chooseChars_BinaryOneChar(int numCharsInRow, Character[] charSet, Character[][] expected) {
        assertEquals(Arrays.deepToString(expected),
                Arrays.deepToString(binaryBoard.chooseChars(numCharsInRow, charSet)));
    }

    @ParameterizedTest
    @MethodSource("provideArgsForChooseChars_OneChar")
    void chooseChars_NonBinaryOneChar(int numCharsInRow, Character[] charSet, Character[][] expected) {
        assertEquals(Arrays.deepToString(expected), Arrays.deepToString(nonBinaryBoard.chooseChars(numCharsInRow,
                charSet)));
    }

    @ParameterizedTest
    @MethodSource("provideArgsForChooseChars_OneChar")
    void chooseChars_ColoredOneChar(int numCharsInRow, Character[] charSet, Character[][] expected) {
        assertEquals(Arrays.deepToString(expected), Arrays.deepToString(coloredBoard.chooseChars(numCharsInRow,
                charSet)));
    }

    @ParameterizedTest
    @MethodSource("provideArgsForChooseChars_OnePixel")
    void chooseChars_OnePixel(int numCharsInRow, Character[] charSet, Character[][] expected) {
        assertEquals(Arrays.deepToString(expected), Arrays.deepToString(coloredBoard.chooseChars(numCharsInRow,
                charSet)));
        assertEquals(Arrays.deepToString(expected), Arrays.deepToString(nonBinaryBoard.chooseChars(numCharsInRow,
                charSet)));
        assertEquals(Arrays.deepToString(expected), Arrays.deepToString(binaryBoard.chooseChars(numCharsInRow,
                charSet)));
    }

    @ParameterizedTest
    @MethodSource("provideArgsForChooseChars_SameValues")
    void chooseChars_OnePixel(int numCharsInRow, Character[] charSet, Character[][] expectedb,
                              Character[][] expectedd) {
        String actual = Arrays.deepToString(binaryBoard.chooseChars(numCharsInRow,
                charSet));
        boolean allIsb = actual.equals(Arrays.deepToString(expectedb));
        boolean allIsd = actual.equals(Arrays.deepToString(expectedd));
        assertTrue(allIsb || allIsd);
    }

    private static Stream<Arguments> provideArgsForChooseChars_TwoChars() {
        Character[] charSet = {'m', 'o'};
        return Stream.of(
                Arguments.of(2, charSet, new Character[][]{{'m', 'o'}, {'o', 'm'}}),
                Arguments.of(4, charSet, new Character[][]{{'m', 'm', 'o', 'o'}, {'m', 'm', 'o', 'o'},
                        {'o', 'o', 'm', 'm'}, {'o', 'o', 'm', 'm'}}),
                Arguments.of(8, charSet, new Character[][]{
                        {'m', 'm', 'm', 'm', 'o', 'o', 'o', 'o'},
                        {'m', 'm', 'm', 'm', 'o', 'o', 'o', 'o'},
                        {'m', 'm', 'm', 'm', 'o', 'o', 'o', 'o'},
                        {'m', 'm', 'm', 'm', 'o', 'o', 'o', 'o'},
                        {'o', 'o','o', 'o', 'm', 'm', 'm', 'm'},
                        {'o', 'o','o', 'o', 'm', 'm', 'm', 'm'},
                        {'o', 'o','o', 'o', 'm', 'm', 'm', 'm'},
                        {'o', 'o','o', 'o', 'm', 'm', 'm', 'm'}})
        );
    }

    private static Stream<Arguments> provideArgsForChooseChars_BinaryFourChars() {
        Character[] charSet = {'m', 'o', '☻', ' '};
        return Stream.of(
                Arguments.of(2, charSet, new Character[][]{{'☻', ' '}, {' ', '☻'}}),
                Arguments.of(4, charSet, new Character[][]{{'☻', '☻', ' ', ' '}, {'☻', '☻', ' ', ' '},
                        {' ', ' ', '☻', '☻'}, {' ', ' ', '☻', '☻'}}),
                Arguments.of(8, charSet, new Character[][]{
                        {'☻', '☻', '☻', '☻', ' ', ' ', ' ', ' '},
                        {'☻', '☻', '☻', '☻', ' ', ' ', ' ', ' '},
                        {'☻', '☻', '☻', '☻', ' ', ' ', ' ', ' '},
                        {'☻', '☻', '☻', '☻', ' ', ' ', ' ', ' '},
                        {' ', ' ',' ', ' ', '☻', '☻', '☻', '☻'},
                        {' ', ' ',' ', ' ', '☻', '☻', '☻', '☻'},
                        {' ', ' ',' ', ' ', '☻', '☻', '☻', '☻'},
                        {' ', ' ',' ', ' ', '☻', '☻', '☻', '☻'}})
        );
    }


    private static Stream<Arguments> provideArgsForChooseChars_NonBinaryFourChars() {
        Character[] charSet = {'m', 'o', '☻', ' '};
        return Stream.of(
                Arguments.of(2, charSet, new Character[][]{{'☻', ' '}, {'o', 'm'}}),
                Arguments.of(4, charSet, new Character[][]{{'☻', '☻', ' ', ' '}, {'☻', '☻', ' ', ' '},
                        {'o', 'o', 'm', 'm'}, {'o', 'o', 'm', 'm'}}),
                Arguments.of(8, charSet, new Character[][]{
                        {'☻', '☻', '☻', '☻', ' ', ' ', ' ', ' '},
                        {'☻', '☻', '☻', '☻', ' ', ' ', ' ', ' '},
                        {'☻', '☻', '☻', '☻', ' ', ' ', ' ', ' '},
                        {'☻', '☻', '☻', '☻', ' ', ' ', ' ', ' '},
                        {'o', 'o','o', 'o', 'm', 'm', 'm', 'm'},
                        {'o', 'o','o', 'o', 'm', 'm', 'm', 'm'},
                        {'o', 'o','o', 'o', 'm', 'm', 'm', 'm'},
                        {'o', 'o','o', 'o', 'm', 'm', 'm', 'm'}})
        );
    }

    private static Stream<Arguments> provideArgsForChooseChars_OneChar() {
        Character[] charSet = {'m'};
        return Stream.of(
                Arguments.of(2, charSet, new Character[][]{{'m', 'm'}, {'m', 'm'}}),
                Arguments.of(4, charSet, new Character[][]{{'m', 'm', 'm', 'm'}, {'m', 'm', 'm', 'm'},
                        {'m', 'm', 'm', 'm'}, {'m', 'm', 'm', 'm'}}),
                Arguments.of(8, charSet, new Character[][]{
                        {'m', 'm', 'm', 'm', 'm', 'm', 'm', 'm'},
                        {'m', 'm', 'm', 'm', 'm', 'm', 'm', 'm'},
                        {'m', 'm', 'm', 'm', 'm', 'm', 'm', 'm'},
                        {'m', 'm', 'm', 'm', 'm', 'm', 'm', 'm'},
                        {'m', 'm','m', 'm', 'm', 'm', 'm', 'm'},
                        {'m', 'm','m', 'm', 'm', 'm', 'm', 'm'},
                        {'m', 'm','m', 'm', 'm', 'm', 'm', 'm'},
                        {'m', 'm','m', 'm', 'm', 'm', 'm', 'm'}})
        );
    }

    private static Stream<Arguments> provideArgsForChooseChars_OnePixel() {
        Character[] charSet = {' ', 'o', '#'};
        return Stream.of(
                Arguments.of(1, charSet, new Character[][]{{'o'}})
        );
    }

    private static Stream<Arguments> provideArgsForChooseChars_SameValues() {
        Character[] charSet = {'b','d'};
        return Stream.of(
                Arguments.of(1, charSet, new Character[][]{{'b'}},
                        new Character[][]{{'d'}}),
                Arguments.of(2, charSet, new Character[][]{{'b', 'b'}, {'b', 'b'}},
                        new Character[][]{{'d', 'd'}, {'d', 'd'}}),
                Arguments.of(4, charSet, new Character[][]{{'b', 'b', 'b', 'b'}, {'b', 'b', 'b', 'b'},
                        {'b', 'b', 'b', 'b'}, {'b', 'b', 'b', 'b'}},
                        new Character[][]{{'d', 'd', 'd', 'd'}, {'d', 'd', 'd', 'd'},
                                {'d', 'd', 'd', 'd'}, {'d', 'd', 'd', 'd'}}),
                Arguments.of(8, charSet, new Character[][]{
                        {'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b'},
                        {'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b'},
                        {'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b'},
                        {'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b'},
                        {'b', 'b','b', 'b', 'b', 'b', 'b', 'b'},
                        {'b', 'b','b', 'b', 'b', 'b', 'b', 'b'},
                        {'b', 'b','b', 'b', 'b', 'b', 'b', 'b'},
                        {'b', 'b','b', 'b', 'b', 'b', 'b', 'b'}},
                        new Character[][]{
                                {'d', 'd', 'd', 'd', 'd', 'd', 'd', 'd'},
                                {'d', 'd', 'd', 'd', 'd', 'd', 'd', 'd'},
                                {'d', 'd', 'd', 'd', 'd', 'd', 'd', 'd'},
                                {'d', 'd', 'd', 'd', 'd', 'd', 'd', 'd'},
                                {'d', 'd','d', 'd', 'd', 'd', 'd', 'd'},
                                {'d', 'd','d', 'd', 'd', 'd', 'd', 'd'},
                                {'d', 'd','d', 'd', 'd', 'd', 'd', 'd'},
                                {'d', 'd','d', 'd', 'd', 'd', 'd', 'd'}})
        );
    }
}