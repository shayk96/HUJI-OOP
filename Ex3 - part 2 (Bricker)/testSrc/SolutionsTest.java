import org.junit.jupiter.api.Test;
import algo_questions.Solutions;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <B>Tests for the Solutions Class,</B>
 * featured in Exercise 3 part 2 of the new "Introduction to OOP" course,
 * HUJI, Winter 2021-2022 Semester.
 *
 * @author Erel Debel.
 * <p>
 * Special thanks to Roi for his alotStudyTime cases and to Rani for his minLeap tips.
 */
class SolutionsTest {

	/**
	 * Tests for the alotStudyTime algorithm.
	 */
	@Test
	public void alotStudyTimeTest() {
		assertEquals(3, Solutions.alotStudyTime(new int[]{1,1,1,1,1}, new int[]{1,1,1}));
		assertEquals(3, Solutions.alotStudyTime(new int[]{1,1,1}, new int[]{1,1,1,1,1}));
		assertEquals(0, Solutions.alotStudyTime(new int[]{2, 3, 1}, new int[]{}));
		assertEquals(0, Solutions.alotStudyTime(new int[]{}, new int[]{1, 3, 2}));
		assertEquals(3, Solutions.alotStudyTime(new int[]{2, 3, 1}, new int[]{1, 3, 2}));

		assertEquals(1, Solutions.alotStudyTime(new int[]{2, 3, 2}, new int[]{1, 7, 1}),
				"One could use 7, the other two will be left out");
		assertEquals(4, Solutions.alotStudyTime(new int[]{1,7,13,9,2,5,15}, new int[]{2,2,4,6,6,10}),
				"Example: 9->10, 5->6, 2->2, 1->2");
		assertEquals(5, Solutions.alotStudyTime(new int[]{2,5,3,4,9,13,9}, new int[]{1,7,3,2,1,1,10,15}),
				"Example: 13->15, 9->10, 5->7, 3->3, 2->2");
	}

	/**
	 * Tests for the minLeap algorithm.
	 */
	@Test
	public void minLeapTest() {
		assertEquals(0, Solutions.minLeap(new int[]{3}), "No leaps needed as n-1.");
		assertEquals(3, Solutions.minLeap(new int[]{1, 1, 1, 1}), "Only Option: 1,2,3,4");
		assertEquals(2, Solutions.minLeap(new int[]{1, 2, 1, 1}), "Best: 1,2,4");
		assertEquals(3, Solutions.minLeap(new int[]{1, 4, 6, 3, 1, 1, 1, 1, 1}), "Example: 1,2,3,9");
		assertEquals(3, Solutions.minLeap(new int[]{8, 4, 5, 8, 1, 5, 5, 2, 1, 1, 3, 1, 10}),
				"Example: 1,4,11,13");
		int[] depthArray = new int[100000000];
		Arrays.fill(depthArray, 1);
		assertEquals(depthArray.length - 1, Solutions.minLeap(depthArray),
				"Did you use recursion? keep in mind that recursive calls cost plenty of memory.");
		depthArray[124215] = 38;
		depthArray[89466461] = 1254;
		assertEquals(depthArray.length - depthArray[124215] - depthArray[89466461] + 1,
				Solutions.minLeap(depthArray));
	}

	/**
	 * Tests for the bucketWalk algorithm.
	 */
	@Test
	public void bucketWalkTest() {
		assertEquals(1, Solutions.bucketWalk(0));
		assertEquals(1, Solutions.bucketWalk(1));
		assertEquals(2, Solutions.bucketWalk(2));
		assertEquals(13, Solutions.bucketWalk(6));
		assertEquals(987, Solutions.bucketWalk(15));
		assertEquals(514229, Solutions.bucketWalk(28));
		assertEquals(14930352, Solutions.bucketWalk(35));
		assertEquals(1836311903, Solutions.bucketWalk(45));
	}

	/**
	 * Tests for the numTrees algorithm.
	 */
	@Test
	public void numTreesTest(){
		assertEquals(1, Solutions.numTrees(1));
		assertEquals(2, Solutions.numTrees(2));
		assertEquals(5, Solutions.numTrees(3));
		assertEquals(429, Solutions.numTrees(7));
		assertEquals(16796, Solutions.numTrees(10));
		assertEquals(9694845, Solutions.numTrees(15));
		assertEquals(1767263190, Solutions.numTrees(19));
	}

}