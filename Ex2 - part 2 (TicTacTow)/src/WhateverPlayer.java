import java.util.Random;

/**
 * a class for the Whatever player
 */
public class WhateverPlayer implements Player {

    private static final Random random = new Random();

    /**
     * makes the turn in the name of the Whatever player
     *
     * @param board the game board
     * @param mark  the player mark
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        boolean coordinatesOk = false;
        while (!coordinatesOk) {
            int row = random.nextInt(Board.SIZE);
            int col = random.nextInt(Board.SIZE);
            coordinatesOk = board.putMark(mark, row, col);
        }
    }
}
