/**
 * an interface for the different player types
 */
public interface Player {

    /**
     * the function that plays in the name of the diffrent payer types, if the player type is human asks
     * for input.
     *
     * @param board the game board
     * @param mark the player mark
     */
    void playTurn(Board board, Mark mark);

}
