import static org.junit.jupiter.api.Assertions.*;

/**
 * <B>Tests for the Board Class,</B>
 * featured in Exercise 2 of the new "Introduction to OOP" course,
 * HUJI, Winter 2021-2022 Semester.
 *
 * @author Erel Debel.
 */
public class BoardTest {
	private Board board;

	/**
	 * Checks that a board is created with all marks Blank.
	 */
	@org.junit.jupiter.api.Test
	void checkInitialization() {
		board = new Board();
		for (int row = 0; row < Board.SIZE; row++) {
			for (int col = 0; col < Board.SIZE; col++) {
				assertEquals(Mark.BLANK, board.getMark(row, col));
			}
		}
	}

	/**
	 * Checks the PutMark and GetMark work using the same indices.
	 */
	@org.junit.jupiter.api.Test
	void checkPutMarkAndGetMarkSynchronization() {
		board = new Board();
		Mark[] marks = {Mark.X, Mark.O};
		for (int row = 0; row < Board.SIZE; row++) {
			for (int col = 0; col < Board.SIZE; col++) {
				board.putMark(marks[(row + 3 * col) % 2], row, col);
			}
		}
		for (int row = 0; row < Board.SIZE; row++) {
			for (int col = 0; col < Board.SIZE; col++) {
				assertEquals(marks[(row + 3 * col) % 2], board.getMark(row, col));
			}
		}
	}

	/**
	 * Checks that you can't override a placed mark.
	 */
	@org.junit.jupiter.api.Test
	void checkMarksCanNotChange() {
		board = new Board();
		assertTrue(board.putMark(Mark.X, 1, 0));
		assertFalse(board.putMark(Mark.X, 1, 0));
		assertFalse(board.putMark(Mark.O, 1, 0));
		assertEquals(Mark.X, board.getMark(1, 0));
		assertTrue(board.putMark(Mark.O, 0, 1));
		assertFalse(board.putMark(Mark.O, 0, 1));
		assertFalse(board.putMark(Mark.X, 0, 1));
		assertEquals(Mark.O, board.getMark(0, 1));
	}

	/**
	 * Checks that putMark returns false for coordinates out of range and does not mark them.
	 */
	@org.junit.jupiter.api.Test
	void checkPutAndGetMarkOutOfRange() {
		checkPutMarkOutOfRangeWithMark(Mark.X);
		checkPutMarkOutOfRangeWithMark(Mark.O);
	}

	private void checkPutMarkOutOfRangeWithMark(Mark mark) {
		board = new Board();
		assertFalse(board.putMark(mark, -1, 0));
		assertFalse(board.putMark(mark, -2, 0));
		assertFalse(board.putMark(mark, 0, -1));
		assertFalse(board.putMark(mark, 0, -2));
		assertFalse(board.putMark(mark, -1, -1));
		assertFalse(board.putMark(mark, Board.SIZE, 0));
		assertFalse(board.putMark(mark, Board.SIZE + 1, 0));
		assertFalse(board.putMark(mark, 0, Board.SIZE));
		assertFalse(board.putMark(mark, 0, Board.SIZE + 1));
		assertFalse(board.putMark(mark, Board.SIZE, Board.SIZE));
		assertFalse(board.putMark(mark, -1, Board.SIZE));
		assertFalse(board.putMark(mark, Board.SIZE, -1));

		assertEquals(Mark.BLANK, board.getMark(-1, 0));
		assertEquals(Mark.BLANK, board.getMark(-2, 0));
		assertEquals(Mark.BLANK, board.getMark(0, -1));
		assertEquals(Mark.BLANK, board.getMark(0, -2));
		assertEquals(Mark.BLANK, board.getMark(-1, -1));
		assertEquals(Mark.BLANK, board.getMark(Board.SIZE, 0));
		assertEquals(Mark.BLANK, board.getMark(Board.SIZE + 1, 0));
		assertEquals(Mark.BLANK, board.getMark(0, Board.SIZE));
		assertEquals(Mark.BLANK, board.getMark(0, Board.SIZE + 1));
		assertEquals(Mark.BLANK, board.getMark(Board.SIZE, Board.SIZE));
		assertEquals(Mark.BLANK, board.getMark(-1, Board.SIZE));
		assertEquals(Mark.BLANK, board.getMark(Board.SIZE, -1));
	}

	/**
	 * Checks that gameEnded does not initialize to True.
	 */
	@org.junit.jupiter.api.Test
	void checkGameIsNotFinishedAtStart() {
		board = new Board();
		assertFalse(board.gameEnded());
	}

	/**
	 * Checks that X can win.
	 */
	@org.junit.jupiter.api.Test
	void checkXWin() {
		checkWin(Mark.X);
	}

	/**
	 * Checks that O can win.
	 */
	@org.junit.jupiter.api.Test
	void checkOWin() {
		checkWin(Mark.O);
	}

	void checkWin(Mark mark) {
		if ((Board.WIN_STREAK != 3) && (Board.SIZE < 4)) {
			fail("This test assumes a win streak of 3 and a size of at least 4.");
			return;
		}
		// scenario 1: horizontal
		board = new Board();
		for (int i = 0; i < 3; i++) {
			assertFalse(board.gameEnded());
			board.putMark(mark, 3, 1 + i);
		}
		assertEquals(mark, board.getWinner());
		// scenario 2: vertical, length 4
		board = new Board();
		assertFalse(board.gameEnded(), "End boolean may unwantedly be static.");
		int[] puttingOrder = {4, 2, 1, 3};
		for (int i = 0; i < 4; i++) {
			assertFalse(board.gameEnded());
			board.putMark(mark, puttingOrder[i], 1);
		}
		assertEquals(mark, board.getWinner());
		// scenario 3: first diagonal
		board = new Board();
		for (int i = 0; i < 3; i++) {
			assertFalse(board.gameEnded());
			board.putMark(mark, 1 + i, i);
		}
		assertEquals(mark, board.getWinner());
		// scenario 4: second diagonal
		board = new Board();
		for (int i = 0; i < 3; i++) {
			assertFalse(board.gameEnded());
			board.putMark(mark, 3 - i, 1 + i);
		}
		assertEquals(mark, board.getWinner());
	}

	/**
	 * Checks that a draw happens when the board is full with no winning streaks.
	 */
	@org.junit.jupiter.api.Test
	void CheckDraw() {
		board = new Board();
		if (Board.WIN_STREAK < 3) {
			fail("Can't achieve a draw with a win streak smaller than 3.");
			return;
		}
		Mark[] marks = {Mark.X, Mark.X, Mark.O, Mark.O};
		for (int row = 0; row < Board.SIZE; row++) {
			for (int col = 0; col < Board.SIZE; col++) {
				board.putMark(marks[(2 * row + col) % 4], row, col);
				if ((row == Board.SIZE - 1) && (col == Board.SIZE - 1)) {
					assertEquals(Mark.BLANK, board.getWinner(),
							"Board filled without a win but did not indicate a draw.");
					return;
				}
				assertFalse(board.gameEnded());
			}
		}
	}
}