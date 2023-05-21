/**
 * a class that runs the tournament between two players
 */
public class Tournament {

    private static final String WINNER_ANNOUNCEMENT = "=== player 1: %d | player 2: %d | Draws: %d ===\r";
    private static final String BAD_INPUT = "\"Usage: java Tournament [round count] " +
            "[render target: console/none] [player1: human/clever/whatever/snartypamts] " +
            "[player2: human/clever/whatever/snartypamts]\"";
    private static final String[] RENDER_TYPES = {"none", "console"};
    private static final String[] PLAYER_TYPES = {"human", "clever", "whatever", "snartypamts"};

    private static final int CORRECT_ARGS_COUNT = 4;
    private static final int NUMBER_OF_ROUNDS_IDX = 0;
    private static final int RENDERER_IDX = 1;
    private static final int PLAYER_1_IDX = 2;
    private static final int PLAYER_2_IDX = 3;

    private final int rounds;
    private final Renderer renderer;
    private final Player[] players;
    private final int[] tournamentStats = new int[3];

    /**
     * the main function that runs the program
     *
     * @param args a list of inputs from the user
     */
    public static void main(String[] args) {

        if (!checkArgs(args)) {
            System.out.println(BAD_INPUT);
            return;
        }

        RendererFactory renderFactory = new RendererFactory();
        PlayerFactory playerFactory = new PlayerFactory();

        Renderer render = renderFactory.buildRenderer(args[RENDERER_IDX]);
        Player[] players = {playerFactory.buildPlayer(args[PLAYER_1_IDX]),
                            playerFactory.buildPlayer(args[PLAYER_2_IDX])};
        int numberOfRounds = Integer.parseInt(args[NUMBER_OF_ROUNDS_IDX]);

        Tournament tournament = new Tournament(numberOfRounds, render, players);
        tournament.playTournament();
    }

    /**
     * a constructor for the class
     *
     * @param rounds   number of rounds to play
     * @param renderer the type of renderer
     * @param players  the players that participate in the tournament
     */
    public Tournament(int rounds, Renderer renderer, Player[] players) {
        this.rounds = rounds;
        this.renderer = renderer;
        this.players = players;
    }

    /**
     * the function updates the tournaments states after each game
     *
     * @param mark    the mark of the winner
     * @param playerX the player that plays as X
     * @param playerO the player that plays as O
     */
    private void updateStats(Mark mark, int playerX, int playerO) {
        if (mark == Mark.BLANK) {
            tournamentStats[2]++;
        }
        if (mark == Mark.X) {
            tournamentStats[playerX]++;
        }
        if (mark == Mark.O) {
            tournamentStats[playerO]++;
        }
    }

    /**
     * the function runs a single tournament
     *
     * @return the tournaments stats
     */
    public int[] playTournament() {
        for (int i = 0, j = 1; i < rounds; i++, j++) {
            Game game = new Game(players[i % 2], players[j % 2], renderer);
            updateStats(game.run(), i % 2, j % 2);
        }
        System.out.printf(WINNER_ANNOUNCEMENT, tournamentStats[0], tournamentStats[1], tournamentStats[2]);
        return tournamentStats;
    }

    /**
     * the function checks if the input from the user is valid
     *
     * @param args a list of inputs from the user
     * @return true if valid else false
     */
    private static boolean checkArgs(String[] args) {
        if (args.length != CORRECT_ARGS_COUNT) {
            return false;
        }
        if (Integer.parseInt(args[NUMBER_OF_ROUNDS_IDX]) < 0) {
            return false;
        }
        if (!checkRendererType(args[RENDERER_IDX])) {
            return false;
        }
        if (!checkPlayerType(args[PLAYER_1_IDX]) || !checkPlayerType(args[PLAYER_2_IDX])) {
            return false;
        }
        return true;
    }

    /**
     * check if the player types the user inputted are correct
     *
     * @param playerType the player type the user inputted
     * @return true if valid else false
     */
    private static boolean checkPlayerType(String playerType) {
        for (String type : PLAYER_TYPES) {
            if (type.equals(playerType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * check if the renderer type the user inputted is correct
     *
     * @param rendererType the renderer type the user inputted
     * @return true if valid else false
     */
    private static boolean checkRendererType(String rendererType) {
        for (String type : RENDER_TYPES) {
            if (type.equals(rendererType)) {
                return true;
            }
        }
        return false;
    }
}
