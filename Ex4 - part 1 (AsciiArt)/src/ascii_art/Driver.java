package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.AsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;


public class Driver {

    private static final Character[] charSet = {'q','w','e','r','t','y','u','i','o','p','a','s','d','f','g',
            'h','j','k','l','z','x','c','v','b','n','m',' ','#','$'};

    public static void main(String[] args) {
        Image img = Image.fromFile("07.jpeg");
        BrightnessImgCharMatcher charMatcher = new BrightnessImgCharMatcher(img, "Courier New");
        AsciiOutput asciiOutput = new HtmlAsciiOutput("output512.html", "Courier New");
        char[][] chars = charMatcher.chooseChars(512, charSet);
        asciiOutput.output(chars);
    }
}
