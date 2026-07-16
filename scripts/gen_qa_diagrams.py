#!/usr/bin/env python3
# Generates a mermaid .mmd + renders PNG for every test method in dsa-java-gradleqa.
# PNG only (no SVG). One diagram per method, showing the algorithm's core flow + test anchor.
import os, subprocess, glob

BASE = "/home/deeone/Desktop/jobhunting/dsa/dsa-java-gradleqa"
DOCS = os.path.join(BASE, "docs/diagrams")
os.makedirs(DOCS, exist_ok=True)

PUP = subprocess.check_output(
    "find /home/deeone/.cache/puppeteer/chrome-headless-shell -name chrome-headless-shell -type f | head -1",
    shell=True, text=True).strip()

# method -> (title, [steps])  ; steps become a vertical flowchart (TD)
# Keep node text ASCII-safe (no [], {}, (), =, quotes, apostrophes).
M = {
 # ---- Questions Q1-Q40 ----
 "Q1_maxSumSubarray": ("Kadane max subarray sum", ["Start max cur at 0","For each x cur = max of x and cur plus x","max = max of max and cur","Return max"]),
 "Q2_allPalindromicSubstrings": ("All palindromic substrings", ["For center c in 0..2n","Expand odd length around c","Expand even length around c","Collect substrings that match"]),
 "Q3_twoSum": ("Two Sum", ["Put num to index in map","For each x look for target minus x","If found return pair","Else store x"]),
 "Q4_kadaneViaPrefixSums": ("Kadane via prefix sums", ["Track running prefix sum","minPrefix = min so far","ans = max of prefix minus minPrefix","Return ans"]),
 "Q5_missingNumber": ("Missing number", ["Xor 0..n","Xor all array values","Remaining xor is missing","Return it"]),
 "Q6_groupAnagrams": ("Group anagrams", ["For each word sort chars as key","Group by key in map","Return grouped lists","Stream to list"]),
 "Q6_mergeTwoSorted": ("Merge two sorted arrays", ["i j at 0","While both pick smaller","Append and advance","Append remainder"]),
 "Q7_maxArea": ("Container max area", ["left 0 right n-1","area = min of h times width","Shrink smaller height side","Track max area"]),
 "Q8_firstUniqueChar": ("First unique char", ["Count freq per char","Scan for count 1","Return its index","Else -1"]),
 "Q9_threeSum": ("3Sum", ["Sort array","Fix i scan j k","Skip duplicates","Collect triples sum 0"]),
 "Q9_removeDuplicates": ("Remove duplicates in-place", ["i slow pointer","j fast pointer","If a j != a i copy","Return slow plus 1"]),
 "Q10_lengthOfLongestSubstring": ("Longest substring no repeat", ["Map char to last index","left = max left and prev plus 1","len = i minus left plus 1","Track max"]),
 "Q11_isPalindrome": ("Valid palindrome", ["Two pointers lo hi","Skip non-alnum","Compare lowercased","Return equal"]),
 "Q12_longestCommonPrefix": ("Longest common prefix", ["Prefix = first word","Trim while not startsWith","Return prefix","Empty if none"]),
 "Q13_isValidParentheses": ("Valid parentheses", ["Stack","Push open","Pop on close match","Empty at end"]),
 "Q14_runLengthEncode": ("Run-length encode", ["i at 0","Count run of same char","Append count char","Advance i"]),
 "Q15_binarySearch": ("Binary search", ["lo 0 hi n-1","mid = lo plus hi over 2","Compare target","Halve range"]),
 "Q16_searchRotated": ("Search in rotated sorted", ["Find rotation pivot","Binary search both halves","Compare with mid","Return index or -1"]),
 "Q16_arrayStack": ("Array-backed stack", ["int array","push increments top","pop returns top","isEmpty checks top"]),
 "Q17_firstBadVersion": ("First bad version", ["lo 1 hi n","mid","If isBad mid else mid-1","Return lo"]),
 "Q17_minStack": ("Min stack", ["Two stacks vals and mins","push updates min","pop pops both","getMin reads top"]),
 "Q18_medianOfSorted": ("Median of two sorted", ["Partition both arrays","Left half all <= right","Balance sizes","Median from mids"]),
 "Q18_circularQueue": ("Circular queue", ["Fixed array head tail","Enqueue at tail mod n","Dequeue at head mod n","Full when size n"]),
 "Q19_reverseList": ("Reverse linked list", ["prev null cur head","next = cur next","cur next = prev","Advance"]),
 "Q19_maxStack": ("Max stack", ["Stack of pairs value max","push updates max","pop pops pair","getMax reads top max"]),
 "Q20_hasCycle": ("Linked list cycle", ["Slow fast pointers","fast steps 2","Meet => cycle","Null => none"]),
 "Q20_queueWithStacks": ("Queue with two stacks", ["inStack outStack","Push to in","Pop from out refill","Return front"]),
 "Q21_mergeTwoLists": ("Merge two sorted lists", ["Dummy head","Pick smaller node","Advance","Attach remainder"]),
 "Q22_removeNthFromEnd": ("Remove nth from end", ["Fast ahead n","Slow with dummy","Advance both","Unlink node"]),
 "Q22_lca": ("Lowest common ancestor", ["If null or equal return","If target in left go left","Else go right","Return found"]),
 "Q23_maxDepth": ("Max tree depth", ["If null depth 0","Left = depth left","Right = depth right","Return 1 plus max"]),
 "Q23_isValidBST": ("Validate BST", ["If null true","In-order prev <= cur","Track prev","No violation => true"]),
 "Q24_inorder": ("In-order traversal", ["If null return","Recurse left","Visit node","Recurse right"]),
 "Q24_serializeTree": ("Serialize binary tree", ["Queue BFS","Visit node add val","Add children","Level order list"]),
 "Q25_isSameTree": ("Same tree", ["Both null true","Values equal","Recurse left right","Else false"]),
 "Q26_numIslands": ("Number of islands", ["Grid scan","On land DFS sink","Count starts","Return count"]),
 "Q26_diameter": ("Tree diameter", ["DFS returns depth","diameter = left plus right","Track max","Return depth"]),
 "Q27_cloneGraph": ("Clone graph", ["Map old to new","BFS clone neighbors","Link copies","Return clone"]),
 "Q27_mirror": ("Mirror tree", ["If null return","Swap left right","Recurse both","Return root"]),
 "Q28_canFinish": ("Course schedule (topo)", ["Build graph indegree","Queue indegree 0","Remove edges","All removed?"]),
 "Q29_dijkstra": ("Dijkstra shortest path", ["Dist 0 others inf","Min-heap source","Relax neighbors","Return dist"]),
 "Q30_shortestPath": ("BFS shortest path unweighted", ["Queue start","Mark visited","Expand neighbors","Return dist"]),
 "Q31_fib": ("Fibonacci", ["Base 0 1","Iterate a b","c = a plus b","Shift"]),
 "Q31_cycleUndirected": ("Undirected cycle", ["Union-find edges","Same root => cycle","Union else","Return flag"]),
 "Q32_climbStairs": ("Climb stairs", ["dp0 dp1 1","For i step","cur = dp0 plus dp1","Shift"]),
 "Q32_bipartite": ("Bipartite graph", ["Color map","BFS alternate colors","Conflict => false","Return true"]),
 "Q33_coinChange": ("Coin change min coins", ["dp inf init","dp 0 = 0","For each coin update","Return dp or -1"]),
 "Q33_connectedComponents": ("Connected components", ["Union-find","For each edge union","Count roots","Return count"]),
 "Q34_lengthOfLIS": ("Longest increasing subseq", ["tails list","Binary search insert","Replace or append","Len = tails size"]),
 "Q34_bridges": ("Bridge edges Tarjan", ["DFS disc low","low v from child","low u from back edge","low v > disc u => bridge"]),
 "Q35_editDistance": ("Edit distance", ["dp i j","Match or 3 choices","Min plus 1","Return dp"]),
 "Q36_knapsack": ("0-1 knapsack", ["dp weight 0","For each item","Take or skip","Return dp W"]),
 "Q37_singleNumber": ("Single number (xor)", ["result 0","Xor all","Duplicates cancel","Return result"]),
 "Q38_countBits": ("Count bits (popcount)", ["dp 0 = 0","dp i = dp i and i-1 plus 1","Fill array","Return bits"]),
 "Q38_interpolationSearch": ("Interpolation search", ["lo hi","pos by value ratio","Compare","Narrow range"]),
 "Q39_isPowerOfTwo": ("Is power of two", ["n > 0","n and n-1 == 0","Return bool","Else false"]),
 "Q39_kthSmallEst": ("Kth smallest (quickselect)", ["Pick pivot","Partition around pivot","Recurse side","Return kth"]),
 "Q40_reverseBits": ("Reverse bits", ["result 0","For 32 bits shift","Set bit if set","Return result"]),
 "Q40_countInversions": ("Count inversions (merge)", ["Merge sort","Count split inv","Add left right","Return total"]),
 # ---- Algorithms A1-A31 ----
 "A1_bubbleSort": ("Bubble sort", ["For i n","For j n-i-1","Swap if a j > a j+1","Repeat passes"]),
 "A2_mergeSort": ("Merge sort", ["Mid split","Recurse halves","Merge sorted","Return"]),
 "A3_quickSort": ("Quick sort", ["Pick pivot","Partition","Recurse sides","In-place"]),
 "A4_heapSort": ("Heap sort", ["Build max-heap","Swap root end","Heapify down","Repeat"]),
 "A5_bfs": ("Breadth-first search", ["Queue start","Mark visited","Expand neighbors","Enqueue"]),
 "A6_dfs": ("Depth-first search", ["Stack or recurse","Mark visited","Go deep","Backtrack"]),
 "A7_unionFind": ("Union-Find", ["Parent array","Find with path compress","Union by rank","Connected?"]),
 "A8_sieve": ("Sieve of Eratosthenes", ["Mark true","For p p*p<=n","Cross multiples","Count primes"]),
 "A9_gcd": ("GCD (Euclid)", ["While b != 0","temp = b","b = a mod b","a = temp"]),
 "A10_modPow": ("Modular exponentiation", ["result 1","While e>0","Square base","Multiply if odd"]),
 "A11_slidingWindowMax": ("Sliding window maximum", ["Deque indices","Maintain decreasing","Drop out-of-window","Front is max"]),
 "A12_topoSort": ("Topological sort", ["Indegree count","Queue zero","Remove edges","Append order"]),
 "A13_kruskal": ("Kruskal MST", ["Sort edges","Union-find","Skip if cycle","Add edge"]),
 "A14_pow": ("Fast power", ["result 1","While n>0","Square base","Multiply if odd"]),
 "A15_lcs": ("Longest common subsequence", ["dp i j","Match diag plus 1","Else max up left","Return dp"]),
 "A16_levelOrder": ("Level-order traversal", ["Queue root","While not empty","Poll add children","Per level"]),
 "A17_prim": ("Prim MST", ["Start node","Min-heap edges","Add unvisited","Grow tree"]),
 "A18_matrixChain": ("Matrix chain multiply", ["dp i j","Split k","Min cost","Return dp"]),
 "A19_lowestSetBit": ("Lowest set bit", ["n and -n","Returns isolated bit","Mask","Return value"]),
 "A20_clearLowestSetBit": ("Clear lowest set bit", ["n and n-1","Drops lowest 1","Return value","Bit trick"]),
 "A21_selectionSort": ("Selection sort", ["For i n","Find min in rest","Swap","Repeat"]),
 "A22_insertionSort": ("Insertion sort", ["For i 1..n","Key = a i","Shift larger left","Insert"]),
 "A23_lcm": ("LCM via GCD", ["a / gcd times b","Long-safe","Return lcm","Number theory"]),
 "A24_eulerTotient": ("Euler totient", ["result n","Factor primes","Multiply by 1-1/p","Return"]),
 "A25_kmp": ("KMP search", ["Build LPS","Scan text","Mismatch use LPS","Match at end"]),
 "A26_rabinKarp": ("Rabin-Karp search", ["Hash pattern","Slide window","Compare hash","Verify match"]),
 "A27_trie": ("Trie insert/search", ["Root node","Follow chars","Mark end","Search prefix"]),
 "A28_subsets": ("Generate subsets (backtrack)", ["Result list","Choose include","Recurse","Exclude"]),
 "A29_permutations": ("Permutations (backtrack)", ["Swap elements","Recurse","Backtrack swap","Collect"]),
 "A30_nQueens": ("N-Queens", ["Place row","Check attacks","Recurse next","Count solutions"]),
 "A31_segmentTree": ("Segment tree range sum", ["Build from array","Query range","Update point","Push up"]),
}

def sanitize(s):
    # mermaid-safe: drop chars that break node text
    for ch in ["[","]","{","}","(",")","=","\"","'",":","/","\\","#","@","*","+","&","|","<",">","%","$"]:
        s = s.replace(ch, " ")
    return " ".join(s.split())

count = 0
for method, (title, steps) in M.items():
    # dedupe steps if tuple slipped in (Q23 fix)
    if isinstance(steps, tuple):
        steps = list(steps)
    lines = ["graph TD"]
    prev = "START"
    lines.append(f'    START["{sanitize(title)}"]')
    for i, st in enumerate(steps):
        node = f"N{i}"
        lines.append(f'    {node}["{sanitize(st)}"]')
        lines.append(f"    {prev} --> {node}")
        prev = node
    lines.append(f'    {prev} --> END["assert passes in JUnit"]')
    mmd = os.path.join(DOCS, method + ".mmd")
    with open(mmd, "w") as f:
        f.write("\n".join(lines) + "\n")
    count += 1

print(f"wrote {count} mermaid sources")
