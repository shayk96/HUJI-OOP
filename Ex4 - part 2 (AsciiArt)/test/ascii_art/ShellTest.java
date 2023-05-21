package ascii_art;

import image.Image;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class ShellTest {

    private static final String RES_REGEX = "/^Width set to \\d+$/gm";
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    private Shell shell;

    @BeforeEach
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    private void setUpShell(String data) {
        setUpShell("test/board.jpeg", data);
    }

    private void setUpShell(String imagePath, String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
        Image img = Image.fromFile(imagePath);
        assumeTrue(img != null);
        shell = new Shell(img);
    }

    private String getOutput() {
        return testOut.toString();
    }

    private String getFormattedOutput() {
        String regex = "\\s+$";
        return getOutput().replaceAll(">>> ", "").replaceAll(regex, "");
    }

    @AfterEach
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    @ParameterizedTest
    @ValueSource(strings = {"exit", "exit \n"})
    void exit_OnCorrectSpelling_ExitsImmediately(String exitPrompt) {
        setUpShell(exitPrompt);
        shell.run();
    }

    @Test
    void invalidCommand_LoopsUntilTypedCorrectly() {
        setUpShell(inputFrom("", "e xit", "addd", "re-move", "redner", "konsole", "chars chars chars",
                "res up up", "Res up", "EXIT", "Chars", "Add 1", "REMOVE 1", "ConSoLe", "RENdER"));
        shell.run();
        String[] messages = getFormattedOutput().split("\n");
        // Makes sure each incorrect command prints an error message
        assertEquals(14, messages.length);
    }

    @Test
    void chars_InitialCharsAre1To9() {
        setUpShell(inputFrom("chars", "exit"));
        shell.run();
        assertEquals("0 1 2 3 4 5 6 7 8 9", getFormattedOutput());
    }

    @ParameterizedTest
    @MethodSource("provideArgsForAddCorrectRange")
    void add_OnCorrectRange_RangeGetsAdded(String userInput, String expected) {
        setUpShell(userInput);
        shell.run();
        assertEquals(expected, getFormattedOutput());
    }

    @ParameterizedTest
    @MethodSource("provideArgsForAddNoDuplicates")
    void add_OnRangeAlreadyInCharSet_DuplicatesNotAdded(String userInput, String expected) {
        setUpShell(userInput);
        shell.run();
        assertEquals(expected, getFormattedOutput());
    }

    @ParameterizedTest
    @MethodSource("provideArgsForAddIncorrectRange")
    void add_OnIncorrectRange_ErrorPrintedAndNothingHappens(String userInput, String expected,
                                                            Integer messagesToExpect) {
        setUpShell(userInput);
        shell.run();
        String[] messages = getFormattedOutput().split("\n");
        assumeTrue(messages.length == messagesToExpect);
        for (int i = 0; i < messagesToExpect - 1; i++) {
            assertFalse(messages[i].isEmpty());
        }
        assertEquals(expected, messages[messagesToExpect - 1]);
    }

    @ParameterizedTest
    @MethodSource("provideArgsForRemoveCorrectRangeInSet")
    void remove_OnCorrectRangeInSet_RangeGetsRemoved(String userInput, String expected) {
        setUpShell(userInput);
        shell.run();
        assertEquals(expected, getFormattedOutput());
    }

    @ParameterizedTest
    @MethodSource("provideArgsForRemoveCorrectRangeNotInSet")
    void remove_OnCorrectRangeNotInSet_NothingHappens(String userInput, String expected) {
        setUpShell(userInput);
        shell.run();
        assertEquals(expected, getFormattedOutput());
    }

    @ParameterizedTest
    @MethodSource("provideArgsForRemoveIncorrectRange")
    void remove_OnIncorrectRange_ErrorPrintedAndNothingHappens(String userInput, String expected,
                                                               int messagesToExpect) {
        setUpShell(userInput);
        shell.run();
        String[] messages = getFormattedOutput().split("\n");
        assumeTrue(messages.length == messagesToExpect);
        for (int i = 0; i < messagesToExpect - 1; i++) {
            assertFalse(messages[i].isEmpty());
        }
        assertEquals(expected, messages[messagesToExpect - 1]);
    }

    @ParameterizedTest
    @MethodSource("provideArgsForResUpInRange")
    void resUp_InRange_RangeIsIncreasedAndProperlyPrinted(String image, String userInput,
                                                          int[] expectedRes) {
        setUpShell(image, userInput);
        shell.run();
        String[] messages = getFormattedOutput().split("\n");
        assumeTrue(messages.length == expectedRes.length);
        for (int i = 0; i < messages.length; i++) {
            assertEquals(String.format("Width set to %d", expectedRes[i]), messages[i]);
        }
    }

    @ParameterizedTest
    @MethodSource("provideArgsForResDownInRange")
    void resDown_InRange_RangeIsDecreasedAndProperlyPrinted(String image, String userInput,
                                                            int[] expectedRes) {
        setUpShell(image, userInput);
        shell.run();
        String[] messages = getFormattedOutput().split("\n");
        assumeTrue(messages.length == expectedRes.length);
        for (int i = 0; i < messages.length; i++) {
            assertEquals(String.format("Width set to %d", expectedRes[i]), messages[i]);
        }
    }

    @ParameterizedTest
    @MethodSource("provideArgsForResUpExceedsMaximal")
    void resUp_ExceedsMaximal_ErrorPrintedAndNothingHappens(String image, String userInput,
                                                            int[] expectedRes) {
        setUpShell(image, userInput);
        shell.run();
        String[] messages = getFormattedOutput().split("\n");
        assumeTrue(messages.length == expectedRes.length);
        for (int i = 0; i < messages.length; i++) {
            if (expectedRes[i] != -1)
                assertEquals(String.format("Width set to %d", expectedRes[i]), messages[i]);
            else
                assertFalse(messages[i].matches(RES_REGEX));
        }
    }

    @ParameterizedTest
    @MethodSource("provideArgsForResDownExceedsMinimal")
    void resDown_ExceedsMinimal_ErrorPrintedAndNothingHappens(String image, String userInput,
                                                              int[] expectedRes) {
        setUpShell(image, userInput);
        shell.run();
        String[] messages = getFormattedOutput().split("\n");
        assumeTrue(messages.length == expectedRes.length);
        for (int i = 0; i < messages.length; i++) {
            if (expectedRes[i] != -1)
                assertEquals(String.format("Width set to %d", expectedRes[i]), messages[i]);
            else
                assertFalse(messages[i].matches(RES_REGEX));
        }
    }

    @Test
    void res_OnIncorrectFormat_ErrorPrintedAndNothingHappens() {
        setUpShell(inputFrom("res", "res ", "res s", "res blah", "res res up", "res DOWN"));
        shell.run();
        String[] messages = getFormattedOutput().split("\n");
        assertEquals(6, messages.length);
        for (int i = 0; i < 6; i++) {
            assertFalse(messages[i].isEmpty());
            assertFalse(messages[i].matches(RES_REGEX));
        }
    }

    @ParameterizedTest
    @MethodSource("provideArgsForRender")
    void render_OnNonEmptyCharSet_RendersProperly(String image, String userInput, String expected) throws IOException {
        File outFile = new File("out.html");

        if (outFile.exists() && !outFile.isDirectory()) {
            assumeTrue(outFile.delete());
        }

        setUpShell(image, userInput);
        shell.run();

        outFile = new File("out.html");
        assertTrue(outFile.exists() && !outFile.isDirectory());
        Document doc = Jsoup.parse(outFile, "UTF-8", "");
        String actual = doc.select("p").text();
        assertEquals(expected, actual);
    }

    @Test
    void render_OnEmptyCharset_NothingHappensAndErrorIsPrinted() {
        File outFile = new File("out.html");

        if (outFile.exists() && !outFile.isDirectory()) {
            assumeTrue(outFile.delete());
        }

        setUpShell(inputFrom("remove all", "render"));
        shell.run();

        String output = getFormattedOutput();
        outFile = new File("out.html");
        assertFalse(outFile.exists());
        assertTrue(output.isEmpty());

    }

    @ParameterizedTest
    @MethodSource("provideArgsForConsole")
    void consoleRender_OnNonEmptyCharSet_RendersProperly(String image, String userInput, String[] expected,
                                                         int numOfPrePrints) throws IOException {
        File outFile = new File("out.html");

        if (outFile.exists() && !outFile.isDirectory()) {
            assumeTrue(outFile.delete());
        }

        setUpShell(image, userInput);
        shell.run();

        outFile = new File("out.html");
        assertFalse(outFile.exists());
        String[] output = getFormattedOutput().split("\n");
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], output[i + numOfPrePrints].replaceAll("\\s", ""));
        }
    }


    private static Stream<Arguments> provideArgsForAddCorrectRange() {
        return Stream.of(
                Arguments.of(inputFrom("add a", "chars"), "0 1 2 3 4 5 6 7 8 9 a"),
                Arguments.of(inputFrom("add a-a", "chars"), "0 1 2 3 4 5 6 7 8 9 a"),
                Arguments.of(inputFrom("add a-d", "chars"), "0 1 2 3 4 5 6 7 8 9 a b c d"),
                Arguments.of(inputFrom("add d-a", "chars"), "0 1 2 3 4 5 6 7 8 9 a b c d"),
                Arguments.of(inputFrom("add A-D", "chars"), "0 1 2 3 4 5 6 7 8 9 A B C D"),
                Arguments.of(inputFrom("add D-A", "chars"), "0 1 2 3 4 5 6 7 8 9 A B C D"),
                Arguments.of(inputFrom("add Y-b", "chars"), "0 1 2 3 4 5 6 7 8 9 Y Z [ \\ ] ^ _ ` a b"),
                Arguments.of(inputFrom("add (", "chars"), "( 0 1 2 3 4 5 6 7 8 9"),
                Arguments.of(inputFrom("add space", "chars"), "  0 1 2 3 4 5 6 7 8 9"),
                Arguments.of(inputFrom("add a", "add b", "add c", "add d", "chars"), "0 1 2 3 4 5 6 7 8 9 a" +
                        " b c d")
        );
    }

    private static Stream<Arguments> provideArgsForAddNoDuplicates() {
        return Stream.of(
                Arguments.of(inputFrom("add 1", "chars"), "0 1 2 3 4 5 6 7 8 9"),
                Arguments.of(inputFrom("add 1-9", "chars"), "0 1 2 3 4 5 6 7 8 9"),
                Arguments.of(inputFrom("add 0-:", "chars"), "0 1 2 3 4 5 6 7 8 9 :"),
                Arguments.of(inputFrom("add all", "chars"), "  ! \" # $ % & ' ( ) * + , - . / 0 1 2 3 4 5 6" +
                        " 7 8 9 : ; < = > ? @ A B C D E F G H I J K L M N O P Q R S T U V W X Y Z [ \\ ] ^ " +
                        "_ ` a b c d e f g h i j k l m n o p q r s t u v w x y z { | } ~")
        );
    }

    private static Stream<Arguments> provideArgsForAddIncorrectRange() {
        return Stream.of(
                Arguments.of(inputFrom("add aa", "chars"), "0 1 2 3 4 5 6 7 8 9", 2),
                Arguments.of(inputFrom("add", "chars"), "0 1 2 3 4 5 6 7 8 9", 2),
                Arguments.of(inputFrom("add a-aa", "chars"), "0 1 2 3 4 5 6 7 8 9", 2),
                Arguments.of(inputFrom("add aa-a", "chars"), "0 1 2 3 4 5 6 7 8 9", 2),
                Arguments.of(inputFrom("add space-a", "chars"), "0 1 2 3 4 5 6 7 8 9", 2),
                Arguments.of(inputFrom("add 1_9", "chars"), "0 1 2 3 4 5 6 7 8 9", 2),
                Arguments.of(inputFrom("add ALL", "chars"), "0 1 2 3 4 5 6 7 8 9", 2),
                Arguments.of(inputFrom("add aa", "add aa", "add aa", "add aa", "chars"), "0 1 2 3 4 5 6 7 8 9", 5)
        );
    }

    private static Stream<Arguments> provideArgsForRemoveCorrectRangeInSet() {
        return Stream.of(
                Arguments.of(inputFrom("remove 0", "chars"), "1 2 3 4 5 6 7 8 9"),
                Arguments.of(inputFrom("remove 0-0", "chars"), "1 2 3 4 5 6 7 8 9"),
                Arguments.of(inputFrom("remove 0-4", "chars"), "5 6 7 8 9"),
                Arguments.of(inputFrom("remove 4-0", "chars"), "5 6 7 8 9"),
                Arguments.of(inputFrom("remove all", "chars"), ""),
                Arguments.of(inputFrom("add all", "remove all", "chars"), ""),
                Arguments.of(inputFrom("add a-z", "remove b-y", "chars"), "0 1 2 3 4 5 6 7 8 9 a z"),
                Arguments.of(inputFrom("add a-d", "remove 0-9", "chars"), "a b c d"),
                Arguments.of(inputFrom("add space", "remove space", "chars"), "0 1 2 3 4 5 6 7 8 9"),
                Arguments.of(inputFrom("add a-d", "remove c", "remove d", "chars"), "0 1 2 3 4 5 6 7 8 9 a b")
        );
    }

    private static Stream<Arguments> provideArgsForRemoveCorrectRangeNotInSet() {
        return Stream.of(
                Arguments.of(inputFrom("remove a", "chars"), "0 1 2 3 4 5 6 7 8 9"),
                Arguments.of(inputFrom("remove a-a", "chars"), "0 1 2 3 4 5 6 7 8 9"),
                Arguments.of(inputFrom("remove a-z", "chars"), "0 1 2 3 4 5 6 7 8 9"),
                Arguments.of(inputFrom("remove z-a", "chars"), "0 1 2 3 4 5 6 7 8 9"),
                Arguments.of(inputFrom("remove space", "chars"), "0 1 2 3 4 5 6 7 8 9")
        );
    }

    private static Stream<Arguments> provideArgsForRemoveIncorrectRange() {
        return Stream.of(
                Arguments.of(inputFrom("remove aa", "chars"), "0 1 2 3 4 5 6 7 8 9", 2),
                Arguments.of(inputFrom("remove", "chars"), "0 1 2 3 4 5 6 7 8 9", 2),
                Arguments.of(inputFrom("remove a-aa", "chars"), "0 1 2 3 4 5 6 7 8 9", 2),
                Arguments.of(inputFrom("remove aa-a", "chars"), "0 1 2 3 4 5 6 7 8 9", 2),
                Arguments.of(inputFrom("remove space-a", "chars"), "0 1 2 3 4 5 6 7 8 9", 2),
                Arguments.of(inputFrom("remove 1_9", "chars"), "0 1 2 3 4 5 6 7 8 9", 2),
                Arguments.of(inputFrom("remove ALL", "chars"), "0 1 2 3 4 5 6 7 8 9", 2),
                Arguments.of(inputFrom("remove aa", "remove aa", "remove aa", "remove aa", "chars"),
                        "0 1 2 3 4 5 6 7 8 9", 5)
        );
    }

    private static Stream<Arguments> provideArgsForResUpInRange() {
        return Stream.of(
                Arguments.of("test/dino.png", inputFrom("res up"), new int[]{128}),
                Arguments.of("test/dino.png", inputFrom("res up", "res up"), new int[]{128, 256}),
                Arguments.of("test/dino.png", inputFrom("res up", "res up", "res up"), new int[]{128, 256,
                        512}),
                Arguments.of("test/dino.png", inputFrom("res up", "res up", "res up", "res up"),
                        new int[]{128, 256,
                                512, 1024})
        );
    }

    private static Stream<Arguments> provideArgsForResDownInRange() {
        return Stream.of(
                Arguments.of("test/dino.png", inputFrom("res down"), new int[]{32}),
                Arguments.of("test/dino.png", inputFrom("res down", "res down"), new int[]{32, 16}),
                Arguments.of("test/dino.png", inputFrom("res down", "res down", "res down"),
                        new int[]{32, 16, 8}),
                Arguments.of("test/dino.png", inputFrom("res down", "res down", "res down", "res down"),
                        new int[]{32, 16, 8, 4}),
                Arguments.of("test/dino.png", inputFrom("res down", "res down", "res down", "res down",
                        "res down"), new int[]{32, 16, 8, 4, 2}),
                Arguments.of("test/dino.png", inputFrom("res down", "res down", "res down", "res down",
                        "res down", "res down"), new int[]{32, 16, 8, 4, 2, 1})
        );
    }

    private static Stream<Arguments> provideArgsForResUpExceedsMaximal() {
        return Stream.of(
                Arguments.of("test/dino.png", inputFrom("res up", "res up", "res up", "res up", "res up",
                                "res down"),
                        new int[]{128, 256, 512, 1024, -1, 512}),
                Arguments.of("test/dino.png", inputFrom("res up", "res up", "res up", "res up", "res up",
                                "res down", "res up", "res up", "res up", "res down"),
                        new int[]{128, 256, 512, 1024, -1, 512, 1024, -1, -1, 512}),
                Arguments.of("test/board.jpeg", inputFrom("res up", "res up", "res down"),
                        new int[]{-1, -1, 16})
        );
    }

    private static Stream<Arguments> provideArgsForResDownExceedsMinimal() {
        return Stream.of(
                Arguments.of("test/dino.png", inputFrom("res down", "res down", "res down", "res down",
                        "res down", "res down", "res down", "res up"), new int[]{32, 16, 8, 4, 2, 1, -1, 2}),
                Arguments.of("test/dino.png", inputFrom("res down", "res down", "res down", "res down",
                                "res down", "res down", "res down", "res up", "res down", "res down", "res down",
                                "res up"),
                        new int[]{32, 16, 8, 4, 2, 1, -1, 2, 1, -1, -1, 2}),
                Arguments.of("test/board.jpeg", inputFrom("res down", "res down", "res down", "res down",
                        "res down", "res down", "res down", "res up"), new int[]{16, 8, 4, 2, 1, -1, -1, 2})
        );
    }

    private static Stream<Arguments> provideArgsForRender() {
        return Stream.of(
                Arguments.of("test/board.jpeg", inputFrom("res down", "res down", "remove all", "add m",
                        "add o", "render"), "mmmmoooo mmmmoooo mmmmoooo mmmmoooo " +
                        "oooommmm oooommmm oooommmm oooommmm"),
                Arguments.of("test/board.jpeg", inputFrom("res down", "res down", "res down",
                        "remove all", "add m", "add o", "render"), "mmoo mmoo oomm oomm"),
                Arguments.of("test/board.jpeg", inputFrom("res down", "res down", "res down", "res down",
                        "remove all", "add o", "render"), "oo oo"),
                Arguments.of("test/board.jpeg", inputFrom("res down", "res down", "res down",
                        "res down", "res down", "remove all", "add o", "render"), "o"),
                Arguments.of("test/boardNonBinary.jpeg", inputFrom("res down", "res down", "remove all",
                        "add ^", "add o", "add `", "add @", "render"), "@@@@```` @@@@```` @@@@```` " +
                        "@@@@```` " +
                        "^^^^oooo ^^^^oooo ^^^^oooo ^^^^oooo")
        );
    }

    private static Stream<Arguments> provideArgsForConsole() {
        return Stream.of(
                Arguments.of("test/board.jpeg", inputFrom("res down", "res down", "remove all", "add m",
                        "add o", "console", "render"), new String[]{"mmmmoooo", "mmmmoooo",
                        "mmmmoooo", "mmmmoooo", "oooommmm", "oooommmm", "oooommmm", "oooommmm"}, 2),
                Arguments.of("test/board.jpeg", inputFrom("res down", "res down", "res down",
                                "remove all", "add m", "add o", "console", "render"),
                        new String[]{"mmoo", "mmoo", "oomm", "oomm"}, 3),
                Arguments.of("test/board.jpeg", inputFrom("res down", "res down", "res down", "res down",
                        "remove all", "add o", "console", "render"), new String[]{"oo", "oo"}, 4),
                Arguments.of("test/board.jpeg", inputFrom("res down", "res down", "res down",
                                "res down", "res down", "remove all", "add o", "console", "render"),
                        new String[]{"o"}, 5),
                Arguments.of("test/boardNonBinary.jpeg", inputFrom("res down", "res down", "remove all",
                                "add ^", "add o", "add `", "add @", "console", "render"),
                        new String[]{"@@@@````", "@@@@````", "@@@@````", "@@@@````",
                                "^^^^oooo", "^^^^oooo", "^^^^oooo", "^^^^oooo"}, 2)
        );
    }


    private static String inputFrom(String... lines) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : lines) {
            stringBuilder.append(line);
            stringBuilder.append('\n');
        }
        stringBuilder.append("exit\n");
        return stringBuilder.toString();
    }
}