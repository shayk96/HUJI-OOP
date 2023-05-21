package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.util.Scanner;
import java.util.*;

/**
 * a class that creates and runs a shell type of console.
 */
public class Shell {


    private final Scanner scanner = new Scanner(System.in);

    private static final String WRONG_INPUT_MSG = "Wrong input, enter a valid command and parameter or " +
            "just a command. Valid commands: <add,remove> <<char>-<char>,<char>,space,all>, res <up,down>, " +
            "chars, render, console, html";
    private static final String WRONG_PARAMETER_MSG = "Wrong parameter. Valid usage:<add,remove> " +
            "<<char>-<char>,<char>,space,all>";
    private static final String WRONG_RES_INPUT_MSG = "Wrong parameter. Valid usage: res <up,down>";
    private static final String MINIMUM_RES_MSG = "The resolution is at its minimum";
    private static final String MAXIMUM_RES_MSG = "The resolution is at its maximum";
    private static final String RES_CHANGE_MSG = "Width set to %d\n";

    private static final String UP_COMMAND = "up";
    private static final String DOWN_COMMAND = "down";
    private static final String SPACE_COMMAND = "space";
    private static final String ALL_COMMAND = "all";
    private static final String RES_COMMAND = "res";
    private static final String CHARS_COMMAND = "chars";
    private static final String CONSOLE_COMMAND = "console";
    private static final String ADD_COMMAND = "add";
    private static final String RENDER_COMMAND = "render";
    private static final String REMOVE_COMMAND = "remove";
    private static final String EXIT_COMMAND = "exit";
    private static final String CMD_PROMPT = ">>> ";

    private static final String FONT_NAME = "Courier New";
    private static final String OUTPUT_FILENAME = "out.html";
    private static final int FIRST_CHAR_INDEX = 0;
    private static final int SECOND_CHAR_INDEX = 2;
    private static final int FIRST_CHAR = 0;
    private static final int SECOND_CHAR = 1;
    private static final int COMMAND = 0;
    private static final int PARAM = 1;

    private final Set<Character> charSet = new HashSet<>();
    private static final char[] ALL_CHARS_RANGE = new char[]{' ', '~'};
    private static final char[] SPACE = new char[]{' ', ' '};
    private static final String SPACE_REGEX = " ";
    private static final String CHAR_RANGE_PATTERN = "\\S-\\S";
    private static final String[] INITIAL_CHARS_RANGE = {"add", "0-9"};
    private static final int INITIAL_CHARS_IN_ROW = 64;
    private static final int MIN_PIXELS_PER_CHAR = 2;

    private final BrightnessImgCharMatcher charMatcher;
    private final int minCharsInRow;
    private final int maxCharsInRow;

    private AsciiOutput output;
    private int charsInRow;


    /**
     * creates an instance of the class and initializes the maximum and minimum limits
     *
     * @param img the img to transform into ASCII representation
     */
    public Shell(Image img) {
        minCharsInRow = Math.max(1, img.getWidth() / img.getHeight());
        maxCharsInRow = img.getWidth() / MIN_PIXELS_PER_CHAR;
        charsInRow = Math.max(Math.min(INITIAL_CHARS_IN_ROW, maxCharsInRow), minCharsInRow);
        charMatcher = new BrightnessImgCharMatcher(img, FONT_NAME);
        output = new HtmlAsciiOutput(OUTPUT_FILENAME, FONT_NAME);
        manipulateChars(INITIAL_CHARS_RANGE, true);
    }

    /**
     * prints out all the chars in the hash map in a sorted order
     */
    private void showChars() {
        charSet.stream().sorted().forEach(aChar -> System.out.print(aChar + " "));
        System.out.println();
    }

    /**
     * checks if the command is valid and calls a helper function that changes the resolution according to the
     * command given
     *
     * @param commands the commands inputted by the user
     */
    private void resChange(String[] commands) {
        if (commands.length == 1 || (!commands[PARAM].equals(UP_COMMAND) &&
                !commands[PARAM].equals(DOWN_COMMAND))) {
            System.out.println(WRONG_RES_INPUT_MSG);
            return;
        }
        boolean success = resChangeHelper(commands[PARAM]);
        if (!success && commands[PARAM].equals(UP_COMMAND)) {
            System.out.println(MAXIMUM_RES_MSG);
        } else if (!success) {
            System.out.println(MINIMUM_RES_MSG);
        }
    }

    /**
     * increases or decreases the resolution by 2 if the limit is not reached
     *
     * @param upOrDown the command given by the user
     * @return true upon success else false
     */
    private boolean resChangeHelper(String upOrDown) {
        if (upOrDown.equals(UP_COMMAND) && charsInRow * 2 <= maxCharsInRow) {
            charsInRow *= 2;
            System.out.printf(RES_CHANGE_MSG, charsInRow);
            return true;
        }
        if (upOrDown.equals(DOWN_COMMAND) && charsInRow / 2 >= minCharsInRow) {
            charsInRow /= 2;
            System.out.printf(RES_CHANGE_MSG, charsInRow);
            return true;
        }
        return false;
    }

    /**
     * checks if the command given by the user is valid and return an array containing 2 chars that symbolizes
     * the limits to of the chars sequence to add
     *
     * @param commands the commands given by the user.
     * @return an array of 2 chars
     */
    private static char[] parseCharRange(String[] commands) {
        if (commands.length == 1) {
            System.out.println(WRONG_PARAMETER_MSG);
            return null;
        }
        String command = commands[PARAM];
        if (command.length() == 1) {
            return new char[]{command.charAt(FIRST_CHAR_INDEX), command.charAt(FIRST_CHAR_INDEX)};
        }
        if (command.matches(CHAR_RANGE_PATTERN)) {
            return new char[]{command.charAt(FIRST_CHAR_INDEX), command.charAt(SECOND_CHAR_INDEX)};
        }
        if (command.equals(ALL_COMMAND)) {
            return ALL_CHARS_RANGE;
        }
        if (command.equals(SPACE_COMMAND)) {
            return SPACE;
        }
        return null;
    }

    /**
     * calls the parseCharRange function to parse the chars from the user input. Then calls a helper function
     * that adds or removes the specified chars to the chars set or from the char set. if parseCharRange
     * returns null the function does nothing
     *
     * @param commands the user input
     * @param add      specifies if the user wants to add or remove chars
     */
    private void manipulateChars(String[] commands, boolean add) {
        char[] range = parseCharRange(commands);
        if (range != null && range[FIRST_CHAR] >= range[SECOND_CHAR]) {
            if (add) {
                CharsHelper(range[SECOND_CHAR], range[FIRST_CHAR], ADD_COMMAND);
            } else {
                CharsHelper(range[SECOND_CHAR], range[FIRST_CHAR], REMOVE_COMMAND);
            }
        } else if (range != null) {
            if (add) {
                CharsHelper(range[FIRST_CHAR], range[SECOND_CHAR], ADD_COMMAND);
            } else {
                CharsHelper(range[FIRST_CHAR], range[SECOND_CHAR], REMOVE_COMMAND);
            }
        }
    }

    /**
     * adds a sequence of chars to the chars set
     *
     * @param start       the char to start with
     * @param end         the last char to add
     * @param addOrRemove tells the function to add the sequence or remove the sequence
     */
    private void CharsHelper(char start, char end, String addOrRemove) {
        switch (addOrRemove) {
            case ADD_COMMAND:
                while (start <= end) {
                    charSet.add(start);
                    start++;
                }
                break;
            case REMOVE_COMMAND:
                while (start <= end) {
                    charSet.remove(start);
                    start++;
                }
        }
    }


    /**
     * renders the img in ASCII form. depending on the user input the img will be printed to the console or
     * to an HTML file
     */
    private void render() {
        if (charSet.size() == 0) {
            return;
        }
        Character[] charsArray = new Character[charSet.size()];
        charsArray = charSet.toArray(charsArray);
        output.output(charMatcher.chooseChars(charsInRow, charsArray));
    }

    /**
     * this function handles the "console","chars" and "render" commands
     *
     * @param commands the commands inputted by the user
     */
    private void command(String[] commands) {
        if (commands.length > 1) {
            System.out.println(WRONG_INPUT_MSG);
            return;
        }
        switch (commands[COMMAND]) {
            case CONSOLE_COMMAND:
                this.output = new ConsoleAsciiOutput();
                break;
            case CHARS_COMMAND:
                showChars();
                break;
            case RENDER_COMMAND:
                render();
        }
    }

    /**
     * this function runs the shell. Receives input from the user and calls the corresponding functions.
     */
    public void run() {
        System.out.print(CMD_PROMPT);
        String cmd = scanner.nextLine().trim();
        String[] commands = cmd.split(SPACE_REGEX, 2);
        while (!(commands[COMMAND].equals(EXIT_COMMAND) && commands.length == 1)) {
            if (commands[COMMAND].length() >= 1) {
                switch (commands[COMMAND]) {
                    case ADD_COMMAND:
                        manipulateChars(commands, true);
                        break;
                    case REMOVE_COMMAND:
                        manipulateChars(commands, false);
                        break;
                    case RES_COMMAND:
                        resChange(commands);
                        break;
                    case CONSOLE_COMMAND:
                    case RENDER_COMMAND:
                    case CHARS_COMMAND:
                        command(commands);
                        break;
                    default:
                        System.out.println(WRONG_INPUT_MSG);
                }
            }
            System.out.print(CMD_PROMPT);
            cmd = scanner.nextLine().trim();
            commands = cmd.split(SPACE_REGEX, 2);
        }
    }


}
