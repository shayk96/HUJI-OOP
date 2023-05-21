package ascii_art.img_to_char;

import image.Image;

import java.awt.*;
import java.util.HashMap;

/**
 * a class that receives an img and converts it to Ascii representation
 */
public class BrightnessImgCharMatcher {


    private static final int CHAR_PIXELS = 16;
    private static final double RED_RATIO = 0.2126;
    private static final double GREEN_RATIO = 0.7152;
    private static final double BLUE_RATIO = 0.0722;
    private static final int PIXELS_IN_IMAGE = CHAR_PIXELS * CHAR_PIXELS - 1;
    private int prevNumCharsInRow = 0;
    private final Image img;
    private final String font;
    private final HashMap<Image, Double> cache = new HashMap<>();


    /**
     * creates an instance of the class
     *
     * @param img  the img to convect to Ascii
     * @param font the font of the Ascii characters
     */
    public BrightnessImgCharMatcher(Image img, String font) {
        this.img = img;
        this.font = font;
    }

    /**
     * receives a list of chars and an image and transforms it to an image that is compiles from Ascii
     * characters
     *
     * @param numCharsInRow the number of char the user wants in a row
     * @param charSet       the Ascii chars to compile the image with
     * @return an array of chars that represents the image
     */
    public char[][] chooseChars(int numCharsInRow, Character[] charSet) {
        numCharsInRow = convertNumOfCharsToPowerOfTwo(numCharsInRow);
        if (numCharsInRow != prevNumCharsInRow) {
            cache.clear();
            calculateImageBrightness(numCharsInRow);
            prevNumCharsInRow = numCharsInRow;
        }
        double[] charsBrightness = extendBrightness(getCharsBrightness(charSet));
        return convertImageToAscii(charSet, charsBrightness, numCharsInRow);
    }

    /**
     * check that the number of chars in a row that the user inputs is a power of 2. if not changes it to the
     * closet number that is a power of 2 that is bigger than the number that the user inputted.
     *
     * @param numCharsInRow the number of chars in a row that the user wants
     * @return corrected number that is a power of 2 or the original number
     */
    private int convertNumOfCharsToPowerOfTwo(int numCharsInRow) {
        double numCharsLogTwo = Math.log(numCharsInRow) / Math.log(2);
        if (((int) (Math.ceil(numCharsLogTwo)) != (int) (Math.floor(numCharsLogTwo)))) {
            int correctNumCharsInRow = 1;
            while (correctNumCharsInRow <= numCharsInRow) {
                correctNumCharsInRow = correctNumCharsInRow * 2;
            }
            numCharsInRow = correctNumCharsInRow;
        }
        return numCharsInRow;
    }

    /**
     * splits the img to sub images and replaces each image with a matching Ascii char by matching the
     * brightness level
     *
     * @param charSet         the chars to replace the image with
     * @param charsBrightness an array that holds the chars' brightness level
     * @param numCharsInRow   hoe many chars to put n a row. determines how many sub images are going to be
     * @return a list representing the image in Ascii
     */
    private char[][] convertImageToAscii(Character[] charSet, double[] charsBrightness, int numCharsInRow) {
        int pixels = img.getWidth() / numCharsInRow;
        char[][] asciiArt = new char[img.getHeight() / pixels][img.getWidth() / pixels];
        if (charSet.length == 0){
            return asciiArt;
        }
        int colIndex = 0, rowIndex = 0;
        for (Image subImage : img.squareSubImagesOfSize(pixels)) {
            int closestChar = getClosestChar(charsBrightness, subImage);
            asciiArt[rowIndex][colIndex] = charSet[closestChar];
            colIndex++;
            if (colIndex == numCharsInRow) {
                colIndex = 0;
                rowIndex++;
            }
        }
        return asciiArt;
    }

    /**
     * the function finds the closest char in its brightness level to the sub image brightness level
     *
     * @param charsBrightness an array that contains the Ascii chars brightness level
     * @param subImage        the image to replace with an Ascii char
     * @return the matching char index in the char array
     */
    private int getClosestChar(double[] charsBrightness, Image subImage) {
        double closestBrightness = 1;
        int closestChar = 0;
        for (int i = 0; i < charsBrightness.length; i++) {
            if (Math.abs(charsBrightness[i] - cache.get(subImage)) < closestBrightness) {
                closestBrightness = Math.abs(charsBrightness[i] - cache.get(subImage));
                closestChar = i;
            }
        }
        return closestChar;
    }


    /**
     * receives an array of chars and calculates the brightness level of each one
     *
     * @param charArray the chars to calculate
     * @return an Array of the brightness levels
     */
    private double[] getCharsBrightness(Character[] charArray) {
        double[] charBrightness = new double[charArray.length];
        boolean[][][] charImgArray = new boolean[charArray.length][][];
        renderChars(charArray, charImgArray);
        calculateCharBrightness(charBrightness, charImgArray);
        return charBrightness;
    }


    /**
     * renders the chars by using the CharRenderer class
     *
     * @param charArray    the chars to render
     * @param charImgArray the rendered representations of the chars
     */
    private void renderChars(Character[] charArray, boolean[][][] charImgArray) {
        int i = 0;
        for (char singleChar : charArray) {
            charImgArray[i] = CharRenderer.getImg(singleChar, CHAR_PIXELS, font);
            i++;
        }
    }

    /**
     * calculates each char brightness level
     *
     * @param charBrightness array that holds the chars' brightness levels
     * @param charImgArray   array that holds the chars
     */
    private void calculateCharBrightness(double[] charBrightness, boolean[][][] charImgArray) {
        int i = 0;
        double brightPixels = 0;
        for (boolean[][] charImg : charImgArray) {
            for (boolean[] row : charImg) {
                for (boolean value : row) {
                    if (value) {
                        brightPixels++;
                    }
                }
            }
            charBrightness[i] = brightPixels / (PIXELS_IN_IMAGE);
            brightPixels = 0;
            i++;
        }
    }

    /**
     * the function receives the brightness level of the Ascii chars and normalizes it in order to make the
     * differences easier to see in the final picture
     *
     * @param charBrightness a list containing the brightness level of the chars
     * @return the brightness levels normalized
     */
    private double[] extendBrightness(double[] charBrightness) {
        double[] extendedBrightness = new double[charBrightness.length];
        double minBrightness = 1, maxBrightness = 0;
        for (double brightness : charBrightness) {
            if (brightness > maxBrightness) {
                maxBrightness = brightness;
            }
            if (brightness < minBrightness) {
                minBrightness = brightness;
            }
        }
        for (int i = 0; i < charBrightness.length; i++) {
            extendedBrightness[i] = (charBrightness[i] - minBrightness) / (maxBrightness - minBrightness);
        }
        return extendedBrightness;
    }

    /**
     * the function transforms each sub image to grey and checks the total brightness of the img
     *
     * @param img the img to check the brightness of
     */
    private void getAverageBrightness(Image img) {
        double sum = 0, numOfPixels = 0;
        for (Color pixel : img.pixels()) {
            sum += (pixel.getRed() * RED_RATIO + pixel.getGreen() * GREEN_RATIO + pixel.getBlue() *
                    BLUE_RATIO) / PIXELS_IN_IMAGE;
            numOfPixels++;
        }
        cache.put(img, sum / numOfPixels);
    }

    /**
     * calculates the sub images brightness and adds them to the cache each time the resolution changes
     *
     * @param numCharsInRow number of chars in each row. represents the resolution
     */
    private void calculateImageBrightness(int numCharsInRow) {
        int pixels = img.getWidth() / numCharsInRow;
        for (Image subImage : img.squareSubImagesOfSize(pixels)) {
            getAverageBrightness(subImage);
        }
    }
}
