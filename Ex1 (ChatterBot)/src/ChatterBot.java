import java.util.*;

/**
 * Base file for the ChatterBot exercise.
 * The bot's replyTo method receives a statement.
 * If it starts with the constant REQUEST_PREFIX, the bot returns
 * whatever is after this prefix. Otherwise, it returns one of
 * a few possible replies as supplied to it via its constructor.
 * In this case, it may also include the statement after
 * the selected reply (coin toss).
 *
 * @author Dan Nirel
 */
class ChatterBot {
    static final String REQUEST_PREFIX = "say ";
    static final String REQUESTED_PHRASE_PLACEHOLDER = "<phrase>";
    static final String ILLEGAL_REQUEST_PLACEHOLDER = "<request>";

    Random rand = new Random();
    String[] repliesToIllegalRequest;
    String[] repliesToLegalRequest;
    String name;

    /**
     * Constructor for the class. creates a bot and saves the possible answers and the bots name.
     * @param name name of the bot.
     * @param repliesToLegalRequest possible answers to a legal request.
     * @param repliesToIllegalRequest possible answers to illegal request.
     */
    ChatterBot(String name, String[] repliesToLegalRequest, String[] repliesToIllegalRequest) {
        this.name = name;
        this.repliesToIllegalRequest = new String[repliesToIllegalRequest.length];
        for (int i = 0; i < repliesToIllegalRequest.length; i++) {
            this.repliesToIllegalRequest[i] = repliesToIllegalRequest[i];
        }
        this.repliesToLegalRequest = new String[repliesToLegalRequest.length];
        for (int i = 0; i < repliesToLegalRequest.length; i++) {
            this.repliesToLegalRequest[i] = repliesToLegalRequest[i];
        }
    }

    /**
     * A getter for the bot name.
     * @return the name of the bot.
     */
    String getName() {
        return name;
    }

    /**
     * Cheacks if the statement starts with the prefix, calls a helper function and returns the right answer.
     * @param statement the word to reply to.
     * @return the bots answer.
     */
    String replyTo(String statement) {
        if (statement.startsWith(REQUEST_PREFIX)) {
            String phrase = statement.replaceFirst(REQUEST_PREFIX, "");
            return replacePlaceholderInARandomPattern(repliesToLegalRequest, REQUESTED_PHRASE_PLACEHOLDER, phrase);
        }
        return replacePlaceholderInARandomPattern(repliesToIllegalRequest, ILLEGAL_REQUEST_PLACEHOLDER, statement);
    }

    /**
     * Handles the replacement of the placeholder.
     * @param possibleAnswers a list of possible answers
     * @param placeHolder the placeholder inside the answer to change
     * @param replacement the string to replace with
     * @return the bots answer after it replaced the placeholder
     */
    String replacePlaceholderInARandomPattern(String[] possibleAnswers, String placeHolder, String replacement) {
        int randomIndex = rand.nextInt(possibleAnswers.length);
        String responsePattern = possibleAnswers[randomIndex];
        return responsePattern.replaceAll(placeHolder, replacement);
    }
}
