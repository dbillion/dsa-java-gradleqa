package dsa;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;

/**
 * Tests for the 60-algorithm DSA QA harness.
 * 40 "interview question" tests (Q1-Q40) + 20 "core algorithm" tests (A1-A20).
 */
class AlgorithmsTest {

    // ===== Section 1: Arrays & Hashing (Q1-Q6) =====
    @Test void Q1_maxSumSubarray() {
        var r = Algorithms.maxSumSubarray(new int[]{-2,1,-3,4,-1,2,1,-5,4});
        assertEquals(6, r.sum());
        assertArrayEquals(new int[]{4,-1,2,1}, r.subarray());
    }
    @Test void Q2_allPalindromicSubstrings() {
        var pals = Algorithms.allPalindromicSubstrings("abcba");
        assertEquals(7, pals.size());
        assertTrue(pals.containsAll(List.of("a","b","c","bcb","abcba")));
    }
    @Test void Q3_twoSum() {
        assertArrayEquals(new int[]{0,1}, Algorithms.twoSum(new int[]{2,7,11,15}, 9));
    }
    @Test void Q4_kadaneViaPrefixSums() {
        assertEquals(6, Algorithms.kadaneViaPrefixSums(new int[]{-2,1,-3,4,-1,2,1,-5,4}));
    }
    @Test void Q6_groupAnagrams() {
        var groups = Algorithms.groupAnagrams(new String[]{"eat","tea","tan","ate","nat","bat"});
        assertEquals(3, groups.size());
    }
    @Test void Q8_firstUniqueChar() {
        assertEquals(0, Algorithms.firstUniqueChar("leetcode").getAsInt());
        assertTrue(Algorithms.firstUniqueChar("aabb").isEmpty());
    }

    // ===== Section 2: Two Pointers & Sliding Window (Q7-Q10) =====
    @Test void Q7_maxArea() {
        assertEquals(49, Algorithms.maxArea(new int[]{1,8,6,2,5,4,8,3,7}));
    }
    @Test void Q9_threeSum() {
        assertEquals(List.of(List.of(-1,-1,2), List.of(-1,0,1)),
                Algorithms.threeSum(new int[]{-1,0,1,2,-1,-4}));
    }
    @Test void Q10_lengthOfLongestSubstring() {
        assertEquals(3, Algorithms.lengthOfLongestSubstring("abcabcbb"));
    }

    // ===== Section 3: Strings (Q11-Q14) =====
    @Test void Q11_isPalindrome() {
        assertTrue(Algorithms.isPalindrome("A man, a plan, a canal: Panama"));
        assertFalse(Algorithms.isPalindrome("race a car"));
    }
    @Test void Q12_longestCommonPrefix() {
        assertEquals("fl", Algorithms.longestCommonPrefix(new String[]{"flower","flow","flight"}));
    }
    @Test void Q13_isValidParentheses() {
        assertTrue(Algorithms.isValidParentheses("()[]{}"));
        assertFalse(Algorithms.isValidParentheses("(]"));
    }
    @Test void Q14_runLengthEncode() {
        assertEquals("a3b2c1", Algorithms.runLengthEncode("aaabbc").encoded());
    }

    // ===== Section 4: Binary Search (Q15-Q18) =====
    @Test void Q15_binarySearch() {
        assertEquals(4, Algorithms.binarySearch(new int[]{-1,0,3,5,9,12}, 9));
        assertEquals(-1, Algorithms.binarySearch(new int[]{-1,0,3,5,9,12}, 2));
    }
    @Test void Q16_searchRotated() {
        assertEquals(4, Algorithms.searchRotated(new int[]{4,5,6,7,0,1,2}, 0));
    }
    @Test void Q17_firstBadVersion() {
        assertEquals(4, Algorithms.firstBadVersion(5, v -> v >= 4));
    }
    @Test void Q18_medianOfSorted() {
        assertEquals(2.0, Algorithms.medianOfSorted(new int[]{1,3}, new int[]{2}).getAsDouble());
    }

    // ===== Section 5: Linked Lists (Q19-Q22) =====
    @Test void Q19_reverseList() {
        var h = new Algorithms.ListNode(1); h.next = new Algorithms.ListNode(2); h.next.next = new Algorithms.ListNode(3);
        var r = Algorithms.reverseList(h);
        assertEquals(3, r.val); assertEquals(2, r.next.val); assertEquals(1, r.next.next.val);
    }
    @Test void Q20_hasCycle() {
        var a = new Algorithms.ListNode(1); a.next = new Algorithms.ListNode(2); a.next.next = a;
        assertTrue(Algorithms.hasCycle(a));
        var b = new Algorithms.ListNode(1); b.next = new Algorithms.ListNode(2);
        assertFalse(Algorithms.hasCycle(b));
    }
    @Test void Q21_mergeTwoLists() {
        var x = new Algorithms.ListNode(1); x.next = new Algorithms.ListNode(3);
        var y = new Algorithms.ListNode(2); y.next = new Algorithms.ListNode(4);
        var m = Algorithms.mergeTwoLists(x, y);
        assertEquals(1, m.val); assertEquals(2, m.next.val); assertEquals(3, m.next.next.val);
    }
    @Test void Q22_removeNthFromEnd() {
        var h = new Algorithms.ListNode(1); h.next = new Algorithms.ListNode(2); h.next.next = new Algorithms.ListNode(3);
        var r = Algorithms.removeNthFromEnd(h, 2);
        assertEquals(1, r.val); assertEquals(3, r.next.val);
    }

    // ===== Section 6: Trees & Graphs (Q23-Q30) =====
    @Test void Q23_maxDepth() {
        Algorithms.TreeNode root = new Algorithms.TreeNode(1);
        root.left = new Algorithms.TreeNode(2); root.right = new Algorithms.TreeNode(3);
        root.left.left = new Algorithms.TreeNode(4);
        assertEquals(3, Algorithms.maxDepth(root));
    }
    @Test void Q24_inorder() {
        Algorithms.TreeNode root = new Algorithms.TreeNode(2);
        root.left = new Algorithms.TreeNode(1); root.right = new Algorithms.TreeNode(3);
        assertEquals(List.of(1,2,3), Algorithms.inorder(root));
    }
    @Test void Q25_isSameTree() {
        Algorithms.TreeNode a = new Algorithms.TreeNode(1); a.left = new Algorithms.TreeNode(2);
        Algorithms.TreeNode b = new Algorithms.TreeNode(1); b.left = new Algorithms.TreeNode(2);
        assertTrue(Algorithms.isSameTree(a, b));
    }
    @Test void Q26_numIslands() {
        char[][] g = {{'1','1','0'},{'0','1','0'},{'0','0','0'}};
        assertEquals(1, Algorithms.numIslands(g));
    }
    @Test void Q27_cloneGraph() {
        var n0 = new Algorithms.GraphNode(0);
        var n1 = new Algorithms.GraphNode(1);
        n0.neighbors.add(n1); n1.neighbors.add(n0);
        var clone = Algorithms.cloneGraph(n0);
        assertNotSame(n0, clone);
        assertEquals(1, clone.neighbors.size());
        assertEquals(0, clone.neighbors.get(0).neighbors.get(0).val); // points back to clone(0)
    }
    @Test void Q28_canFinish() {
        assertTrue(Algorithms.canFinish(2, new int[][]{{1,0}}));
        assertFalse(Algorithms.canFinish(2, new int[][]{{1,0},{0,1}}));
    }
    @Test void Q29_dijkstra() {
        int[][] e = {{0,1,4},{0,2,1},{2,1,2},{1,3,1},{2,3,5}};
        assertEquals(4, Algorithms.dijkstra(4, e, 0, 3));
    }
    @Test void Q30_shortestPath() {
        char[][] g = {{'.','.','.'},{'.','#','.'},{'.','.','.'}};
        assertEquals(4, Algorithms.shortestPath(g, new int[]{0,0}, new int[]{2,2}).getAsInt());
    }

    // ===== Section 7: Dynamic Programming (Q31-Q36) =====
    @Test void Q31_fib() { assertEquals(55, Algorithms.fib(10)); }
    @Test void Q32_climbStairs() { assertEquals(3, Algorithms.climbStairs(3)); }
    @Test void Q33_coinChange() { assertEquals(3, Algorithms.coinChange(new int[]{1,2,5}, 11)); }
    @Test void Q34_lengthOfLIS() { assertEquals(4, Algorithms.lengthOfLIS(new int[]{10,9,2,5,3,7,101,18})); }
    @Test void Q35_editDistance() { assertEquals(3, Algorithms.editDistance("horse", "ros")); }
    @Test void Q36_knapsack() {
        var r = Algorithms.knapsack(new int[]{1,3,4,5}, new int[]{1,4,5,7}, 7);
        assertEquals(9, r.value());
    }

    // ===== Section 8: Bit Manipulation & Math (Q37-Q40) =====
    @Test void Q37_singleNumber() { assertEquals(4, Algorithms.singleNumber(new int[]{4,1,2,1,2})); }
    @Test void Q38_countBits() { assertEquals(3, Algorithms.countBits(7)); }
    @Test void Q39_isPowerOfTwo() {
        assertTrue(Algorithms.isPowerOfTwo(16));
        assertFalse(Algorithms.isPowerOfTwo(3));
    }
    @Test void Q40_reverseBits() {
        // 1 (=000...0001) reversed over 32 bits = 0x80000000 = -2147483648
        assertEquals(Integer.MIN_VALUE, Algorithms.reverseBits(1));
    }

    // ===== Section 9: 20 Core Algorithms (A1-A20) =====
    @Test void A1_bubbleSort() {
        int[] a = {5,2,8,1,9}; Algorithms.bubbleSort(a);
        assertArrayEquals(new int[]{1,2,5,8,9}, a);
    }
    @Test void A2_mergeSort() {
        assertArrayEquals(new int[]{1,2,5,8,9}, Algorithms.mergeSort(new int[]{5,2,8,1,9}));
    }
    @Test void A3_quickSort() {
        int[] a = {5,2,8,1,9}; Algorithms.quickSort(a);
        assertArrayEquals(new int[]{1,2,5,8,9}, a);
    }
    @Test void A4_heapSort() {
        assertEquals(List.of(1,2,5,8,9), Algorithms.heapSort(List.of(5,2,8,1,9)));
    }
    @Test void A5_bfs() {
        Map<Integer, List<Integer>> g = Map.of(1, List.of(2,3), 2, List.of(4), 3, List.of(4), 4, List.of());
        assertEquals(List.of(1,2,3,4), Algorithms.bfs(g, 1));
    }
    @Test void A6_dfs() {
        Map<String, List<String>> g = Map.of("A", List.of("B","C"), "B", List.of("D"), "C", List.of(), "D", List.of());
        assertEquals(List.of("A","C","B","D"), Algorithms.dfs(g, "A"));
    }
    @Test void A7_unionFind() {
        var uf = new Algorithms.UnionFind(4);
        assertTrue(uf.union(0,1)); assertTrue(uf.union(1,2));
        assertFalse(uf.union(0,2)); // already connected
        assertEquals(0, uf.find(2));
    }
    @Test void A8_sieve() {
        assertEquals(List.of(2,3,5,7,11,13,17,19), Algorithms.sieve(20));
    }
    @Test void A9_gcd() { assertEquals(6, Algorithms.gcd(54, 24)); }
    @Test void A10_modPow() { assertEquals(9, Algorithms.modPow(3, 2, 100)); }
    @Test void A11_slidingWindowMax() {
        assertArrayEquals(new int[]{3,3,5,5,6,7}, Algorithms.slidingWindowMax(new int[]{1,3,-1,-3,5,3,6,7}, 3));
    }
    @Test void A12_topoSort() {
        // 0->1, 0->2, 1->3, 2->3
        assertEquals(List.of(0,1,2,3), Algorithms.topoSort(4, new int[][]{{0,1},{0,2},{1,3},{2,3}}));
    }
    @Test void A13_kruskal() {
        // MST of triangle 0-1(1),1-2(2),0-2(3) => 1+2=3
        assertEquals(3, Algorithms.kruskal(3, new int[][]{{0,1,1},{1,2,2},{0,2,3}}));
    }
    @Test void A14_pow() { assertEquals(8, Algorithms.pow(2, 3)); }
    @Test void A15_lcs() { assertEquals(3, Algorithms.lcs("abcde", "ace")); }
    @Test void A16_levelOrder() {
        Algorithms.TreeNode root = new Algorithms.TreeNode(1);
        root.left = new Algorithms.TreeNode(2); root.right = new Algorithms.TreeNode(3);
        assertEquals(List.of(List.of(1), List.of(2,3)), Algorithms.levelOrder(root));
    }
    @Test void A17_prim() {
        assertEquals(3, Algorithms.prim(3, new int[][]{{0,1,1},{1,2,2},{0,2,3}}));
    }
    @Test void A18_matrixChain() {
        // dims 10x20 x 20x30 => 6000
        assertEquals(6000, Algorithms.matrixChain(new int[]{10,20,30}));
    }
    @Test void A19_lowestSetBit() { assertEquals(4, Algorithms.lowestSetBit(12)); } // 1100 -> 100 = 4
    @Test void A20_clearLowestSetBit() { assertEquals(8, Algorithms.clearLowestSetBit(12)); } // 1100 -> 1000 = 8

    // ===== Section 10: Generics / wildcard helpers =====
    @Test void generics_sumNumbers() {
        assertEquals(6.0, Algorithms.sumNumbers(List.of(1, 2, 3.0)));
    }
    @Test void generics_copy() {
        List<Integer> src = List.of(1, 2, 3);
        List<Number> dst = new ArrayList<>();
        Algorithms.copy(src, dst);
        assertEquals(List.of(1, 2, 3), dst);
    }
}
