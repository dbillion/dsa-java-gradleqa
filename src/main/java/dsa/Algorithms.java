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
}
