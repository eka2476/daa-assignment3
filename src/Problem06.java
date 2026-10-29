import java.util.Arrays;

public class Problem06 {
    public int countDependencyGroups(int n, int[][] dependencies) {
        int m = dependencies.length;
        int[] head = new int[n], next = new int[m], to = new int[m];
        Arrays.fill(head, -1);
        for (int i = 0; i < m; i++) {
            int a = dependencies[i][0], b = dependencies[i][1];
            to[i] = b;
            next[i] = head[a];
            head[a] = i;
        }

        int[] idx = new int[n];
        int[] low = new int[n];
        Arrays.fill(idx, -1);
        boolean[] onStack = new boolean[n];
        int[] tarjanStack = new int[n];
        int tsp = 0;
        int[] callStack = new int[n];
        int[] it = new int[n];
        for (int i = 0; i < n; i++) it[i] = head[i];

        int counter = 0, groups = 0;
        for (int s = 0; s < n; s++) {
            if (idx[s] != -1) continue;
            int csp = 0;
            callStack[csp++] = s;
            idx[s] = low[s] = counter++;
            tarjanStack[tsp++] = s;
            onStack[s] = true;

            while (csp > 0) {
                int u = callStack[csp - 1];
                if (it[u] != -1) {
                    int e = it[u];
                    it[u] = next[e];
                    int v = to[e];
                    if (idx[v] == -1) {
                        idx[v] = low[v] = counter++;
                        tarjanStack[tsp++] = v;
                        onStack[v] = true;
                        callStack[csp++] = v;
                    } else if (onStack[v]) {
                        low[u] = Math.min(low[u], idx[v]);
                    }
                } else {
                    if (low[u] == idx[u]) {
                        groups++;
                        int w;
                        do {
                            w = tarjanStack[--tsp];
                            onStack[w] = false;
                        } while (w != u);
                    }
                    csp--;
                    if (csp > 0) {
                        int parent = callStack[csp - 1];
                        low[parent] = Math.min(low[parent], low[u]);
                    }
                }
            }
        }
        return groups;
    }
}
