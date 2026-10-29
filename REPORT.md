### DAA Assignment3 - Report and Defense (50 points)

Notation: `n` / `V` - number of vertices, `m` / `E` - number of edges. All solutions are in [src/](src/).
Every solution was tested on the examples from the statements and on thousands of random small cases
against brute-force references (transitive closure, naive Boruvka MST), plus large inputs (`n = 10^5`) for stack safety.

##### Problem 1 and 2 - Number of Provinces (10 points)

**Model.** `isConnected` is the adjacency matrix of an undirected graph. A province is a connected component,
so the answer is the number of connected components.

**Problem 1 - DFS** ([Problem01.java](src/Problem01.java))
1. Keep a `visited[]` array.
2. For every unvisited city start a new DFS (explicit stack) and mark everything reachable. Each such start = one new province.
3. The count of DFS starts is the answer.

I used an iterative DFS with an explicit stack instead of recursion, so it can't overflow the call stack on a long chain.

- Time: `O(n^2)` - the matrix row of every vertex is scanned once. Space: `O(n)` for `visited` and the stack.

**Problem 2 - DSU** ([Problem02.java](src/Problem02.java))
1. Start with `n` components (each city is its own set).
2. For every pair `i < j` with `isConnected[i][j] == 1` call `union(i, j)`.
3. If `union` really merged two different sets, decrement the component counter.
4. The counter at the end is the answer.

Optimizations: path halving in `find` and union by size, which together give `O(alpha(n))` amortized per operation
(`alpha` is the inverse Ackermann function, practically constant).

- Time: `O(n^2 * alpha(n))` (reading the matrix dominates). Space: `O(n)`.

**Comparison.** Both are `O(n^2)` because the input is a matrix. DFS is simpler for a static graph;
DSU is better when edges arrive online (incremental connectivity) and needs no adjacency structure.

##### Problem 3 and 4 - Min Cost Spanning Tree (10 points)

**Model.** Points are vertices of a complete graph, edge weight = Manhattan distance
`|x1 - x2| + |y1 - y2|`. We need the weight of a minimum spanning tree (MST) with `n - 1` edges.
Correctness of both algorithms follows from the **cut property**: the lightest edge crossing any cut belongs to some MST.

**Problem 3 - Kruskal** ([Problem03.java](src/Problem03.java))
1. Generate all `n(n-1)/2` edges with their weights.
2. Sort edges by weight.
3. Go through the sorted edges; take an edge if its endpoints are in different DSU components (no cycle), add its weight, union them.
4. Stop after `n - 1` edges were taken.

- Time: `O(E log E) = O(n^2 log n)` (sorting; `log E = O(log n)`), DSU part is almost linear. Space: `O(n^2)` for the edge list.

**Problem 4 - Prim** ([Problem04.java](src/Problem04.java))
1. Start the tree from vertex 0; `minDist[v]` = cheapest known edge from `v` to the tree.
2. Repeat `n` times: pick the vertex outside the tree with the smallest `minDist`, add it to the tree and add its `minDist` to the answer.
3. Relax: for every vertex outside the tree update `minDist[v] = min(minDist[v], dist(u, v))`.

For a complete graph the array (no heap) version is better than a priority queue: a heap would give `O(E log V) = O(n^2 log n)`, the array gives `O(n^2)`.

- Time: `O(n^2)`. Space: `O(n)` (edges are computed on the fly, never stored).

**Comparison.** Same answer, different trade-off: Kruskal is edge-centric and good for sparse graphs, but here `E = Theta(n^2)`
so sorting costs an extra `log n` and memory is `O(n^2)`. Prim (dense version) is faster and uses `O(n)` memory on this problem.

##### Problem 5 and 6 - Software Dependency Cycles (10 points)

**Model.** Modules are vertices, `[a, b]` is a directed edge `a -> b`. A circular dependency group
(every module reaches every other one) is exactly a **strongly connected component (SCC)**; a module without a cycle is a singleton SCC.
So the answer is the number of SCCs. Self loops and duplicate edges don't change the count.
With `n` up to `10^5` recursion could overflow the Java stack, so both solutions are **iterative**.

**Problem 5 - Kosaraju** ([Problem05.java](src/Problem05.java))
1. DFS on the original graph `G`; push each vertex to a list when it **finishes**.
2. Build the transpose graph `G^T` (all edges reversed).
3. Process vertices in **decreasing finish time**; each unvisited vertex starts a DFS on `G^T`. The set reached is one SCC, count it.

Why it works: the vertex with the latest finish time lies in a source component of the condensation DAG.
In `G^T` edges between components are reversed, so DFS from it can't leave its own component. Removing it and repeating peels components one by one.

- Time: `O(n + m)` (two DFS passes + building `G^T`). Space: `O(n + m)`.

**Problem 6 - Tarjan** ([Problem06.java](src/Problem06.java))
1. One DFS. For every vertex keep `idx[v]` (discovery order) and `low[v]` (smallest `idx` reachable from the subtree of `v` using one edge to a vertex still on the stack).
2. Push the vertex on a separate stack when discovered.
3. For an edge `u -> v`: if `v` is new, go deeper, then `low[u] = min(low[u], low[v])`; if `v` is on the stack, `low[u] = min(low[u], idx[v])`; if `v` is already assigned to an SCC, ignore it.
4. When `low[u] == idx[u]`, `u` is the root of an SCC: pop the stack until `u` - those vertices form one SCC, count it.

- Time: `O(n + m)` with a **single** DFS pass and no transpose graph. Space: `O(n + m)`.

**Comparison.** Same complexity. Kosaraju is easier to explain and prove but needs the reversed graph (extra memory, two passes);
Tarjan does one pass and needs only the `idx/low` arrays but its correctness argument (low-link) is subtler.

##### Problem 7 and 8 - Course Schedule (10 points)

**Model.** Courses are vertices; prerequisite `[a, b]` ("take `b` before `a`") is an edge `b -> a`.
All courses can be finished **iff the graph has no directed cycle** (it is a DAG).

**Problem 7 - DFS with cycle detection** ([Problem07.java](src/Problem07.java))
1. Three colors: `0` unvisited, `1` on the current DFS path (gray), `2` fully processed (black).
2. During DFS, an edge to a **gray** vertex is a back edge -> a cycle exists -> return `false`.
3. When all neighbors of a vertex are explored, color it black. Black vertices are never re-explored, so every vertex/edge is handled once.
4. No cycle found in any DFS -> `true`.

The recursion depth is at most `V <= 2000` (LeetCode constraint), which is safe in Java.

- Time: `O(V + E)`. Space: `O(V + E)` (adjacency lists + recursion stack).

**Problem 8 - Kahn's algorithm** ([Problem08.java](src/Problem08.java))
1. Compute `indegree[]` for every vertex.
2. Put all vertices with `indegree == 0` (courses with no prerequisites) in a queue.
3. Pop a vertex, count it as processed, decrement the indegree of its neighbors; neighbors that reach 0 go to the queue.
4. If the number of processed vertices equals `numCourses` -> `true`; otherwise the remaining vertices lie on or behind a cycle (their indegree never reaches 0) -> `false`.

- Time: `O(V + E)`. Space: `O(V + E)`.

**Comparison.** DFS detects the cycle directly via a back edge and is recursive; Kahn is iterative (no stack-depth issue)
and detects a cycle indirectly by "not all vertices could be removed".

##### Problem 9 and 10 - Course Schedule II (10 points)

**Model.** Same graph as above, but now we must return a valid order of courses, i.e. a **topological order**; if a cycle exists, return an empty array.
Any valid order is accepted (it is not unique).

**Problem 9 - DFS with reverse finishing order** ([Problem09.java](src/Problem09.java))
1. Run the same 3-color DFS as in Problem 7. If a gray vertex is met -> cycle -> return `new int[0]`.
2. When a vertex becomes black (all descendants finished), append it to the `finish` array.
3. The answer is `finish` **reversed**.

Why it works: for every edge `u -> v` the vertex `v` finishes before `u` (no back edges in a DAG),
so `u` is placed before `v` after reversal - every prerequisite precedes the course that needs it.

- Time: `O(V + E)`. Space: `O(V + E)`.

**Problem 10 - Kahn's algorithm** ([Problem10.java](src/Problem10.java))
1. Same as Problem 8, but write every vertex to the result array at the moment it is popped from the queue.
2. If fewer than `numCourses` vertices were popped -> cycle -> return `new int[0]`; otherwise return the array.

The pop order is a topological order because a vertex enters the queue only after all its prerequisites were already popped.

- Time: `O(V + E)`. Space: `O(V + E)`.

**Comparison.** Both give a valid (possibly different) order. DFS builds the order "from the back" via finishing times;
Kahn builds it "from the front" by repeatedly taking available courses, which is also the natural way to simulate the semester-by-semester study plan.

##### Summary table

| # | Problem | Algorithm | Time | Space |
|---|---------|-----------|------|-------|
| 1 | Number of Provinces | DFS (iterative) | O(n^2) | O(n) |
| 2 | Number of Provinces | DSU (path halving + union by size) | O(n^2 α(n)) | O(n) |
| 3 | Min Cost Connect Points | Kruskal + DSU | O(n^2 log n) | O(n^2) |
| 4 | Min Cost Connect Points | Prim (array, dense) | O(n^2) | O(n) |
| 5 | Dependency Cycles | Kosaraju (iterative) | O(n + m) | O(n + m) |
| 6 | Dependency Cycles | Tarjan (iterative) | O(n + m) | O(n + m) |
| 7 | Course Schedule | DFS, 3-color cycle detection | O(V + E) | O(V + E) |
| 8 | Course Schedule | Kahn | O(V + E) | O(V + E) |
| 9 | Course Schedule II | DFS, reverse finishing order | O(V + E) | O(V + E) |
| 10 | Course Schedule II | Kahn | O(V + E) | O(V + E) |
