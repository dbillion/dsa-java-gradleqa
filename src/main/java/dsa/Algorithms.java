package dsa;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * DSA QA harness — 60 concise, modern-Java algorithms.
 *
 * 40 "interview questions" (Q1-Q40) covering the LeetCode-style topics from
 * DSA_Interview_Questions_40_Java.md, plus 20 "core algorithms" (A1-A20)
 * from the 20-Algorithms roadmap + advanced gaps (bit manipulation, matrix chain).
 *
 * Modern Java used throughout:
 *   - records for return types
 *   - pattern matching for switch (Java 17+ preview / 21+ standard)
 *   - Stream API for declarative transforms
 *   - generics + wildcards for reusable helpers
 *   - enhanced instanceof, var, Optional
 */
public final class Algorithms {

    private Algorithms() {}

    // ===================================================================
    // SECTION 1 — Arrays & Hashing (Q1-Q6)
    // ===================================================================

    /** Q1: maximum sum subarray (Kadane) returning sum + the subarray itself. */
    public record SubarrayResult(int sum, int[] subarray) {}
    public static SubarrayResult maxSumSubarray(int[] nums) {
        int bestSum = nums[0], currentSum = nums[0];
        int bestStart = 0, bestEnd = 0, currentStart = 0;
        for (int i = 1; i < nums.length; i++) {
            if (currentSum < 0) { currentSum = nums[i]; currentStart = i; }
            else currentSum += nums[i];
            if (currentSum > bestSum) { bestSum = currentSum; bestStart = currentStart; bestEnd = i; }
        }
        return new SubarrayResult(bestSum, Arrays.copyOfRange(nums, bestStart, bestEnd + 1));
    }

    /** Q3: two sum. */
    public static int[] twoSum(int[] nums, int target) {
        var seen = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (seen.containsKey(complement)) return new int[]{seen.get(complement), i};
            seen.put(nums[i], i);
        }
        throw new NoSuchElementException("no two sum solution");
    }

    /** Q4: Kadane via Stream-based prefix-minimum framing (correct + idiomatic). */
    public static int kadaneViaPrefixSums(int[] nums) {
        int[] minPrefix = {0};   // running minimum of prefix sums (starts at 0 for empty prefix)
        int[] best = {nums[0]};
        int[] running = {0};     // running prefix sum
        for (int n : nums) {
            running[0] += n;
            best[0] = Math.max(best[0], running[0] - minPrefix[0]);
            minPrefix[0] = Math.min(minPrefix[0], running[0]);
        }
        return best[0];
    }

    /** Q6: group anagrams — Stream + groupBy on sorted char key. */
    public static List<List<String>> groupAnagrams(String[] words) {
        return new ArrayList<>(Arrays.stream(words)
                .collect(Collectors.groupingBy(w -> w.chars().sorted()
                        .collect(StringBuilder::new, (sb, c) -> sb.append((char) c), StringBuilder::append).toString()))
                .values());
    }

    /** Q8: first non-repeating character index (Optional). */
    public static OptionalInt firstUniqueChar(String s) {
        var counts = s.chars().boxed()
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        for (int i = 0; i < s.length(); i++) {
            if (counts.get((int) s.charAt(i)) == 1L) return OptionalInt.of(i);
        }
        return OptionalInt.empty();
    }

    /** Q2: all palindromic substrings (expand around center). */
    public static List<String> allPalindromicSubstrings(String s) {
        List<String> result = new ArrayList<>();
        for (int center = 0; center < 2 * s.length() - 1; center++) {
            int left = center / 2, right = left + center % 2;
            while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
                result.add(s.substring(left, right + 1));
                left--; right++;
            }
        }
        return result;
    }

    // ===================================================================
    // SECTION 2 — Two Pointers & Sliding Window (Q7-Q10)
    // ===================================================================

    /** Q7: container with most water (two pointers). */
    public static int maxArea(int[] heights) {
        int left = 0, right = heights.length - 1, best = 0;
        while (left < right) {
            int area = Math.min(heights[left], heights[right]) * (right - left);
            best = Math.max(best, area);
            if (heights[left] < heights[right]) left++; else right--;
        }
        return best;
    }

    /** Q9: 3-sum (sorted two-pointer). */
    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        var result = new ArrayList<List<Integer>>();
        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            int l = i + 1, r = nums.length - 1;
            while (l < r) {
                int sum = nums[i] + nums[l] + nums[r];
                if (sum == 0) {
                    result.add(List.of(nums[i], nums[l], nums[r]));
                    while (l < r && nums[l] == nums[l + 1]) l++;
                    while (l < r && nums[r] == nums[r - 1]) r--;
                    l++; r--;
                } else if (sum < 0) l++; else r--;
            }
        }
        return result;
    }

    /** Q10: longest substring without repeating chars (sliding window). */
    public static int lengthOfLongestSubstring(String s) {
        var seen = new HashSet<Character>();
        int left = 0, best = 0;
        for (int right = 0; right < s.length(); right++) {
            while (seen.contains(s.charAt(right))) seen.remove(s.charAt(left++));
            seen.add(s.charAt(right));
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    // ===================================================================
    // SECTION 3 — Strings (Q11-Q14)
    // ===================================================================

    /** Q11: is palindrome (Stream). */
    public static boolean isPalindrome(String s) {
        String cleaned = s.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        return IntStream.range(0, cleaned.length() / 2)
                .allMatch(i -> cleaned.charAt(i) == cleaned.charAt(cleaned.length() - 1 - i));
    }

    /** Q12: longest common prefix. */
    public static String longestCommonPrefix(String[] strs) {
        if (strs.length == 0) return "";
        String prefix = strs[0];
        for (String s : strs) {
            while (!s.startsWith(prefix)) prefix = prefix.substring(0, prefix.length() - 1);
        }
        return prefix;
    }

    /** Q13: valid parentheses (Deque/ArrayDeque, not legacy Stack). */
    public static boolean isValidParentheses(String s) {
        var stack = new ArrayDeque<Character>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') stack.push(c);
            else if (stack.isEmpty() || !matches(stack.pop(), c)) return false;
        }
        return stack.isEmpty();
    }
    private static boolean matches(char open, char close) {
        return (open == '(' && close == ')') || (open == '[' && close == ']') || (open == '{' && close == '}');
    }

    /** Q14: encode/decode run-length (records). */
    public record RLE(String encoded) {}
    public static RLE runLengthEncode(String s) {
        if (s.isEmpty()) return new RLE("");
        var sb = new StringBuilder();
        int count = 1;
        for (int i = 1; i <= s.length(); i++) {
            if (i < s.length() && s.charAt(i) == s.charAt(i - 1)) count++;
            else { sb.append(s.charAt(i - 1)).append(count); count = 1; }
        }
        return new RLE(sb.toString());
    }

    // ===================================================================
    // SECTION 4 — Binary Search & Sorted (Q15-Q18)
    // ===================================================================

    /** Q15: binary search. */
    public static int binarySearch(int[] nums, int target) {
        int l = 0, r = nums.length - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            if (nums[m] == target) return m;
            if (nums[m] < target) l = m + 1; else r = m - 1;
        }
        return -1;
    }

    /** Q16: search in rotated sorted array. */
    public static int searchRotated(int[] nums, int target) {
        int l = 0, r = nums.length - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            if (nums[m] == target) return m;
            if (nums[l] <= nums[m]) {
                if (target >= nums[l] && target < nums[m]) r = m - 1; else l = m + 1;
            } else {
                if (target > nums[m] && target <= nums[r]) l = m + 1; else r = m - 1;
            }
        }
        return -1;
    }

    /** Q17: first bad version (lower_bound). */
    public static int firstBadVersion(int n, IntPredicate isBad) {
        int l = 1, r = n;
        while (l < r) {
            int m = l + (r - l) / 2;
            if (isBad.test(m)) r = m; else l = m + 1;
        }
        return l;
    }

    /** Q18: median of two sorted arrays (Optional when empty). */
    public static OptionalDouble medianOfSorted(int[] a, int[] b) {
        if (a.length == 0 && b.length == 0) return OptionalDouble.empty();
        var merged = IntStream.concat(IntStream.of(a), IntStream.of(b)).sorted().toArray();
        int n = merged.length;
        return OptionalDouble.of(n % 2 == 1 ? merged[n / 2]
                : (merged[n / 2 - 1] + merged[n / 2]) / 2.0);
    }

    // ===================================================================
    // SECTION 5 — Linked Lists (Q19-Q22)
    // ===================================================================

    /** Q19: singly-linked list node + reverse. */
    public static class ListNode {
        int val; ListNode next;
        ListNode(int v) { this.val = v; }
    }
    public static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        while (head != null) { var next = head.next; head.next = prev; prev = head; head = next; }
        return prev;
    }

    /** Q20: detect cycle (Floyd). */
    public static boolean hasCycle(ListNode head) {
        var slow = head; var fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next; fast = fast.next.next;
            if (slow == fast) return true;
        }
        return false;
    }

    /** Q21: merge two sorted lists (recursive + pattern matching style). */
    public static ListNode mergeTwoLists(ListNode a, ListNode b) {
        if (a == null) return b;
        if (b == null) return a;
        if (a.val <= b.val) { a.next = mergeTwoLists(a.next, b); return a; }
        b.next = mergeTwoLists(a, b.next); return b;
    }

    /** Q22: remove Nth from end (two pointers). */
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        var dummy = new ListNode(0); dummy.next = head;
        var first = dummy; var second = dummy;
        for (int i = 0; i <= n; i++) first = first.next;
        while (first != null) { first = first.next; second = second.next; }
        second.next = second.next.next;
        return dummy.next;
    }

    // ===================================================================
    // SECTION 6 — Trees & Graphs (Q23-Q30)
    // ===================================================================

    /** Q23: binary tree node + max depth. */
    public static class TreeNode {
        int val; TreeNode left, right;
        TreeNode(int v) { this.val = v; }
    }
    public static int maxDepth(TreeNode root) {
        return root == null ? 0 : 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }

    /** Q24: inorder traversal (Stream-flattened). */
    public static List<Integer> inorder(TreeNode root) {
        return root == null ? List.of()
                : Stream.concat(Stream.concat(inorder(root.left).stream(), Stream.of(root.val)),
                                 inorder(root.right).stream()).toList();
    }

    /** Q25: is same tree. */
    public static boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null || q == null) return p == q;
        return p.val == q.val && isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }

    /** Q26: number of islands (DFS grid). */
    public static int numIslands(char[][] grid) {
        int count = 0;
        for (int r = 0; r < grid.length; r++)
            for (int c = 0; c < grid[0].length; c++)
                if (grid[r][c] == '1') { count++; dfs(grid, r, c); }
        return count;
    }
    private static void dfs(char[][] g, int r, int c) {
        if (r < 0 || c < 0 || r >= g.length || c >= g[0].length || g[r][c] != '1') return;
        g[r][c] = '0';
        dfs(g, r + 1, c); dfs(g, r - 1, c); dfs(g, r, c + 1); dfs(g, r, c - 1);
    }

    /** Q27: clone graph (BFS + map). */
    public static class GraphNode {
        int val; List<GraphNode> neighbors = new ArrayList<>();
        GraphNode(int v) { this.val = v; }
    }
    public static GraphNode cloneGraph(GraphNode node) {
        if (node == null) return null;
        var map = new HashMap<GraphNode, GraphNode>();
        var q = new ArrayDeque<GraphNode>();
        map.put(node, new GraphNode(node.val));
        q.add(node);
        while (!q.isEmpty()) {
            var cur = q.poll();
            for (var nb : cur.neighbors) {
                if (!map.containsKey(nb)) {
                    map.put(nb, new GraphNode(nb.val));
                    q.add(nb);
                }
                map.get(cur).neighbors.add(map.get(nb));
            }
        }
        return map.get(node);
    }

    /** Q28: course schedule (topological sort / Kahn). */
    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        var adj = new ArrayList<List<Integer>>();
        for (int i = 0; i < numCourses; i++) adj.add(new ArrayList<>());
        int[] indeg = new int[numCourses];
        for (var p : prerequisites) { adj.get(p[1]).add(p[0]); indeg[p[0]]++; }
        var q = IntStream.range(0, numCourses).filter(i -> indeg[i] == 0).boxed().collect(Collectors.toCollection(ArrayDeque::new));
        int visited = 0;
        while (!q.isEmpty()) {
            int u = q.poll(); visited++;
            for (int v : adj.get(u)) if (--indeg[v] == 0) q.add(v);
        }
        return visited == numCourses;
    }

    /** Q29: Dijkstra shortest path (priority queue). */
    public static int dijkstra(int n, int[][] edges, int src, int dst) {
        var adj = new ArrayList<List<int[]>>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (var e : edges) { adj.get(e[0]).add(new int[]{e[1], e[2]}); adj.get(e[1]).add(new int[]{e[0], e[2]}); }
        var dist = new int[n]; Arrays.fill(dist, Integer.MAX_VALUE); dist[src] = 0;
        var pq = new PriorityQueue<int[]>((a, b) -> a[1] - b[1]); pq.add(new int[]{src, 0});
        while (!pq.isEmpty()) {
            var cur = pq.poll();
            if (cur[0] == dst) return cur[1];
            if (cur[1] > dist[cur[0]]) continue;
            for (var nb : adj.get(cur[0])) if (cur[1] + nb[1] < dist[nb[0]]) {
                dist[nb[0]] = cur[1] + nb[1]; pq.add(new int[]{nb[0], dist[nb[0]]});
            }
        }
        return -1;
    }

    /** Q30: BFS shortest path in unweighted grid (Optional). */
    public static OptionalInt shortestPath(char[][] grid, int[] start, int[] end) {
        if (grid[start[0]][start[1]] == '#' || grid[end[0]][end[1]] == '#') return OptionalInt.empty();
        var q = new ArrayDeque<int[]>(); q.add(new int[]{start[0], start[1], 0});
        var seen = new HashSet<String>(); seen.add(start[0] + "," + start[1]);
        while (!q.isEmpty()) {
            var cur = q.poll();
            if (cur[0] == end[0] && cur[1] == end[1]) return OptionalInt.of(cur[2]);
            for (int[] d : new int[][]{{1,0},{-1,0},{0,1},{0,-1}}) {
                int nr = cur[0] + d[0], nc = cur[1] + d[1];
                String key = nr + "," + nc;
                if (nr >= 0 && nc >= 0 && nr < grid.length && nc < grid[0].length
                        && grid[nr][nc] != '#' && seen.add(key))
                    q.add(new int[]{nr, nc, cur[2] + 1});
            }
        }
        return OptionalInt.empty();
    }

    // ===================================================================
    // SECTION 7 — Dynamic Programming (Q31-Q36)
    // ===================================================================

    /** Q31: fibonacci (memoized, iterative fill to avoid self-referential lambda). */
    public static long fib(int n) {
        if (n < 2) return n;
        var memo = new long[n + 1];
        memo[0] = 0; memo[1] = 1;
        for (int i = 2; i <= n; i++) memo[i] = memo[i - 1] + memo[i - 2];
        return memo[n];
    }

    /** Q32: climb stairs. */
    public static int climbStairs(int n) {
        if (n < 3) return n;
        int a = 1, b = 2;
        for (int i = 3; i <= n; i++) { int t = a + b; a = b; b = t; }
        return b;
    }

    /** Q33: coin change (min coins, Stream min). */
    public static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1]; Arrays.fill(dp, amount + 1); dp[0] = 0;
        for (int a = 1; a <= amount; a++)
            for (int c : coins) if (c <= a) dp[a] = Math.min(dp[a], dp[a - c] + 1);
        return dp[amount] > amount ? -1 : dp[amount];
    }

    /** Q34: longest increasing subsequence (Stream). */
    public static int lengthOfLIS(int[] nums) {
        int[] dp = new int[nums.length];
        int len = 0;
        for (int x : nums) {
            int i = Arrays.binarySearch(dp, 0, len, x);
            if (i < 0) i = -(i + 1);
            dp[i] = x;
            if (i == len) len++;
        }
        return len;
    }

    /** Q35: edit distance. */
    public static int editDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++) dp[0][j] = j;
        for (int i = 1; i <= a.length(); i++)
            for (int j = 1; j <= b.length(); j++)
                dp[i][j] = Math.min(Math.min(dp[i-1][j] + 1, dp[i][j-1] + 1),
                        dp[i-1][j-1] + (a.charAt(i-1) == b.charAt(j-1) ? 0 : 1));
        return dp[a.length()][b.length()];
    }

    /** Q36: knapsack 0/1 (records for clarity). */
    public record KnapsackResult(int value, List<Integer> items) {}
    public static KnapsackResult knapsack(int[] weights, int[] values, int capacity) {
        int n = values.length;
        int[][] dp = new int[n + 1][capacity + 1];
        for (int i = 1; i <= n; i++)
            for (int w = 0; w <= capacity; w++)
                dp[i][w] = w >= weights[i-1]
                        ? Math.max(dp[i-1][w], dp[i-1][w-weights[i-1]] + values[i-1]) : dp[i-1][w];
        int w = capacity; var items = new ArrayList<Integer>();
        for (int i = n; i > 0 && dp[i][w] > 0; i--)
            if (dp[i][w] != dp[i-1][w]) { items.add(i-1); w -= weights[i-1]; }
        Collections.reverse(items);
        return new KnapsackResult(dp[n][capacity], items);
    }

    // ===================================================================
    // SECTION 8 — Bit Manipulation & Math (Q37-Q40)
    // ===================================================================

    /** Q37: single number (XOR). */
    public static int singleNumber(int[] nums) {
        return Arrays.stream(nums).reduce(0, (a, b) -> a ^ b);
    }

    /** Q38: count bits set (Brian Kernighan). */
    public static int countBits(int n) {
        int c = 0;
        while (n != 0) { n &= n - 1; c++; }
        return c;
    }

    /** Q39: power of two. */
    public static boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    /** Q40: reverse bits. */
    public static int reverseBits(int n) {
        int r = 0;
        for (int i = 0; i < 32; i++) { r = (r << 1) | (n & 1); n >>= 1; }
        return r;
    }

    // ===================================================================
    // SECTION 9 — 20 Core Algorithms (A1-A20) from roadmap + gaps
    // ===================================================================

    /** A1: bubble sort. */
    public static void bubbleSort(int[] a) {
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < a.length - 1 - i; j++)
                if (a[j] > a[j + 1]) { int t = a[j]; a[j] = a[j+1]; a[j+1] = t; }
    }

    /** A2: merge sort (generic comparator via natural order of Integers). */
    public static int[] mergeSort(int[] a) {
        if (a.length < 2) return a;
        int mid = a.length / 2;
        int[] left = mergeSort(Arrays.copyOfRange(a, 0, mid));
        int[] right = mergeSort(Arrays.copyOfRange(a, mid, a.length));
        return mergeSorted(left, right);
    }
    private static int[] mergeSorted(int[] l, int[] r) {
        int[] out = new int[l.length + r.length]; int i = 0, j = 0, k = 0;
        while (i < l.length && j < r.length) out[k++] = l[i] <= r[j] ? l[i++] : r[j++];
        while (i < l.length) out[k++] = l[i++];
        while (j < r.length) out[k++] = r[j++];
        return out;
    }

    /** A3: quick sort (median-of-three-ish, in-place). */
    public static void quickSort(int[] a) { quickSort(a, 0, a.length - 1); }
    private static void quickSort(int[] a, int lo, int hi) {
        if (lo >= hi) return;
        int p = partition(a, lo, hi); quickSort(a, lo, p - 1); quickSort(a, p + 1, hi);
    }
    private static int partition(int[] a, int lo, int hi) {
        int pivot = a[hi]; int i = lo;
        for (int j = lo; j < hi; j++) if (a[j] <= pivot) { int t = a[i]; a[i] = a[j]; a[j] = t; i++; }
        int t = a[i]; a[i] = a[hi]; a[hi] = t; return i;
    }

    /** A4: binary heap sort (min-heap) — ascending. */
    public static <T extends Comparable<? super T>> List<T> heapSort(List<T> input) {
        var heap = new PriorityQueue<T>();
        heap.addAll(input);
        var out = new ArrayList<T>();
        while (!heap.isEmpty()) out.add(heap.poll());
        return out;
    }

    /** A5: breadth-first search on adjacency list (generic node type). */
    public static <N> List<N> bfs(Map<N, List<N>> graph, N start) {
        var visited = new LinkedHashSet<N>();
        var q = new ArrayDeque<N>(); q.add(start); visited.add(start);
        while (!q.isEmpty()) {
            var cur = q.poll();
            for (var nb : graph.getOrDefault(cur, List.of()))
                if (visited.add(nb)) q.add(nb);
        }
        return new ArrayList<>(visited);
    }

    /** A6: depth-first search (generic). */
    public static <N> List<N> dfs(Map<N, List<N>> graph, N start) {
        var visited = new LinkedHashSet<N>();
        var stack = new ArrayDeque<N>(); stack.push(start);
        while (!stack.isEmpty()) {
            var cur = stack.pop();
            if (visited.add(cur))
                for (var nb : graph.getOrDefault(cur, List.of())) if (!visited.contains(nb)) stack.push(nb);
        }
        return new ArrayList<>(visited);
    }

    /** A7: union-find (disjoint set) with path compression + rank. */
    public static final class UnionFind {
        private final int[] parent, rank;
        public UnionFind(int n) { parent = new int[n]; rank = new int[n]; for (int i = 0; i < n; i++) parent[i] = i; }
        public int find(int x) { while (parent[x] != x) { parent[x] = parent[parent[x]]; x = parent[x]; } return x; }
        public boolean union(int a, int b) {
            int ra = find(a), rb = find(b);
            if (ra == rb) return false;
            if (rank[ra] < rank[rb]) parent[ra] = rb;
            else if (rank[ra] > rank[rb]) parent[rb] = ra;
            else { parent[rb] = ra; rank[ra]++; }
            return true;
        }
    }

    /** A8: sieve of Eratosthenes. */
    public static List<Integer> sieve(int n) {
        boolean[] prime = new boolean[n + 1];
        Arrays.fill(prime, true);
        for (int p = 2; p * p <= n; p++)
            if (prime[p]) for (int i = p * p; i <= n; i += p) prime[i] = false;
        var out = new ArrayList<Integer>();
        for (int i = 2; i <= n; i++) if (prime[i]) out.add(i);
        return out;
    }

    /** A9: Euclidean GCD. */
    public static int gcd(int a, int b) { while (b != 0) { int t = b; b = a % b; a = t; } return Math.abs(a); }

    /** A10: modular exponentiation (fast pow). */
    public static long modPow(long base, long exp, long mod) {
        long result = 1; base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) result = (result * base) % mod;
            base = (base * base) % mod; exp >>= 1;
        }
        return result;
    }

    /** A11: sliding window maximum (deque). */
    public static int[] slidingWindowMax(int[] a, int k) {
        if (a.length == 0) return new int[0];
        var dq = new ArrayDeque<Integer>();
        int[] out = new int[a.length - k + 1];
        for (int i = 0; i < a.length; i++) {
            while (!dq.isEmpty() && dq.peekFirst() < i - k + 1) dq.pollFirst();
            while (!dq.isEmpty() && a[dq.peekLast()] <= a[i]) dq.pollLast();
            dq.addLast(i);
            if (i >= k - 1) out[i - k + 1] = a[dq.peekFirst()];
        }
        return out;
    }

    /** A12: topological sort (generics over Comparable-friendly node ids). */
    public static List<Integer> topoSort(int n, int[][] edges) {
        var adj = new ArrayList<List<Integer>>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        int[] indeg = new int[n];
        for (var e : edges) { adj.get(e[0]).add(e[1]); indeg[e[1]]++; }
        var q = IntStream.range(0, n).filter(i -> indeg[i] == 0).boxed().collect(Collectors.toCollection(ArrayDeque::new));
        var out = new ArrayList<Integer>();
        while (!q.isEmpty()) {
            int u = q.poll(); out.add(u);
            for (int v : adj.get(u)) if (--indeg[v] == 0) q.add(v);
        }
        return out.size() == n ? out : List.of();
    }

    /** A13: Kruskal's MST (uses UnionFind). */
    public static int kruskal(int n, int[][] edges) {
        Arrays.sort(edges, (a, b) -> Integer.compare(a[2], b[2]));
        var uf = new UnionFind(n); int total = 0, count = 0;
        for (var e : edges) if (uf.union(e[0], e[1])) { total += e[2]; count++; }
        return count == n - 1 ? total : -1;
    }

    /** A14: binary exponentiation (non-modular). */
    public static long pow(long base, long exp) {
        long result = 1;
        while (exp > 0) { if ((exp & 1) == 1) result *= base; base *= base; exp >>= 1; }
        return result;
    }

    /** A15: longest common subsequence. */
    public static int lcs(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 1; i <= a.length(); i++)
            for (int j = 1; j <= b.length(); j++)
                dp[i][j] = a.charAt(i-1) == b.charAt(j-1) ? dp[i-1][j-1] + 1 : Math.max(dp[i-1][j], dp[i][j-1]);
        return dp[a.length()][b.length()];
    }

    /** A16: binary tree level-order traversal (List of levels). */
    public static List<List<Integer>> levelOrder(TreeNode root) {
        var result = new ArrayList<List<Integer>>();
        if (root == null) return result;
        var q = new ArrayDeque<TreeNode>(); q.add(root);
        while (!q.isEmpty()) {
            int size = q.size(); var level = new ArrayList<Integer>();
            for (int i = 0; i < size; i++) {
                var n = q.poll(); level.add(n.val);
                if (n.left != null) q.add(n.left);
                if (n.right != null) q.add(n.right);
            }
            result.add(level);
        }
        return result;
    }

    /** A17: minimum spanning tree (Prim) — returns total weight or -1. */
    public static int prim(int n, int[][] edges) {
        var adj = new ArrayList<List<int[]>>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (var e : edges) { adj.get(e[0]).add(new int[]{e[1], e[2]}); adj.get(e[1]).add(new int[]{e[0], e[2]}); }
        var visited = new boolean[n]; var pq = new PriorityQueue<int[]>((x, y) -> x[1] - y[1]);
        pq.add(new int[]{0, 0}); int total = 0, count = 0;
        while (!pq.isEmpty() && count < n) {
            var cur = pq.poll();
            if (visited[cur[0]]) continue;
            visited[cur[0]] = true; total += cur[1]; count++;
            for (var nb : adj.get(cur[0])) if (!visited[nb[0]]) pq.add(nb);
        }
        return count == n ? total : -1;
    }

    /** A18: matrix chain multiplication (DP from DSA_Advanced_Gaps.md). */
    public static int matrixChain(int[] dims) {
        int n = dims.length - 1;
        int[][] dp = new int[n][n];
        for (int len = 2; len <= n; len++)
            for (int i = 0; i < n - len + 1; i++) {
                int j = i + len - 1; dp[i][j] = Integer.MAX_VALUE;
                for (int k = i; k < j; k++)
                    dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k+1][j] + dims[i]*dims[k+1]*dims[j+1]);
            }
        return dp[0][n-1];
    }

    /** A19: bit manipulation — isolate lowest set bit (x & -x). */
    public static int lowestSetBit(int n) { return n & (-n); }

    /** A20: bit manipulation — clear lowest set bit (x & (x-1)). */
    public static int clearLowestSetBit(int n) { return n & (n - 1); }

    // ===================================================================
    // SECTION 10 — Generic wildcard helper (demonstrates ? extends / super)
    // ===================================================================

    /** Generic sum of a collection of Number using bounded wildcard (? extends Number). */
    public static double sumNumbers(List<? extends Number> numbers) {
        return numbers.stream().mapToDouble(Number::doubleValue).sum();
    }

    /** Generic copy using super wildcard (? super T) — PECS. */
    public static <T> void copy(List<? extends T> src, List<? super T> dst) {
        src.forEach(dst::add);
    }

    // ====================================================================
    // SECTION 11 — GAPS FILLED from dsa-ultimate.md roadmap
    // ====================================================================

    // ---- Stacks & Queues (Q16-Q20) ----
    /** Q16: stack backed by an array. */
    public static class ArrayStack {
        private final int[] a; private int top = 0;
        public ArrayStack(int cap) { a = new int[cap]; }
        public void push(int v) { a[top++] = v; }
        public int pop() { return a[--top]; }
        public int top() { return a[top - 1]; }
        public boolean isEmpty() { return top == 0; }
    }

    /** Q17: min-stack (O(1) push/pop/top/min). */
    public static class MinStack {
        private final Deque<int[]> d = new ArrayDeque<>();
        public void push(int v) { int min = d.isEmpty() ? v : Math.min(v, d.peek()[1]); d.push(new int[]{v, min}); }
        public void pop() { d.pop(); }
        public int top() { return d.peek()[0]; }
        public int getMin() { return d.peek()[1]; }
    }

    /** Q18: circular queue (ring buffer). */
    public static class CircularQueue {
        private final int[] buf; private int head = 0, tail = 0, size = 0;
        public CircularQueue(int cap) { buf = new int[cap]; }
        public boolean enqueue(int v) {
            if (size == buf.length) return false;
            buf[tail] = v; tail = (tail + 1) % buf.length; size++; return true;
        }
        public Integer dequeue() {
            if (size == 0) return null;
            int v = buf[head]; head = (head + 1) % buf.length; size--; return v;
        }
        public boolean isEmpty() { return size == 0; }
    }

    /** Q19: max-stack (O(1) max). */
    public static class MaxStack {
        private final Deque<int[]> d = new ArrayDeque<>();
        public void push(int v) { int mx = d.isEmpty() ? v : Math.max(v, d.peek()[1]); d.push(new int[]{v, mx}); }
        public void pop() { d.pop(); }
        public int top() { return d.peek()[0]; }
        public int getMax() { return d.peek()[1]; }
    }

    /** Q20: queue implemented with two stacks. */
    public static class QueueWithStacks {
        private final Deque<Integer> in = new ArrayDeque<>(), out = new ArrayDeque<>();
        public void enqueue(int v) { in.push(v); }
        public Integer dequeue() {
            if (out.isEmpty()) while (!in.isEmpty()) out.push(in.pop());
            return out.isEmpty() ? null : out.pop();
        }
    }

    // ---- More Arrays (Q5, Q6, Q9) ----
    /** Q5: missing number in 0..n (XOR trick). */
    public static int missingNumber(int[] nums) {
        int x = nums.length;
        for (int i = 0; i < nums.length; i++) x ^= i ^ nums[i];
        return x;
    }

    /** Q6: merge two sorted arrays into one sorted array. */
    public static int[] mergeTwoSorted(int[] a, int[] b) {
        int[] out = new int[a.length + b.length]; int i = 0, j = 0, k = 0;
        while (i < a.length && j < b.length) out[k++] = a[i] <= b[j] ? a[i++] : b[j++];
        while (i < a.length) out[k++] = a[i++];
        while (j < b.length) out[k++] = b[j++];
        return out;
    }

    /** Q9: remove duplicates from sorted array in place; returns new length. */
    public static int removeDuplicates(int[] nums) {
        if (nums.length == 0) return 0;
        int k = 1;
        for (int i = 1; i < nums.length; i++) if (nums[i] != nums[k - 1]) nums[k++] = nums[i];
        return k;
    }

    // ---- BST / Tree ops (Q22, Q23, Q24, Q26, Q27) ----
    /** Q22: lowest common ancestor in a binary tree. */
    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) return root;
        var left = lowestCommonAncestor(root.left, p, q);
        var right = lowestCommonAncestor(root.right, p, q);
        return left == null ? right : (right == null ? left : root);
    }

    /** Q23: validate BST (inorder must be strictly increasing). */
    public static boolean isValidBST(TreeNode root) {
        Integer[] prev = {null};
        return inorderValid(root, prev);
    }
    private static boolean inorderValid(TreeNode n, Integer[] prev) {
        if (n == null) return true;
        if (!inorderValid(n.left, prev)) return false;
        if (prev[0] != null && n.val <= prev[0]) return false;
        prev[0] = n.val;
        return inorderValid(n.right, prev);
    }

    /** Q24: serialize a binary tree to a level-order list (null children omitted). */
    public static List<Integer> serializeTree(TreeNode root) {
        var out = new ArrayList<Integer>();
        if (root == null) return out;
        var q = new ArrayDeque<TreeNode>(); q.add(root);
        while (!q.isEmpty()) {
            var cur = q.poll();
            out.add(cur.val);
            if (cur.left != null) q.add(cur.left);
            if (cur.right != null) q.add(cur.right);
        }
        return out;
    }

    /** Q26: diameter of a binary tree (longest path between any two nodes). */
    public static int diameter(TreeNode root) {
        int[] best = {0};
        depthForDiameter(root, best);
        return best[0];
    }
    private static int depthForDiameter(TreeNode n, int[] best) {
        if (n == null) return 0;
        int l = depthForDiameter(n.left, best);
        int r = depthForDiameter(n.right, best);
        best[0] = Math.max(best[0], l + r);
        return 1 + Math.max(l, r);
    }

    /** Q27: mirror a binary tree (in place). */
    public static void mirror(TreeNode n) {
        if (n == null) return;
        var t = n.left; n.left = n.right; n.right = t;
        mirror(n.left); mirror(n.right);
    }

    // ---- Graph extras (Q31, Q32, Q33, Q34, Bellman-Ford, Floyd-Warshall, A*, Flood Fill) ----
    /** Q31: detect cycle in an undirected graph (DFS + parent tracking). */
    public static boolean hasCycleUndirected(int n, int[][] edges) {
        var adj = new ArrayList<List<Integer>>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (var e : edges) { adj.get(e[0]).add(e[1]); adj.get(e[1]).add(e[0]); }
        var seen = new boolean[n];
        for (int i = 0; i < n; i++)
            if (!seen[i] && dfsCycle(adj, i, -1, seen)) return true;
        return false;
    }
    private static boolean dfsCycle(List<List<Integer>> adj, int u, int p, boolean[] seen) {
        seen[u] = true;
        for (int v : adj.get(u)) {
            if (!seen[v]) { if (dfsCycle(adj, v, u, seen)) return true; }
            else if (v != p) return true;
        }
        return false;
    }

    /** Q32: check if an undirected graph is bipartite (2-color). */
    public static boolean isBipartite(int n, int[][] edges) {
        var adj = new ArrayList<List<Integer>>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (var e : edges) { adj.get(e[0]).add(e[1]); adj.get(e[1]).add(e[0]); }
        int[] color = new int[n];
        for (int i = 0; i < n; i++) {
            if (color[i] != 0) continue;
            color[i] = 1; var q = new ArrayDeque<Integer>(); q.add(i);
            while (!q.isEmpty()) {
                int u = q.poll();
                for (int v : adj.get(u)) {
                    if (color[v] == 0) { color[v] = -color[u]; q.add(v); }
                    else if (color[v] == color[u]) return false;
                }
            }
        }
        return true;
    }

    /** Q33: number of connected components in an undirected graph. */
    public static int connectedComponents(int n, int[][] edges) {
        var uf = new UnionFind(n);
        for (var e : edges) uf.union(e[0], e[1]);
        int count = 0;
        for (int i = 0; i < n; i++) if (uf.find(i) == i) count++;
        return count;
    }

    /** Q34: find bridges in an undirected graph (Tarjan). Returns list of [u,v]. */
    public static List<List<Integer>> findBridges(int n, int[][] edges) {
        var adj = new ArrayList<List<Integer>>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (var e : edges) { adj.get(e[0]).add(e[1]); adj.get(e[1]).add(e[0]); }
        int[] disc = new int[n], low = new int[n]; int[] time = {0};
        boolean[] visited = new boolean[n];
        var bridges = new ArrayList<List<Integer>>();
        for (int i = 0; i < n; i++)
            if (!visited[i]) tarjan(adj, i, -1, disc, low, time, visited, bridges);
        return bridges;
    }
    private static void tarjan(List<List<Integer>> adj, int u, int p, int[] disc, int[] low,
                               int[] time, boolean[] visited, List<List<Integer>> bridges) {
        visited[u] = true; disc[u] = low[u] = ++time[0];
        for (int v : adj.get(u)) {
            if (v == p) continue;
            if (!visited[v]) {
                tarjan(adj, v, u, disc, low, time, visited, bridges);
                low[u] = Math.min(low[u], low[v]);
                if (low[v] > disc[u]) bridges.add(List.of(u, v));
            } else low[u] = Math.min(low[u], disc[v]);
        }
    }

    /** Bellman-Ford: shortest paths from src; returns dist array or null if negative cycle. */
    public static int[] bellmanFord(int n, int[][] edges, int src) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE / 2); dist[src] = 0;
        for (int i = 0; i < n - 1; i++)
            for (var e : edges) if (dist[e[0]] + e[2] < dist[e[1]]) dist[e[1]] = dist[e[0]] + e[2];
        for (var e : edges) if (dist[e[0]] + e[2] < dist[e[1]]) return null;
        return dist;
    }

    /** Floyd-Warshall: all-pairs shortest paths (returns n x n matrix). */
    public static int[][] floydWarshall(int n, int[][] edges) {
        int[][] d = new int[n][n];
        for (int i = 0; i < n; i++) Arrays.fill(d[i], Integer.MAX_VALUE / 2);
        for (int i = 0; i < n; i++) d[i][i] = 0;
        for (var e : edges) d[e[0]][e[1]] = e[2];
        for (int k = 0; k < n; k++)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (d[i][k] + d[k][j] < d[i][j]) d[i][j] = d[i][k] + d[k][j];
        return d;
    }

    /** A* shortest path on a grid using Manhattan heuristic. */
    public static OptionalInt astar(char[][] grid, int[] start, int[] end) {
        int m = grid.length, n = grid[0].length;
        var open = new PriorityQueue<int[]>(Comparator.comparingInt(a -> a[2]));
        open.add(new int[]{start[0], start[1], manhattan(start[0], start[1], end)});
        var g = new HashMap<String, Integer>(); g.put(start[0] + "," + start[1], 0);
        var seen = new HashSet<String>();
        while (!open.isEmpty()) {
            var cur = open.poll();
            String key = cur[0] + "," + cur[1];
            if (seen.contains(key)) continue; seen.add(key);
            if (cur[0] == end[0] && cur[1] == end[1]) return OptionalInt.of(cur[2] - manhattan(cur[0], cur[1], end));
            for (int[] d : new int[][]{{1,0},{-1,0},{0,1},{0,-1}}) {
                int nr = cur[0] + d[0], nc = cur[1] + d[1];
                if (nr < 0 || nc < 0 || nr >= m || nc >= n || grid[nr][nc] == '#') continue;
                int ng = g.get(key) + 1;
                String nk = nr + "," + nc;
                if (ng < g.getOrDefault(nk, Integer.MAX_VALUE)) {
                    g.put(nk, ng); open.add(new int[]{nr, nc, ng + manhattan(nr, nc, end)});
                }
            }
        }
        return OptionalInt.empty();
    }
    private static int manhattan(int r, int c, int[] end) { return Math.abs(r - end[0]) + Math.abs(c - end[1]); }

    /** Flood Fill (BFS) — replaces target color with new color, returns the grid. */
    public static int[][] floodFill(int[][] image, int sr, int sc, int color) {
        int target = image[sr][sc];
        if (target == color) return image;
        var q = new ArrayDeque<int[]>(); q.add(new int[]{sr, sc});
        while (!q.isEmpty()) {
            var p = q.poll();
            if (image[p[0]][p[1]] != target) continue;
            image[p[0]][p[1]] = color;
            for (int[] d : new int[][]{{1,0},{-1,0},{0,1},{0,-1}}) {
                int nr = p[0] + d[0], nc = p[1] + d[1];
                if (nr >= 0 && nc >= 0 && nr < image.length && nc < image[0].length && image[nr][nc] == target)
                    q.add(new int[]{nr, nc});
            }
        }
        return image;
    }

    // ---- Search extras (Q38 interpolation, Q39 quickselect, Q40 inversions) ----
    /** Q38: interpolation search (works well on uniformly distributed sorted data). */
    public static int interpolationSearch(int[] a, int key) {
        int lo = 0, hi = a.length - 1;
        while (lo <= hi && key >= a[lo] && key <= a[hi]) {
            if (lo == hi) return a[lo] == key ? lo : -1;
            int pos = lo + (key - a[lo]) * (hi - lo) / (a[hi] - a[lo]);
            if (a[pos] == key) return pos;
            if (a[pos] < key) lo = pos + 1; else hi = pos - 1;
        }
        return -1;
    }

    /** Q39: kth smallest element (quickselect, average O(n)). */
    public static int kthSmallest(int[] a, int k) {
        var list = Arrays.stream(a).boxed().collect(Collectors.toList());
        return quickselect(list, k - 1);
    }
    private static int quickselect(List<Integer> l, int k) {
        if (l.size() == 1) return l.get(0);
        int pivot = l.get(l.size() / 2);
        var lo = new ArrayList<Integer>();
        var hi = new ArrayList<Integer>();
        var eq = new ArrayList<Integer>();
        for (int x : l) { if (x < pivot) lo.add(x); else if (x > pivot) hi.add(x); else eq.add(x); }
        if (k < lo.size()) return quickselect(lo, k);
        if (k < lo.size() + eq.size()) return pivot;
        return quickselect(hi, k - lo.size() - eq.size());
    }

    /** Q40: count inversions in an array (merge-sort based, O(n log n)). */
    public static int countInversions(int[] a) {
        return mergeSortCount(a, 0, a.length - 1, new int[a.length]);
    }
    private static int mergeSortCount(int[] a, int l, int r, int[] tmp) {
        if (l >= r) return 0;
        int mid = (l + r) / 2;
        int cnt = mergeSortCount(a, l, mid, tmp) + mergeSortCount(a, mid + 1, r, tmp);
        int i = l, j = mid + 1, k = l;
        while (i <= mid && j <= r) {
            if (a[i] <= a[j]) tmp[k++] = a[i++];
            else { tmp[k++] = a[j++]; cnt += mid - i + 1; }
        }
        while (i <= mid) tmp[k++] = a[i++];
        while (j <= r) tmp[k++] = a[j++];
        for (int x = l; x <= r; x++) a[x] = tmp[x];
        return cnt;
    }

    // ---- More sorts (selection, insertion) ----
    /** A21: selection sort. */
    public static void selectionSort(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++) if (a[j] < a[min]) min = j;
            int t = a[i]; a[i] = a[min]; a[min] = t;
        }
    }

    /** A22: insertion sort. */
    public static void insertionSort(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int key = a[i], j = i - 1;
            while (j >= 0 && a[j] > key) { a[j + 1] = a[j]; j--; }
            a[j + 1] = key;
        }
    }

    // ---- Number theory (LCM, Euler's totient) ----
    /** A23: least common multiple via GCD (long-safe). */
    public static long lcm(long a, long b) { return a / gcd((int) a, (int) b) * b; }

    /** A24: Euler's totient function φ(n). */
    public static int eulerTotient(int n) {
        int result = n;
        for (int p = 2; p * p <= n; p++) {
            if (n % p == 0) { while (n % p == 0) n /= p; result -= result / p; }
        }
        if (n > 1) result -= result / n;
        return result;
    }

    // ---- String algorithms (KMP, Rabin-Karp, Trie) ----
    /** A25: KMP substring search — returns first index or -1. */
    public static int kmpSearch(String text, String pat) {
        int n = text.length(), m = pat.length();
        int[] lps = new int[m];
        for (int i = 1, len = 0; i < m; ) {
            if (pat.charAt(i) == pat.charAt(len)) lps[i++] = ++len;
            else if (len != 0) len = lps[len - 1]; else i++;
        }
        for (int i = 0, j = 0; i < n; ) {
            if (text.charAt(i) == pat.charAt(j)) { i++; j++; if (j == m) return i - m; }
            else if (j != 0) j = lps[j - 1]; else i++;
        }
        return -1;
    }

    /** A26: Rabin-Karp substring search (rolling hash, base 31, mod 1e9+7). */
    public static int rabinKarp(String text, String pat) {
        final int B = 31, MOD = 1_000_000_007;
        long ph = 0, pw = 1;
        for (char c : pat.toCharArray()) { ph = (ph * B + c) % MOD; pw = (pw * B) % MOD; }
        long th = 0;
        for (int i = 0; i < text.length(); i++) {
            th = (th * B + text.charAt(i)) % MOD;
            if (i >= pat.length()) th = (th - pw * text.charAt(i - pat.length()) % MOD + MOD) % MOD;
            if (i >= pat.length() - 1 && th == ph && text.substring(i - pat.length() + 1, i + 1).equals(pat))
                return i - pat.length() + 1;
        }
        return -1;
    }

    /** A27: Trie (prefix tree) for autocomplete-style insert/search. */
    public static class Trie {
        private static class Node { Map<Character, Node> ch = new HashMap<>(); boolean end = false; }
        private final Node root = new Node();
        public void insert(String w) {
            Node cur = root;
            for (char c : w.toCharArray()) cur = cur.ch.computeIfAbsent(c, k -> new Node());
            cur.end = true;
        }
        public boolean search(String w) {
            Node cur = root;
            for (char c : w.toCharArray()) { cur = cur.ch.get(c); if (cur == null) return false; }
            return cur.end;
        }
        public boolean startsWith(String pre) {
            Node cur = root;
            for (char c : pre.toCharArray()) { cur = cur.ch.get(c); if (cur == null) return false; }
            return true;
        }
    }

    // ---- Backtracking (subsets, permutations, N-queens) ----
    /** A28: all subsets (power set) via backtracking. */
    public static List<List<Integer>> subsets(int[] nums) {
        var res = new ArrayList<List<Integer>>(); var cur = new ArrayList<Integer>();
        backtrackSubsets(nums, 0, cur, res);
        return res;
    }
    private static void backtrackSubsets(int[] nums, int i, List<Integer> cur, List<List<Integer>> res) {
        if (i == nums.length) { res.add(new ArrayList<>(cur)); return; }
        cur.add(nums[i]); backtrackSubsets(nums, i + 1, cur, res); cur.remove(cur.size() - 1);
        backtrackSubsets(nums, i + 1, cur, res);
    }

    /** A29: all permutations via backtracking. */
    public static List<List<Integer>> permutations(int[] nums) {
        var res = new ArrayList<List<Integer>>();
        var used = new boolean[nums.length];
        backtrackPerm(nums, new ArrayList<>(), used, res);
        return res;
    }
    private static void backtrackPerm(int[] nums, List<Integer> cur, boolean[] used, List<List<Integer>> res) {
        if (cur.size() == nums.length) { res.add(new ArrayList<>(cur)); return; }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            used[i] = true; cur.add(nums[i]); backtrackPerm(nums, cur, used, res); cur.remove(cur.size() - 1); used[i] = false;
        }
    }

    /** A30: N-queens — number of valid placements on an n x n board. */
    public static int nQueens(int n) {
        int[] cols = new int[n]; int[] count = {0};
        placeQueens(n, 0, cols, new boolean[n], new boolean[2 * n], new boolean[2 * n], count);
        return count[0];
    }
    private static void placeQueens(int n, int r, int[] cols, boolean[] usedC, boolean[] diag1, boolean[] diag2, int[] count) {
        if (r == n) { count[0]++; return; }
        for (int c = 0; c < n; c++) {
            if (usedC[c] || diag1[r + c] || diag2[r - c + n]) continue;
            cols[r] = c; usedC[c] = diag1[r + c] = diag2[r - c + n] = true;
            placeQueens(n, r + 1, cols, usedC, diag1, diag2, count);
            usedC[c] = diag1[r + c] = diag2[r - c + n] = false;
        }
    }

    // ---- Segment Tree (point update, range sum query) ----
    /** A31: segment tree for range-sum + point update. */
    public static class SegmentTree {
        private final int[] tree; private final int n;
        public SegmentTree(int[] a) {
            n = a.length; tree = new int[4 * n]; build(a, 1, 0, n - 1);
        }
        private void build(int[] a, int node, int l, int r) {
            if (l == r) { tree[node] = a[l]; return; }
            int m = (l + r) / 2;
            build(a, 2 * node, l, m); build(a, 2 * node + 1, m + 1, r);
            tree[node] = tree[2 * node] + tree[2 * node + 1];
        }
        public void update(int idx, int val) { update(1, 0, n - 1, idx, val); }
        private void update(int node, int l, int r, int idx, int val) {
            if (l == r) { tree[node] = val; return; }
            int m = (l + r) / 2;
            if (idx <= m) update(2 * node, l, m, idx, val); else update(2 * node + 1, m + 1, r, idx, val);
            tree[node] = tree[2 * node] + tree[2 * node + 1];
        }
        public int query(int ql, int qr) { return query(1, 0, n - 1, ql, qr); }
        private int query(int node, int l, int r, int ql, int qr) {
            if (ql > r || qr < l) return 0;
            if (ql <= l && r <= qr) return tree[node];
            int m = (l + r) / 2;
            return query(2 * node, l, m, ql, qr) + query(2 * node + 1, m + 1, r, ql, qr);
        }
    }
}

