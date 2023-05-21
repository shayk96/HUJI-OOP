/*
  Tests for the algorithms part of Ex3_2, OOP, written by Omri Wolf.
  In order to run tests, right click on testSrc directory -> mark directory as -> test sources root.
  then, if you don't have junit downloaded already, right click on "junit" in the second import line
  and download junit 5. continue to run all tests together or individually.
  Disclaimer: Greedy algorithms tests were written and checked by hand, and can be wrong.
  All assumptions I made over the input are written as a comment at each individual test.
  If you have any questions or improvement suggestions,
  please contact me at 054-3397915 or omri.wolf1@mail.huji.ac.il.
  Good luck!
 */

// if your algo_questions directory is different, change this line.
import algo_questions.Solutions;

// download me!
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

public class testAlgo_questions {

    @Test
    public void testNumTrees() {
        // runs the method with a massive input to make sure method is not recursive.
        Solutions.numTrees(12000);
        for (int i = 1; i < 19; i++) {
            assertEquals(catalanNumber(i), Solutions.numTrees(i));
        }
    }

    /**
     * Assumptions:
     * 1. Order does matter: i.e 1 2 =\= 2 1 (1 is 1 liter filled, 2 is 2 liters filled).
     * 2. Staff asked the answer to be correct for 0 <= n <= 48 and return INT,
     *    but n = 46 overflows int max value so the algorithm returns an incorrect solution.
     *    so we are assuming 0 <= n <= 45
     */
    @Test
    public void testBucketWalk() {
        // runs the method with a massive input to make sure method is not recursive.
        Solutions.bucketWalk(12000);
        for (int i = 0; i < 46; i++) {
            assertEquals(fib(i + 1), Solutions.bucketWalk(i));
        }
    }

    /**
     * Assumptions:
     * 1. Assuming for each input, there exists a correct solution.
     * 2. Also assuming x[i] >= 0 (though algorithm should still probably work).
     */
    @Test
    public void testMinLeap() {
        int[] empty_arr = {};
        assertEquals(0, Solutions.minLeap(empty_arr));
        int[] single_arr = {1};
        for (int i = 0; i < 10; i++) {
            single_arr[0] = i;
            assertEquals(0, Solutions.minLeap(single_arr));
        }
        int[] single_jump = {5, 1000, 4, 2};
        assertEquals(1, Solutions.minLeap(single_jump));
        for (int i = 1; i < 20; i++) {
            int[] single_jumps = new int[i];
            Arrays.fill(single_jumps, 1);
            assertEquals(i - 1, Solutions.minLeap(single_jumps));
        }
        int[] anti_greedy = {4, 1, 5, 1, 1, 1, 1, 1};
        assertEquals(2, Solutions.minLeap(anti_greedy));
        int[] random_arr = {1, 3, 1, 2, 1, 3, 2, 1};
        assertEquals(4, Solutions.minLeap(random_arr));
        int[] zero_arr = {2, 0, 1, 3, 2, 1};
        assertEquals(3, Solutions.minLeap(zero_arr));
        int[] large_arr = {1, 1, 2, 4, 1, 5, 1, 1, 1, 1, 1, 1, 3, 1, 2, 6};
        assertEquals(8, Solutions.minLeap(large_arr));
    }

    /**
     * Assumptions:
     * 1. Assuming time for completing a task is positive (greater than 0).
     */
    @Test
    public void testAlotStudyTime() {
        int [] tasks = {};
        int [] timeSlots = {};
        // check empty arrays input
        assertEquals(0, Solutions.alotStudyTime(tasks, timeSlots));
        // one is empty, other is not
        tasks = new int[]{1};
        assertEquals(0, Solutions.alotStudyTime(tasks, timeSlots));
        assertEquals(0, Solutions.alotStudyTime(timeSlots, tasks));
        // no tasks can be completed
        tasks = new int[] {3, 4, 2, 7};
        timeSlots = new int[] {1, 1, 1};
        assertEquals(0, Solutions.alotStudyTime(tasks, timeSlots));
        // all tasks can be completed
        assertEquals(3, Solutions.alotStudyTime(timeSlots, tasks));
        // one task can be completed
        tasks = new int[] {5, 9, 6, 3, 2, 8, 9, 5};
        timeSlots = new int[] {2, 1, 2, 1, 2};
        assertEquals(1, Solutions.alotStudyTime(tasks, timeSlots));
        // checks for efficient placement, not placing task 5 in the time slot 6
        timeSlots = new int[] {6, 1, 4, 5, 10};
        assertEquals(4, Solutions.alotStudyTime(tasks, timeSlots));
        // 10 random generated tasks, 10 random generates timeslots
        tasks = new int[] {86, 59, 84, 97, 63, 20, 99, 94, 23, 20};
        timeSlots = new int[] {100, 89, 7, 99, 36, 5, 13, 72, 22, 69};
        assertEquals(7, Solutions.alotStudyTime(tasks, timeSlots));
    }

    private long catalanNumber(int n) {
        long catalan = 1;
        for (int i = 1; i <= n; i++) {
            catalan *= (4L * i - 2);
            catalan /= (i + 1);
        }
        return catalan;
    }

    private int fib(int n) {
        double Phi = (1 + Math.sqrt(5)) / 2;
        double phi = 1 - Phi;
        return (int) ((Math.pow(Phi, n) - Math.pow(phi, n)) / Math.sqrt(5));
    }
}
