# DSA Java QA Harness — 40 Questions + 20 Algorithms (and more)
Run a single algorithm from main() if you add one, or just trust the test suite — each @Test is named after its question/algorithm (e.g. Q3_twoSum, A4_heapSort).

can you make this md look better in  telegram

+Run a single algorithm from main() if you add one, or just trust the test suite — each @Test
+is named after its question/algorithm (e.g. Q3_twoSum, A4_heapSort).
 
-| Category | Questions | Core Algorithms |
-|----------|-----------|-----------------|
-| Arrays & Hashing | Q1–Q6, Q8 | A21 selection, A22 insertion |
-| Two Pointers & Window | Q7, Q9, Q10 | A11 sliding window max |
-| Strings | Q11–Q14 | A25 KMP, A26 Rabin-Karp, A27 Trie |
-| Binary Search | Q15–Q18, Q38 | A39 quickselect, Q39 interpolation |
-| Linked Lists | Q19–Q22 | — |
-| Trees & Graphs | Q23–Q30, Q22–Q27 gaps | A16 level-order, A31 segment tree |
-| Dynamic Programming | Q31–Q36, Q40 | A15 LCS, A18 matrix chain |
-| Bit Manipulation | Q37–Q40 | A19, A20 bit tricks |
-| Sorting | — | A1 bubble, A2 merge, A3 quick, A4 heap |
-| Graph Paths | Q29, Q30 | A13 Kruskal, A17 Prim, Bellman-Ford, Floyd-Warshall, A* |
-| Backtracking | — | A28 subsets, A29 perms, A30 N-queens |
-| Number Theory | — | A9 GCD, A10 modPow, A23 LCM, A24 totient |
+---



Here's the list labeled A1 through An:

| Label | Algorithm |
|-------|-----------|
| A1 | Quickselect |
| A2 | Binary Search |
| A3 | Depth-First Search (DFS) |
| A4 | Breadth-First Search (BFS) |
| A5 | Dijkstra's Algorithm |
| A6 | Dynamic Programming |
| A7 | Fibonacci Sequence |
| A8 | Longest Common Subsequence |
| A9 | Binary Tree Traversals (Inorder, Preorder, Postorder) |
| A10 | Heap Sort |
| A11 | Floyd-Warshall Algorithm |
| A12 | Knapsack Problem |
| A13 | Merge Sort |
| A14 | Union Find |
| A15 | Topological Sort |
| A16 | Kruskal's Algorithm |
| A17 | Quick Sort |
| A18 | Prim's Algorithm |
| A19 | Bellman-Ford Algorithm |
| A20 | Kadane's Algorithm |
| A21 | Flood Fill Algorithm |
| A22 | Rabin-Karp Algorithm |
| An (A23) | A* Algorithm |

Most asked DSA questions to ace your next interview
➤ 𝗔𝗿𝗿𝗮𝘆𝘀 𝗮𝗻𝗱 𝗦𝘁𝗿𝗶𝗻𝗴𝘀:
Find the maximum sum subarray.
Find all substrings that are palindromes.
Implement the "two sum" problem.
Implement Kadane's algorithm for maximum subarray sum.
Find the missing number in an array of integers.
Merge two sorted arrays into one sorted array.
Check if a string is a palindrome.
Find the first non-repeating character in a string.
Write a program to remove duplicates from a sorted array.
➤ 𝗟𝗶𝗻𝗸𝗲𝗱 𝗟𝗶𝘀𝘁𝘀:
10. Reverse a linked list.
11. Detect a cycle in a linked list.
12. Find the middle of a linked list.
13. Merge two sorted linked lists.
14. Implement a stack using linked list.
15. Find the intersection point of two linked lists.
➤ 𝗦𝘁𝗮𝗰𝗸𝘀 𝗮𝗻𝗱 𝗤𝘂𝗲𝘂𝗲𝘀:
16. Implement a stack using an array.
17. Implement a stack that supports push, pop, top, and retrieving the minimum element.
18. Implement a circular queue.
19. Design a max stack that supports push, pop, top, retrieve maximum element.
20. Design a queue using stacks.
➤ 𝗧𝗿𝗲𝗲𝘀 𝗮𝗻𝗱 𝗕𝗶𝗻𝗮𝗿𝘆 𝗦𝗲𝗮𝗿𝗰𝗵 𝗧𝗿𝗲𝗲𝘀:
21. Find the height of a binary tree.
22. Find the lowest common ancestor of two nodes in a binary tree.
23. Validate if a binary tree is a valid binary search tree.
24. Serialize and deserialize a binary tree.
25. Implement an inorder traversal of a binary tree.
26. Find the diameter of a binary tree.
27. Convert a binary tree to its mirror tree.
➤ 𝗚𝗿𝗮𝗽𝗵𝘀:
28. Implement depth-first search (DFS).
29. Implement breadth-first search (BFS).
30. Find the shortest path between two nodes in an unweighted graph.
31. Detect a cycle in an undirected graph using DFS.
32. Check if a graph is bipartite.
33. Find the number of connected components in an undirected graph.
34. Find bridges in a graph.
➤ 𝗦𝗼𝗿𝘁𝗶𝗻𝗴 𝗮𝗻𝗱 𝗦𝗲𝗮𝗿𝗰𝗵𝗶𝗻𝗴:
35. Implement (bubble, insertion, selection, merge) sort.
36. Implement quicksort.
37. Implement binary search.
38. Implement interpolation search.
39. Find the kth smallest element in an array.
40. Given an array of integers, count the number of inversions it has. An inversion occurs when two elements in the array are out of order.
Note: your list has 23 algorithms total, not 20 (despite the "20 Algorithms" title) — I labeled all of them sequentially through A23. Let me know if you'd rather I trim it down to a specific 20.
> A runnable, tested study guide: **40 DSA interview questions** and **20 core algorithms** from
> `DSA_Interview_Questions_40_Java.md` + `dsa-ultimate.md`, implemented in **modern Java** (records,
> `sealed`, `var`, Stream API, generics + wildcards, `Optional`, pattern-matching `switch`) and pinned
> down with **96 JUnit 5 tests**. Every question and every algorithm below has its own mermaid
> diagram (rendered to PNG) showing the core flow + the test anchor.

---

## 🗺️ Start here — architecture & coverage

```mermaid
graph TD
    subgraph Questions["40 Questions by topic"]
        Q1[Arrays & Strings]
        Q2[Linked Lists]
        Q3[Trees & BST]
        Q4[Graphs]
        Q5[Dynamic Programming]
        Q6[Bit Manipulation]
    end
    subgraph Algos["20+ Algorithms"]
        A1[Sorting]
        A2[Graph traversals]
        A3[Number theory]
        A4[Data structures]
        A5[Backtracking]
    end
    JUnit[JUnit 5 - 96 tests] --> Questions
    JUnit --> Algos
```

![coverage map](docs/diagrams/algorithm-category-flow.png)

---

## 🚀 Quick start (clone → run → test)

```bash
git clone https://github.com/dbillion/dsa-java-gradleqa.git
cd dsa-java-gradleqa

export JAVA_HOME="$HOME/.sdkman/candidates/java/17.0.12-graal"
export PATH="$JAVA_HOME/bin:$PATH"

./gradlew test          # 96 JUnit tests, all green
```

Run a single algorithm from `main()` if you add one, or just trust the test suite — each `@Test`
is named after its question/algorithm (e.g. `Q3_twoSum`, `A4_heapSort`).

---

## 📚 The 40 Questions (each has a diagram)

### Arrays & Strings
| # | Question | Diagram |
|---|---|---|
| Q1 | Max sum subarray (Kadane) | ![Q1](docs/diagrams/Q1_maxSumSubarray.png) |
| Q2 | All palindromic substrings | ![Q2](docs/diagrams/Q2_allPalindromicSubstrings.png) |
| Q3 | Two Sum | ![Q3](docs/diagrams/Q3_twoSum.png) |
| Q4 | Kadane via prefix sums | ![Q4](docs/diagrams/Q4_kadaneViaPrefixSums.png) |
| Q5 | Missing number (xor) | ![Q5](docs/diagrams/Q5_missingNumber.png) |
| Q6 | Group anagrams / Merge two sorted | ![Q6](docs/diagrams/Q6_groupAnagrams.png) ![Q6b](docs/diagrams/Q6_mergeTwoSorted.png) |
| Q7 | Container max area | ![Q7](docs/diagrams/Q7_maxArea.png) |
| Q8 | First unique char | ![Q8](docs/diagrams/Q8_firstUniqueChar.png) |
| Q9 | 3Sum / Remove duplicates | ![Q9](docs/diagrams/Q9_threeSum.png) ![Q9b](docs/diagrams/Q9_removeDuplicates.png) |
| Q10 | Longest substring no repeat | ![Q10](docs/diagrams/Q10_lengthOfLongestSubstring.png) |
| Q11 | Valid palindrome | ![Q11](docs/diagrams/Q11_isPalindrome.png) |
| Q12 | Longest common prefix | ![Q12](docs/diagrams/Q12_longestCommonPrefix.png) |
| Q13 | Valid parentheses | ![Q13](docs/diagrams/Q13_isValidParentheses.png) |
| Q14 | Run-length encode | ![Q14](docs/diagrams/Q14_runLengthEncode.png) |
| Q15 | Binary search | ![Q15](docs/diagrams/Q15_binarySearch.png) |

### Linked Lists & Stacks/Queues
| # | Question | Diagram |
|---|---|---|
| Q16 | Search rotated / Array stack | ![Q16](docs/diagrams/Q16_searchRotated.png) ![Q16b](docs/diagrams/Q16_arrayStack.png) |
| Q17 | First bad version / Min stack | ![Q17](docs/diagrams/Q17_firstBadVersion.png) ![Q17b](docs/diagrams/Q17_minStack.png) |
| Q18 | Median of two sorted / Circular queue | ![Q18](docs/diagrams/Q18_medianOfSorted.png) ![Q18b](docs/diagrams/Q18_circularQueue.png) |
| Q19 | Reverse list / Max stack | ![Q19](docs/diagrams/Q19_reverseList.png) ![Q19b](docs/diagrams/Q19_maxStack.png) |
| Q20 | Linked list cycle / Queue w/ stacks | ![Q20](docs/diagrams/Q20_hasCycle.png) ![Q20b](docs/diagrams/Q20_queueWithStacks.png) |
| Q21 | Merge two sorted lists | ![Q21](docs/diagrams/Q21_mergeTwoLists.png) |
| Q22 | Remove nth from end / LCA | ![Q22](docs/diagrams/Q22_removeNthFromEnd.png) ![Q22b](docs/diagrams/Q22_lca.png) |

### Trees & BST
| # | Question | Diagram |
|---|---|---|
| Q23 | Max depth / Validate BST | ![Q23](docs/diagrams/Q23_maxDepth.png) ![Q23b](docs/diagrams/Q23_isValidBST.png) |
| Q24 | In-order / Serialize tree | ![Q24](docs/diagrams/Q24_inorder.png) ![Q24b](docs/diagrams/Q24_serializeTree.png) |
| Q25 | Same tree | ![Q25](docs/diagrams/Q25_isSameTree.png) |
| Q26 | Num islands / Diameter | ![Q26](docs/diagrams/Q26_numIslands.png) ![Q26b](docs/diagrams/Q26_diameter.png) |
| Q27 | Clone graph / Mirror | ![Q27](docs/diagrams/Q27_cloneGraph.png) ![Q27b](docs/diagrams/Q27_mirror.png) |
| Q28 | Course schedule (topo) | ![Q28](docs/diagrams/Q28_canFinish.png) |

### Graphs
| # | Question | Diagram |
|---|---|---|
| Q29 | Dijkstra | ![Q29](docs/diagrams/Q29_dijkstra.png) |
| Q30 | BFS shortest path | ![Q30](docs/diagrams/Q30_shortestPath.png) |
| Q31 | Fibonacci / Undirected cycle | ![Q31](docs/diagrams/Q31_fib.png) ![Q31b](docs/diagrams/Q31_cycleUndirected.png) |
| Q32 | Climb stairs / Bipartite | ![Q32](docs/diagrams/Q32_climbStairs.png) ![Q32b](docs/diagrams/Q32_bipartite.png) |
| Q33 | Coin change / Connected components | ![Q33](docs/diagrams/Q33_coinChange.png) ![Q33b](docs/diagrams/Q33_connectedComponents.png) |
| Q34 | LIS / Bridge edges | ![Q34](docs/diagrams/Q34_lengthOfLIS.png) ![Q34b](docs/diagrams/Q34_bridges.png) |

### Dynamic Programming & Bits
| # | Question | Diagram |
|---|---|---|
| Q35 | Edit distance | ![Q35](docs/diagrams/Q35_editDistance.png) |
| Q36 | 0-1 Knapsack | ![Q36](docs/diagrams/Q36_knapsack.png) |
| Q37 | Single number (xor) | ![Q37](docs/diagrams/Q37_singleNumber.png) |
| Q38 | Count bits / Interpolation search | ![Q38](docs/diagrams/Q38_countBits.png) ![Q38b](docs/diagrams/Q38_interpolationSearch.png) |
| Q39 | Power of two / Kth smallest | ![Q39](docs/diagrams/Q39_isPowerOfTwo.png) ![Q39b](docs/diagrams/Q39_kthSmallEst.png) |
| Q40 | Reverse bits / Count inversions | ![Q40](docs/diagrams/Q40_reverseBits.png) ![Q40b](docs/diagrams/Q40_countInversions.png) |

---

## 🔧 The 20+ Algorithms (each has a diagram)

| # | Algorithm | Diagram |
|---|---|---|
| A1 | Bubble sort | ![A1](docs/diagrams/A1_bubbleSort.png) |
| A2 | Merge sort | ![A2](docs/diagrams/A2_mergeSort.png) |
| A3 | Quick sort | ![A3](docs/diagrams/A3_quickSort.png) |
| A4 | Heap sort | ![A4](docs/diagrams/A4_heapSort.png) |
| A5 | BFS | ![A5](docs/diagrams/A5_bfs.png) |
| A6 | DFS | ![A6](docs/diagrams/A6_dfs.png) |
| A7 | Union-Find | ![A7](docs/diagrams/A7_unionFind.png) |
| A8 | Sieve of Eratosthenes | ![A8](docs/diagrams/A8_sieve.png) |
| A9 | GCD (Euclid) | ![A9](docs/diagrams/A9_gcd.png) |
| A10 | Modular exponentiation | ![A10](docs/diagrams/A10_modPow.png) |
| A11 | Sliding window maximum | ![A11](docs/diagrams/A11_slidingWindowMax.png) |
| A12 | Topological sort | ![A12](docs/diagrams/A12_topoSort.png) |
| A13 | Kruskal MST | ![A13](docs/diagrams/A13_kruskal.png) |
| A14 | Fast power | ![A14](docs/diagrams/A14_pow.png) |
| A15 | Longest common subsequence | ![A15](docs/diagrams/A15_lcs.png) |
| A16 | Level-order traversal | ![A16](docs/diagrams/A16_levelOrder.png) |
| A17 | Prim MST | ![A17](docs/diagrams/A17_prim.png) |
| A18 | Matrix chain multiplication | ![A18](docs/diagrams/A18_matrixChain.png) |
| A19 | Lowest set bit | ![A19](docs/diagrams/A19_lowestSetBit.png) |
| A20 | Clear lowest set bit | ![A20](docs/diagrams/A20_clearLowestSetBit.png) |
| A21 | Selection sort | ![A21](docs/diagrams/A21_selectionSort.png) |
| A22 | Insertion sort | ![A22](docs/diagrams/A22_insertionSort.png) |
| A23 | LCM via GCD | ![A23](docs/diagrams/A23_lcm.png) |
| A24 | Euler totient | ![A24](docs/diagrams/A24_eulerTotient.png) |
| A25 | KMP search | ![A25](docs/diagrams/A25_kmp.png) |
| A26 | Rabin-Karp | ![A26](docs/diagrams/A26_rabinKarp.png) |
| A27 | Trie | ![A27](docs/diagrams/A27_trie.png) |
| A28 | Subsets (backtrack) | ![A28](docs/diagrams/A28_subsets.png) |
| A29 | Permutations (backtrack) | ![A29](docs/diagrams/A29_permutations.png) |
| A30 | N-Queens | ![A30](docs/diagrams/A30_nQueens.png) |
| A31 | Segment tree | ![A31](docs/diagrams/A31_segmentTree.png) |

---

## 🧪 Testing

```bash
./gradlew test
```

- **96 tests**, all passing. Every `@Test` is named `<Q|A><n>_<topic>` so a failure maps straight to
  a question or algorithm.
- Tests assert real invariants (e.g. `twoSum` returns the right indices, `heapSort` yields ascending
  order, `kthSmallest` of `[7,10,4,3,20,15]` k=3 is `7`, `countInversions` of `[2,4,1,3,5]` is `3`).

---

## 🗂️ Layout

```
dsa-java-gradleqa/
├── build.gradle
├── settings.gradle
├── gradle/wrapper/            # Gradle 8.2.1 (Java 17)
├── scripts/gen_qa_diagrams.py # emits one mermaid .mmd per test method
├── docs/diagrams/             # 90 per-method PNGs + 8 overview PNGs + .mmd sources
└── src/
    ├── main/java/dsa/Algorithms.java   # all Q + A methods
    └── test/java/dsa/AlgorithmsTest.java # 96 JUnit tests (named per method)
```

---

## 🔧 Tech & conventions
- **Java 17** (sdkman `17.0.12-graal`) · **Gradle 8.2.1** · **JUnit 5.9.1**
- Modern Java: `record` (e.g. `Result`, `Subarray`), `sealed` where useful, `var`, Stream API
  (`groupAnagrams`, `sumNumbers`), generics + wildcards, `Optional`, pattern-matching `switch`.
- Diagrams: generated with Mermaid CLI (`mmdc`) → **PNG only** (no SVG), one per question/algorithm.
- Regenerate diagrams anytime: `python3 scripts/gen_qa_diagrams.py && mmdc -i docs/diagrams/<m>.mmd -o docs/diagrams/<m>.png -t neutral`
