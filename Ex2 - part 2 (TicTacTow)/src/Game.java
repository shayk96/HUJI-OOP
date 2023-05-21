
/**
 * a class that creates and runs a single game.
 */
public class Game {


    private final Player[] players = new Player[2];
    private final Renderer renderer;
    static final Mark[] marks = {Mark.X, Mark.O};


    /**
     * the constructor the class game, receives two players and a renderer
     *
     * @param playerX  the first player
     * @param playerO  the second player
     * @param renderer the renderer
     */
    public Game(Player playerX, Player playerO, Renderer renderer) {
        players[0] = playerX;
        players[1] = playerO;
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
