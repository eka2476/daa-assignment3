import java.util.Arrays;

public class Problem03 {
    private int[] parent;
    private int[] rank;

    private int find(int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    private boolean union(int a, int b) {
        int ra = find(a), rb = find(b);
        if (ra == rb) return false;
        if (rank[ra] < rank[rb]) {
            int t = ra;
            ra = rb;
            rb = t;
        }
        parent[rb] = ra;
        if (rank[ra] == rank[rb]) rank[ra]++;
        return true;
    }

    public int minCostConnectPoints(int[][] points) {
        int n = points.length;
        if (n <= 1) return 0;

        int[][] edges = new int[n * (n - 1) / 2][];
        int k = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int w = Math.abs(points[i][0] - points[j][0])
                      + Math.abs(points[i][1] - points[j][1]);
                edges[k++] = new int[]{w, i, j};
            }
        }
        Arrays.sort(edges, (a, b) -> Integer.compare(a[0], b[0]));

        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        int total = 0, used = 0;
        for (int[] e : edges) {
            if (union(e[1], e[2])) {
                total += e[0];
                if (++used == n - 1) break;
            }
        }
        return total;
    }
}
