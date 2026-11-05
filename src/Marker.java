import java.util.*;

/**
 * Missions 3 and 4. Find a word in a long text.
 */
public class Marker {

    /**
     * Mission 3a. The prefix function of p. pi[i] = the length of the longest proper prefix of p[0..i] that is
     * also a suffix of p[0..i] ("proper" means shorter than p[0..i] itself).
     * Example: p = "ACGAC" gives [0, 0, 0, 1, 2]. The last number is the "Border length" line.
     */
    public static int[] prefixFunction(String p) {
        int[] pi = new int[p.length()];

        for (int i = 1; i < p.length(); i++) {
            int j = pi[i - 1];

            while (j > 0 && p.charAt(i) != p.charAt(j)) {
                j = pi[j - 1];
            }

            if (p.charAt(i) == p.charAt(j)) {
                j++;
            }

            pi[i] = j;
        }

        return pi;
    }

    /**
     * Mission 3b. KMP. All start positions (counting from 0, in increasing order) where p occurs in text.
     * Overlapping occurrences count: "ACG" occurs in "ACGACG" at 0 and 3, and "AA" in "AAA" at 0 and 1.
     * Use the prefix function: the text pointer never goes back. The number of positions is "Marker hits",
     * the first one is "First hit" (-1 when there is none).
     */
    public static int[] findAll(String text, String p) {
        if (p.length() == 0) {
            return new int[0];
        }

        int[] pi = prefixFunction(p);
        ArrayList<Integer> positions = new ArrayList<>();

        int j = 0;

        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != p.charAt(j)) {
                j = pi[j - 1];
            }

            if (text.charAt(i) == p.charAt(j)) {
                j++;
            }

            if (j == p.length()) {
                positions.add(i - p.length() + 1);
                j = pi[j - 1];
            }
        }

        int[] result = new int[positions.size()];

        for (int i = 0; i < positions.size(); i++) {
            result[i] = positions.get(i);
        }

        return result;
    }

    /**
     * Mission 4. Rabin-Karp. The number of positions i in a such that the word of k letters starting at i
     * (a[i..i+k-1]) also occurs somewhere in b. Count positions, not different words. This is the "Shared words" line.
     * The idea: give every window of k letters a number (a rolling hash, updated in O(1) when the window moves by one),
     * look the number up among the windows of b, and when it is found COMPARE THE LETTERS, because two different words
     * can have the same hash.
     * Example: a = "ACGTAC", b = "TTACGT", k = 4: the words of a are ACGT, CGTA, GTAC; only ACGT is in b, so the answer is 1.
     */
    public static int sharedWords(String a, String b, int k) {
        if (k <= 0 || a.length() < k || b.length() < k) {
            return 0;
        }

        long base = 5;
        long power = 1;

        for (int i = 1; i < k; i++) {
            power *= base;
        }

        HashMap<Long, ArrayList<Integer>> hashes = new HashMap<>();

        // Hash of the first window in B
        long hashB = 0;
        for (int i = 0; i < k; i++) {
            hashB = hashB * base + value(b.charAt(i));
        }

        hashes.putIfAbsent(hashB, new ArrayList<>());
        hashes.get(hashB).add(0);

        // Rolling hash for all other windows in B
        for (int i = 1; i <= b.length() - k; i++) {
            hashB -= value(b.charAt(i - 1)) * power;
            hashB = hashB * base + value(b.charAt(i + k - 1));

            hashes.putIfAbsent(hashB, new ArrayList<>());
            hashes.get(hashB).add(i);
        }

        int answer = 0;

        // Hash of the first window in A
        long hashA = 0;
        for (int i = 0; i < k; i++) {
            hashA = hashA * base + value(a.charAt(i));
        }

        // Check all windows in A
        for (int start = 0; start <= a.length() - k; start++) {
            if (hashes.containsKey(hashA)) {
                ArrayList<Integer> candidates = hashes.get(hashA);

                boolean found = false;

                for (int position : candidates) {
                    boolean equal = true;

                    for (int j = 0; j < k; j++) {
                        if (a.charAt(start + j) != b.charAt(position + j)) {
                            equal = false;
                            break;
                        }
                    }

                    if (equal) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    answer++;
                }
            }

            // Move the window one position to the right
            if (start < a.length() - k) {
                hashA -= value(a.charAt(start)) * power;
                hashA = hashA * base + value(a.charAt(start + k));
            }
        }

        return answer;
    }


    // Add this helper method inside the Marker class
    private static int value(char c) {
        if (c == 'A') return 1;
        if (c == 'C') return 2;
        if (c == 'G') return 3;
        return 4; // T
    }
}
