# Assignment 4. Patient Zero

Student ID: 253402
Name: Zeine Kambarbek
Group: SE-2510

This file is the report. Fill it in as you go. How to start: `START_HERE.md`. The task: `docs/Assignment4.pdf`.

## Program output

The 9 lines your program prints on your data (`java -cp out Main`):

    Mutations: 62
    Shared core: 1169
    Marker hits: 4
    First hit: 4866
    Border length: 3
    Shared words: 626
    Tests needed: 33
    Cheapest testing: 123
    Longest chain: 25

My outbreak report (a screenshot of `output/outbreak.html`):

![outbreak](output/outbreak.png)

## Questions

Answer in your own words, two or three sentences each.

Mission 1
The cell d[i][j] means the minimum number of changes needed to transform the first i characters of strain A into the first j characters of strain B. It is computed from smaller cells by choosing the best operation: insert, delete, or replace a character.

Mission 2
The longest common subsequence does not require the characters to be next to each other, while the longest common substring must be consecutive. My Shared core can be bigger than the number of letters in the same positions because matching characters can appear in different positions as long as they stay in the same order.

Mission 3
The prefix function tells the length of the longest prefix that is also a suffix for each part of the marker. In KMP, the text pointer never goes back because the prefix function tells us how much of the pattern is still useful after a mismatch. My Border length is 3, which means the longest proper prefix of my marker that is also its suffix has length 3.

Mission 4
We must compare the actual letters after the hashes are equal because two different strings can sometimes have the same hash value. If many windows have the same hash, the algorithm needs many extra comparisons, so Rabin-Karp can become slower.

Mission 5
The two states are the minimum cost when a person is tested and when a person is not tested. If a person is not tested, their children must be tested; if they are tested, we can choose the cheaper state for each child. A greedy choice based only on the number of links may miss a cheaper combination of people in other parts of the tree.

## AI use

I used ChatGPT as a learning assistant to help me understand the algorithms, explain the assignment requirements, and review the implementation. I checked the code by running the provided tests and my program. I reviewed the algorithms and the code so that I can explain how they work.

## Timing (Mission 7)

Milliseconds, average of 3 runs, from `java -cp out Bench`.

| test          | size                      | time                                |
|---------------|---------------------------|-------------------------------------|
| naive search  | 20 000 / 40 000 / 80 000  | 51.8 ms / 98.2 ms / 196.9 ms        |
| KMP           | 20 000 / 40 000 / 80 000  | 2.4 ms / 1.3 ms / 0.4 ms            |
| edit distance | 500 / 1000 / 2000 / 4000  | 5.1 ms / 8.4 ms / 18.4 ms / 86.6 ms |

The naive search time almost doubles when the text size doubles (51.8 → 98.2 → 196.9 ms), which shows linear growth for this test. KMP times stay very small and do not increase consistently because of measurement variations, while edit distance generally grows faster: doubling the size increased the time by about 1.6×, 2.2×, and 4.7×.
## Complexity

n is the length of the text (or of a strain), m the length of the pattern, k the length of the window, v the number of people. Fill in every algorithm you wrote.

| algorithm                  | time              | space     | assumptions                                                                  |
|----------------------------|-------------------|-----------|------------------------------------------------------------------------------|
| Edit distance              | O(n × m)          | O(n × m)  | `n` and `m` are the lengths of the two strains                               |
| LCS                        | O(n × m)          | O(n × m)  | `n` and `m` are the lengths of the two strains                               |
| Prefix function and KMP    | O(n + m)          | O(m)      | `n` is the text length and `m` is the pattern length                         |
| Rabin-Karp (shared words)  | O(n + m) average  | O(m)      | Assumes a good hash with few collisions; many collisions can make it slower  |
| Tree DP                    | O(n)              | O(n)      | The input is a tree, so each person/node and edge is processed once          |

## Before you submit

- The four files in `data` and the screenshot `output/outbreak.png` are in the repository.
- `java tools/Check.java` shows all examples right.
- This README is filled in, the program output and the section AI use included.
- The work is pushed to main (at least one commit per mission) and the repository is public.
- The link is attached in Moodle.
