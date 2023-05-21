import java.util.Scanner;


/**
 * a class that creates a player and makes turns
 */
public class HumanPlayer implements Player {

    private static final String INVALID_INPUT = "Invalid coordinates, type again: ";
    private static final String ASK_FOR_COORDINATES = "Player " + "%s" + ", type coordinates: ";


    /**
     * receives input from the player, checks if it's valid, calculates the index representation according to
     * the program and passes it onto the board
     *
     * @param board the game board
     * @param mark  the players mark
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        Scanner input = new Scanner(System.in);
        boolean coordinatesOk = false;
        System.out.printf(ASK_FOR_COORDINATES, mark);
        while (!coordinatesOk) {
            while (!input.hasNextInt()) {
                System.out.println(INVALID_INPUT);
                input.next();
            }
            int num = input.nextInt();
            int row = num / 10 - 1;
            int col = num % 10 - 1;
            coordinatesOk = board.putMark(mark, row, col);
            if (!coordinatesOk) {
                System.out.println(INVALID_INPUT);
            }
        }
    }
}
