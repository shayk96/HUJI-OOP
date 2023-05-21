public class Game {

    private static final String WINNER_ANNOUNCEMENT = "The winner is: ";
    private static final String NO_WINNER = "No winner this time";

    public static void main(String[] args) {

        Renderer render = new Renderer();
        Player playerX = new Player();
        Player playerO = new Player();
        Game game = new Game(playerX, playerO, render);
        Mark winner = game.run();
        if (winner != Mark.BLANK) {
            System.out.println(WINNER_ANNOUNCEMENT + winner);
        } else {
            System.out.println(NO_WINNER);
        }
    }

    Player[] players;
    Renderer renderer;
    static final Mark[] marks = {Mark.X, Mark.O};

    /**
     * the constructor the class game, receives two players and a renderer
     *
     * @param playerX  the first player
     * @param playerO  the second player
     * @param renderer the renderer
     */
    public Game(Player playerX, Player playerO, Renderer renderer) {
        players = new Player[]{playerX, playerO};
        this.renderer = renderer;
    }

    /**
     * runs a single game of TicTacTow
     *
     * @return the mark of the winner if there is one else a blank mark
     */
    public Mark run() {
        Board board = new Board();
        int turnsCounter = 0;
        renderer.renderBoard(board);
        while (!board.gameEnded()) {
            int turn = turnsCounter % 2;
            players[turn].playTurn(board, marks[turn]);
            renderer.renderBoard(board);
            turnsCounter++;
        }
        return board.getWinner();
    }
}
