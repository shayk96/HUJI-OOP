
/**
 * a class that handles the board. keeps the boarded updated and places marks according to the player.
 * knows if the game ended and who won or if it's a tie.
 */
public class Board {


    public static final int SIZE = 6;
    public static final int WIN_STREAK = 5;

    private static final int[][][] directions = {{{1, 0}, {-1, 0}}, {{1, -1}, {-1, 1}},
                                                 {{1, 1}, {-1, -1}}, {{0, -1}, {0, 1}}};

    private final Mark[][] board = new Mark[SIZE][SIZE];
    private int freeTiles = SIZE * SIZE;
    private boolean gameEnded = false;
    private Mark winner = Mark.BLANK;


    /**
     * a constructor for the class. initializes an empty board
     */
    public Board() {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col] = Mark.BLANK;
            }
        }
    }

    /**
     * returns the mark on the board in the coordinates given
     *
     * @param row the index of the row to get the mark from
     * @param col the index of the collum to get the mark from
     * @return the mark in the row and collum given
     */
    public Mark getMark(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            return Mark.BLANK;
        }
        if (board[row][col] == Mark.X)
            return Mark.X;
        if (board[row][col] == Mark.O) {
            return Mark.O;
        }
        return Mark.BLANK;
    }

    /**
     * receives a mark and coordinates and insert the mark in the correct place on the board according to the
     * coordinates
     *
     * @param mark the mark to put on the board
     * @param row  the index of the row to put the mark on
     * @param col  the index of the collum to put the mark on
     * @return true if successful else false
     */
    public boolean putMark(Mark mark, int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            return false;
        }
        if (getMark(row, col) != Mark.BLANK) {
            return false;
        }
        board[row][col] = mark;
        freeTiles--;
        isGameOver(row, col, mark);
        return true;
    }

    /**
     * a getter for the state of the game
     *
     * @return true if the game ended else false
     */
    public boolean gameEnded() {
        return gameEnded;
    }

    /**
     * a getter for the winner of the game
     *
     * @return the mark of the player that won or blank if a tie
     */
    public Mark getWinner() {
        return winner;
    }


    /**
     * receives a mark and coordinates and checks if the player has won the game or a tie has been reached
     * and updates the winner and game_ended variables
     *
     * @param row  the index of the row to start the check
     * @param col  the index of the collum to start the check
     * @param mark the mark to check if won
     */
    private void isGameOver(int row, int col, Mark mark) {
        int counter = 0;
        for (int[][] direction : directions) {
            for (int[] halfDirection : direction) {
                counter += countMarkInDirection(row, col, halfDirection[0], halfDirection[1], mark);
            }
            if (counter >= WIN_STREAK) {
                winner = mark;
                gameEnded = true;
                return;
            }
            counter = 0;
        }
        if (freeTiles == 0) {
            gameEnded = true;
        }
    }


    /**
     * receives a place on the board and checks for streaks of the mark received in every direction
     *
     * @param row       the index of the row to start the check
     * @param col       the index of the collum to start the check
     * @param row_delta the index of the row direction to go
     * @param col_delta the index of the collum direction to go
     * @param mark      the mark to check a streak of
     * @return the streak number of the mark
     */
    private int countMarkInDirection(int row, int col, int row_delta, int col_delta, Mark mark) {
        int count = 0;
        while (row < SIZE && row >= 0 && col < SIZE && col >= 0 && board[row][col] == mark) {
            count++;
            row += row_delta;
            col += col_delta;
        }
        return count;
    }


}
