# DSA Java Gradle QA

A 60-algorithm DSA interview QA harness in modern Java (17+), built with Gradle 8.2 and JUnit 5.

## Contents

- **40 Interview Questions (Q1–Q40)** — LeetCode-style problems covering arrays, hashing, two pointers, sliding window, strings, binary search, linked lists, trees, graphs, dynamic programming, and bit manipulation.
- **20+ Core Algorithms (A1–A31)** — sorting, search, graph traversal, MST, shortest paths, string matching, backtracking, segment trees, and number theory.
- **Modern Java** — `record` return types, pattern matching for `switch`, Stream API, generics with wildcards (PECS), `var`, `Optional`.

## Build & Test

```bash
./gradlew test          # run all 60 algorithm tests
./gradlew build         # compile + test
```

## Algorithm Categories

| Category | Questions | Core Algorithms |
|----------|-----------|-----------------|
| Arrays & Hashing | Q1–Q6, Q8 | A21 selection, A22 insertion |
| Two Pointers & Window | Q7, Q9, Q10 | A11 sliding window max |
| Strings | Q11–Q14 | A25 KMP, A26 Rabin-Karp, A27 Trie |
| Binary Search | Q15–Q18, Q38 | A39 quickselect, Q39 interpolation |
| Linked Lists | Q19–Q22 | — |
| Trees & Graphs | Q23–Q30, Q22–Q27 gaps | A16 level-order, A31 segment tree |
| Dynamic Programming | Q31–Q36, Q40 | A15 LCS, A18 matrix chain |
| Bit Manipulation | Q37–Q40 | A19, A20 bit tricks |
| Sorting | — | A1 bubble, A2 merge, A3 quick, A4 heap |
| Graph Paths | Q29, Q30 | A13 Kruskal, A17 Prim, Bellman-Ford, Floyd-Warshall, A* |
| Backtracking | — | A28 subsets, A29 perms, A30 N-queens |
| Number Theory | — | A9 GCD, A10 modPow, A23 LCM, A24 totient |

## Mermaid Diagrams

Visual explanations of algorithm flow, sequence, and data structures. Source `.mmd` files and rendered `.svg` are in [`docs/diagrams/`](docs/diagrams/).

### Flow Diagrams

| Diagram | Description |
|---------|-------------|
| [algorithm-category-flow.svg](docs/diagrams/algorithm-category-flow.svg) | Decision tree: which algorithm family fits a problem |
| [kadane-flow.svg](docs/diagrams/kadane-flow.svg) | Q1 max-subarray (Kadane) step-by-step |
| [two-sum-flow.svg](docs/diagrams/two-sum-flow.svg) | Q3 two-sum hashmap approach |
| [union-find-kruskal.svg](docs/diagrams/union-find-kruskal.svg) | A7 Union-Find + A13 Kruskal MST |
| [tree-graph-questions.svg](docs/diagrams/tree-graph-questions.svg) | Q19–Q30 tree/graph question map |

### Sequence Diagrams

| Diagram | Description |
|---------|-------------|
| [two-sum-sequence.svg](docs/diagrams/two-sum-sequence.svg) | Q3 two-sum call sequence (O(n) time, O(n) space) |
| [dijkstra-sequence.svg](docs/diagrams/dijkstra-sequence.svg) | Q29 Dijkstra priority-queue relaxation |

### ER Diagram

| Diagram | Description |
|---------|-------------|
| [data-structures-erd.svg](docs/diagrams/data-structures-erd.svg) | Node relationships: ListNode, TreeNode, GraphNode, UnionFind, SegmentTree, Trie |

## Project Structure

```
src/main/java/dsa/Algorithms.java   # all 60 algorithms
src/test/java/dsa/AlgorithmsTest.java # JUnit 5 tests
build.gradle                         # Gradle 8.2 + Java 17 + JUnit 5.9.1
docs/diagrams/                      # Mermaid .mmd + .svg
```

## Rendering Diagrams Locally

```bash
export PUPPETEER_EXECUTABLE_PATH=/usr/bin/chromium
mmdc -i docs/diagrams/kadane-flow.mmd -o docs/diagrams/kadane-flow.svg -t dark
```
