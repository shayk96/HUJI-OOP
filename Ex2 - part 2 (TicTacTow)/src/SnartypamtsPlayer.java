/**
 * the class of the Snartypamts player type
 */
public class SnartypamtsPlayer implements Player {

    /**
     * makes a move in the name of the Snartypamts player.
     *
     * @param board the game board
     * @param mark  the player mark
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        int boardSize = Board.SIZE;
        for (int col = 1; col < boardSize; col++) {
            for (int row = 0; row <= boardSize; row++) {
                if (board.putMark(mark, row, col)) {
                    return;
                }
            }
        }
    }

}