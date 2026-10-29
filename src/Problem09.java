import java.util.ArrayList;
import java.util.List;

public class Problem09 {
    private List<List<Integer>> graph;
    private int[] state; // 0 white, 1 gray, 2 black
    private int[] finish;
    private int cnt;

    public int[] findOrder(int numCourses, int[][] prerequisites) {
        graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) graph.add(new ArrayList<>());
        for (int[] p : prerequisites) {
            graph.get(p[1]).add(p[0]);
        }
        state = new int[numCourses];
        finish = new int[numCourses];
        cnt = 0;

        for (int i = 0; i < numCourses; i++) {
            if (state[i] == 0 && !dfs(i)) return new int[0];
        }

        int[] order = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            order[i] = finish[numCourses - 1 - i]; 
        }
        return order;
    }

    private boolean dfs(int u) {
        state[u] = 1;
        for (int v : graph.get(u)) {
            if (state[v] == 1) return false;
            if (state[v] == 0 && !dfs(v)) return false;
        }
        state[u] = 2;
        finish[cnt++] = u;
        return true;
    }
}
