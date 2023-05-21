package algo_tests;

import org.junit.jupiter.api.Test;
import static algo_questions.Solutions.*;
import static algo_tests.TestSolutions.FuncName.*;

/**
 * Tests for the second part of Ex3_2 in Oop Winter 2022
 * You're welcomed to email me for any questions: ohad.klein@mail.huji.ac.il
 * @author Ohad Klein
 */
public class TestSolutions {

    enum FuncName {ALOT_STUDY_TIME, MIN_LEAP, BUCKET_WALK, NUM_TREES}

    static class Problem {
        int[][] assumptions;
        int solution;
        public Problem(int[][] assumptions, int solution) {
            this.assumptions = assumptions;
            this.solution = solution;
        }

        public Problem(int[] assumption, int solution) {
            this(new int[][]{assumption}, solution);
        }

        public Problem(int assumption, int solution) {
            this(new int[][]{{assumption}}, solution);
        }

        public boolean solve(FuncName funcName) {
            switch (funcName) {
                case ALOT_STUDY_TIME:return alotStudyTime(assumptions[0], assumptions[1]) == solution;
                case MIN_LEAP: return minLeap(assumptions[0]) == solution;
                case BUCKET_WALK: return bucketWalk(assumptions[0][0]) == solution;
                case NUM_TREES: return numTrees(assumptions[0][0]) == solution;
                default: return false;
            }
        }
    }

    @Test
    public void testAlotStudyTime() {
        Problem[] problems = {
                new Problem(new int[][]{
                        {4, 3, 6, 2},   // tasks
                        {5, 4, 3, 1}},  // timeSlots
                        3       // solution
                ),
                new Problem(new int[][]{
                        {5, 3, 9, 6, 5, 3, 8},      // tasks
                        {8, 6, 2, 4, 5, 7, 1, 6}},  // timeSlots
                        6                   // solution
                ),
                new Problem(new int[][]{
                        {1, 2},     // tasks
                        {2, 1}},    // timeSlots
                        2   // solution
                ),
                new Problem(new int[][]{
                        {76, 1, 4, 69, 40, 3, 5, 53, 79, 83}, // tasks
                        {92, 65, 64, 15, 58, 59, 13, 77, 72, 49}}, // timeSlots
                        9   // solution
                )
        };
        for (Problem problem : problems) {
            assert problem.solve(ALOT_STUDY_TIME);
        }
    }

    @Test
    public void testMinLeap() {
        Problem[] problems = {
                new Problem(new int[]
                        {3, 5, 7, 2, 4, 3, 3, 4, 1, 1},                   // leapNum
                        2                                          // solution
                ),
                new Problem(new int[]
                        {1, 1, 3, 5, 1, 1, 1, 4, 1, 1, 1, 2, 3, 1, 2, 6}, // leapNum
                                                                          // solution
                        7),
                new Problem(new int[]
                        {9, 5, 7, 2, 4, 3, 3, 4, 1, 1},                   // leapNum
                        1                                          // solution
                ),
                new Problem(new int[]
                        {2, 3, 1, 1, 8000},                               // leapNum
                        2                                          // solution
                ),
                new Problem(new int[]
                        {2, 1},                                           // leapNum
                        1                                          // solution
                ),
                new Problem(new int[]
                        {1, 2},                                           // leapNum
                        1                                          // solution
                ),
                new Problem(new int[]
                        {1},                                              // leapNum
                        0                                          // solution
                ),
                new Problem(new int[]
                        {1,1,1,1,1,1,1,1,1,1},                            // leapNum
                        9                                          // solution
                )
        };
        for (Problem problem : problems) {
            assert problem.solve(MIN_LEAP);
        }
    }

    @Test
    public void testBucketWalk() {
        Problem[] problems = {
                new Problem(0, 1),
                new Problem(1, 1),
                new Problem(2, 2),
                new Problem(10, 89),
                new Problem(4, 5),
                new Problem(5, 8),
                new Problem(6, 13),
                new Problem(9, 55),
        };
        for (Problem problem : problems) {
            assert problem.solve(BUCKET_WALK);
        }
    }

    @Test
    public void testNumTrees() {
        Problem[] problems = {
                new Problem(1, 1),
                new Problem(2, 2),
                new Problem(3, 5),
                new Problem(4, 14),
                new Problem(5, 42),
                new Problem(6, 132),
                new Problem(7, 429),
                new Problem(8, 1430),
                new Problem(9, 4862),
                new Problem(10, 16796),
                new Problem(11, 58786),
                new Problem(12, 208012),
                new Problem(13, 742900),
                new Problem(14, 2674440),
                new Problem(15, 9694845),
                new Problem(16, 35357670),
                new Problem(17, 129644790),
                new Problem(18, 477638700),
                new Problem(19, 1767263190)
        };
        for (Problem problem : problems) {
            assert problem.solve(NUM_TREES);
        }
    }

    @Test
    public void testAll() {
        testAlotStudyTime();
        testMinLeap();
        testBucketWalk();
        testNumTrees();
    }
}
