package algo_questions;


import java.util.Arrays;

public class Solutions {

    /**
     * Method computing the solution to the following problem: A boy is filling the water trough for his
     * father's cows in their village. The trough holds n liters of water. With every trip to the village
     * well, he can return using either the 2 bucket yoke, or simply with a single bucket. A bucket holds 1
     * liter. In how many different ways can he fill the water trough? n can be assumed to be greater or
     * equal to 0, less than or equal to 48.
     *
     * @param n - integer representing number of litters needed
     * @return how many ways can the boy fill the water trough.
     */
    public static int bucketWalk(int n) {
        int[] solutionTable = new int[n + 2];
        solutionTable[0] = 0;
        solutionTable[1] = 1;
        for (int i = 2; i <= n + 1; i++) {
            solutionTable[i] = solutionTable[i - 2] + solutionTable[i - 1];
        }
        return solutionTable[n + 1];
    }

    /**
     * Method computing the solution to the following problem: Given an integer n, return the number of
     * structurally unique BST's (binary search trees) which has exactly n nodes of unique values from 1 to n.
     *
     * @param n number of nodes in the BST
     * @return number of structurally unique BST's
     */
    public static int numTrees(int n) {
        int[] solutionTable = new int[n + 1];
        solutionTable[0] = 1;
        solutionTable[1] = 1;
        for (int i = 2; i <= n; i++) {
            int sum = 0;
            for (int j = 0, k = i - 1; k >= 0; j++, k--) {
                sum += solutionTable[j] * solutionTable[k];
            }
            solutionTable[i] = sum;
        }
        return solutionTable[n];
    }

    /**
     * Method computing the nim amount of leaps a frog needs to jumb across n waterlily leaves, from leaf 1
     * to leaf n. The leaves vary in size and how stable they are, so some leaves allow larger leaps
     * than others. leapNum[i] is an integer telling you how many leaves ahead you can jump from leaf i.
     * If leapNum[3]=4, the frog can jump from leaf 3, and land on any of the leaves 4, 5, 6 or 7.
     *
     * @param leapNum - array of ints. leapNum[i] is how many leaves ahead you can jump from leaf i
     * @return minimal no. of leaps to last leaf.
     */
    public static int minLeap(int[] leapNum) {
        int numberOfJumps = 0;
        int currentLeef = leapNum.length - 1;
        if (leapNum.length == 0) {
            return 0;
        }
        for (int i = 0; i < currentLeef; i++) {
            if (i + leapNum[i] >= currentLeef) {
                numberOfJumps++;
                currentLeef = i;
                i = -1;
            }
        }
        return numberOfJumps;
    }

    /**
     * Method computing the maximal amount of tasks out of n tasks that can be completed with m time slots.
     * A task can only be completed in a time slot if the length of the time slot is grater than the no. of
     * hours needed to complete the task.
     *
     * @param tasks-    array of integers of length n. tasks[i] is the time in hours required to complete task i.
     * @param timeSlots - array of integersof length m. timeSlots[i] is the length in hours of the slot i.
     * @return maximal amount of tasks that can be completed
     */
    public static int alotStudyTime(int[] tasks, int[] timeSlots) {
        Arrays.sort(tasks);
        Arrays.sort(timeSlots);
        int ans = 0;
        int timeSlot = 0;
        if (timeSlots.length == 0 || tasks.length == 0) {
            return ans;
        }
        for (int i = 0; i < tasks.length; i++) {
            if (tasks[i] <= timeSlots[timeSlot]) {
                ans++;
                timeSlot++;
                if (timeSlot == timeSlots.length) {
                    break;
                }
                continue;
            }
            timeSlot++;
            if (timeSlot == timeSlots.length) {
                break;
            }
            i--;
        }
        return ans;
    }

}
