/**
 * a class that creates a clever player and makes turns
 */
public class CleverPlayer implements Player {


    /**
     * makes a move in the name of the clever player.
     *
     * @param board the game board
     * @param mark  the players mark
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        int boardSize = Board.SIZE;
        for (int row = 0; row <= boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (board.putMark(mark, row, col)) {
                    return;
                }
            }
        }
    }
}


