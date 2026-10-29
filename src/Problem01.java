import java.util.ArrayDeque;
import java.util.Deque;

public class Problem01 {
    public int findCircleNum(int[][] isConnected) {
        int n = isConnected.length;
        boolean[] visited = new boolean[n];
        int provinces = 0;

        for (int start = 0; start < n; start++) {
            if (visited[start]) continue;
            provinces++;
            Deque<Integer> stack = new ArrayDeque<>();
            stack.push(start);
            visited[start] = true;
            while (!stack.isEmpty()) {
                int u = stack.pop();
                for (int v = 0; v < n; v++) {
                    if (isConnected[u][v] == 1 && !visited[v]) {
                        visited[v] = true;
                        stack.push(v);
                    }
                }
            }
        }
        return provinces;
    }
}
