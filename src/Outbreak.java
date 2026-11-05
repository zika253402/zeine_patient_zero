import java.util.*;

/**
 * Mission 5. Dynamic programming on the tree of infections. Root the tree in person 0.
 * Input.Tree.neighbours() gives the neighbours of every person. The number of people is at most about 100, so a recursive DFS is fine here.
 */
public class Outbreak {

    /**
     * Choose people to test so that for every link (somebody infected somebody) at least one of the two is tested,
     * and the total cost of testing is as small as possible. cost[v] is the price of testing person v.
     * Returns tested[v] = true if person v is tested.
     * <p>
     * State for every person v (looking only at v and everything below it): the smallest cost when v IS tested, and
     * the smallest cost when v is NOT tested. What must then be true about the children of v in each case?
     * Do not forget to rebuild the answer (who exactly is tested), not only the cost: the picture needs it.
     * Main calls this twice: with all costs equal to 1 (the line "Tests needed") and with the real costs ("Cheapest testing").
     */
    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        long[] testedCost = new long[n];
        long[] notTestedCost = new long[n];
        int[] parent = new int[n];

        Arrays.fill(parent, -1);

        calculateCosts(0, -1, nb, cost, testedCost, notTestedCost, parent);

        boolean[] tested = new boolean[n];

        if (testedCost[0] <= notTestedCost[0]) {
            rebuild(0, -1, true, nb, testedCost, notTestedCost, tested);
        } else {
            rebuild(0, -1, false, nb, testedCost, notTestedCost, tested);
        }

        return tested;
    }

    private static void calculateCosts(
            int v,
            int parent,
            int[][] nb,
            int[] cost,
            long[] testedCost,
            long[] notTestedCost,
            int[] parents) {

        parents[v] = parent;

        testedCost[v] = cost[v];
        notTestedCost[v] = 0;

        for (int child : nb[v]) {
            if (child == parent) {
                continue;
            }

            calculateCosts(child, v, nb, cost,
                    testedCost, notTestedCost, parents);

            testedCost[v] += Math.min(
                    testedCost[child],
                    notTestedCost[child]
            );

            notTestedCost[v] += testedCost[child];
        }
    }


    private static void rebuild(
            int v,
            int parent,
            boolean isTested,
            int[][] nb,
            long[] testedCost,
            long[] notTestedCost,
            boolean[] result) {

        result[v] = isTested;

        for (int child : nb[v]) {
            if (child == parent) {
                continue;
            }

            boolean childTested;

            if (!isTested) {
                childTested = true;
            } else {
                childTested = testedCost[child] <= notTestedCost[child];
            }

            rebuild(child, v, childTested, nb,
                    testedCost, notTestedCost, result);
        }
    }

    /**
     * The number of links on the longest chain of infections, that is the longest path in the tree (counted in links).
     * A path goes up from one person and down to another; for every person you need the two longest ways down.
     * Example: a line 0-1-2-3 has chain 3; a star with centre 0 and leaves 1, 2, 3 has chain 2.
     */
    public static int longestChain(int n, int[][] nb) {
        int[] answer = new int[1];

        longestDfs(0, -1, nb, answer);

        return answer[0];
    }

    private static int longestDfs(
            int v,
            int parent,
            int[][] nb,
            int[] answer) {

        int longest = 0;
        int secondLongest = 0;

        for (int child : nb[v]) {
            if (child == parent) {
                continue;
            }

            int distance = longestDfs(child, v, nb, answer) + 1;

            if (distance > longest) {
                secondLongest = longest;
                longest = distance;
            } else if (distance > secondLongest) {
                secondLongest = distance;
            }
        }

        answer[0] = Math.max(answer[0], longest + secondLongest);

        return longest;
    }
}
