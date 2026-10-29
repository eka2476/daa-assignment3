import java.util.ArrayDeque;
import java.util.Deque;

public class Problem05 {
    public int countDependencyGroups(int n, int[][] dependencies) {
        int m = dependencies.length;

        int[] headG = new int[n], nextG = new int[m], toG = new int[m];
        int[] headT = new int[n], nextT = new int[m], toT = new int[m];
        java.util.Arrays.fill(headG, -1);
        java.util.Arrays.fill(headT, -1);
        for (int i = 0; i < m; i++) {
            int a = dependencies[i][0], b = dependencies[i][1];
            toG[i] = b;
            nextG[i] = headG[a];
            headG[a] = i;
            toT[i] = a;
            nextT[i] = headT[b];
            headT[b] = i;
        }

        boolean[] visited = new boolean[n];
        int[] order = new int[n];
        int cnt = 0;
        int[] it = new int[n]; 
        for (int i = 0; i < n; i++) it[i] = headG[i];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int s = 0; s < n; s++) {
            if (visited[s]) continue;
            visited[s] = true;
            stack.push(s);
            while (!stack.isEmpty()) {
                int u = stack.peek();
                if (it[u] != -1) {
                    int e = it[u];
                    it[u] = nextG[e];
                    int v = toG[e];
                    if (!visited[v]) {
                        visited[v] = true;
                        stack.push(v);
                    }
                } else {
                    stack.pop();
                    order[cnt++] = u; // u finished
                }
            }
        }

        boolean[] seen = new boolean[n];
        int groups = 0;
        for (int idx = n - 1; idx >= 0; idx--) {
            int s = order[idx];
            if (seen[s]) continue;
            groups++;
            seen[s] = true;
            stack.push(s);
            while (!stack.isEmpty()) {
                int u = stack.pop();
                for (int e = headT[u]; e != -1; e = nextT[e]) {
                    int v = toT[e];
                    if (!seen[v]) {
                        seen[v] = true;
                        stack.push(v);
                    }
                }
            }
        }
        return groups;
    }
}
