import java.util.Scanner;


/**
 * a class that creates a player and makes turns
 */
public class Player {

    private static final String INVALID_INPUT = "Invalid input, please insert an integer";
    private static final String OCCUPIED_OFFBOARD= "Index out of grid or tile occupied, please choose an " +
                                                    "open tile on the grid";
    /**
     * a constructor for the class
     */
    public Player() {
    }

    /**
     * receives input from the player, checks if it's valid, calculates the index representation according to
     * the program and passes it onto the board
     *
     * @param board the game board
     * @param mark the players mark
     */
    public void playTurn(Board board, Mark mark) {
        Scanner input = new Scanner(System.in);
        boolean coordinatesOk = false;
        while (!coordinatesOk) {
            while (!input.hasNextInt()) {
                System.out.println(INVALID_INPUT);
                input.next();
            }
            int num = input.nextInt();
            int row = num / 10 - 1;
            int col = num % 10 - 1;
            coordinatesOk = board.putMark(mark, row, col);
            if (!coordinatesOk){
                System.out.println(OCCUPIED_OFFBOARD);
            }
        }
    }
}
