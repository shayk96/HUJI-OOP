package ascii_art;

import image.Image;

import java.util.logging.Logger;

/**
 * Runs the program.
 */
public class Driver {
    /**
     * receives img path from the user in the command line and runs the shell
     *
     * @param args array that holds the user input from the console.
     * @throws Exception if the program failed
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("USAGE: java asciiArt ");
            return;
        }
        Image img = Image.fromFile(args[0]);
        if (img == null) {
            Logger.getGlobal().severe("Failed to open image file " + args[0]);
            return;
        }
        new Shell(img).run();
    }
}
