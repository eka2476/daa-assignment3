import java.util.Arrays;

public class Problem04 {
    public int minCostConnectPoints(int[][] points) {
        int n = points.length;
        if (n <= 1) return 0;

        int[] minDist = new int[n];
        boolean[] inTree = new boolean[n];
        Arrays.fill(minDist, Integer.MAX_VALUE);
        minDist[0] = 0;

        int total = 0;
        for (int step = 0; step < n; step++) {
            int u = -1;
            for (int v = 0; v < n; v++) {
                if (!inTree[v] && (u == -1 || minDist[v] < minDist[u])) {
                    u = v;
                }
            }
            inTree[u] = true;
            total += minDist[u];

            for (int v = 0; v < n; v++) {
                if (!inTree[v]) {
                    int w = Math.abs(points[u][0] - points[v][0])
                          + Math.abs(points[u][1] - points[v][1]);
                    if (w < minDist[v]) minDist[v] = w;
                }
            }
        }
        return total;
    }
}
