import java.util.ArrayList;
import java.util.List;

public class Problem07 {
    private List<List<Integer>> graph;
    private int[] state;

    public boolean canFinish(int numCourses, int[][] prerequisites) {
        graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) graph.add(new ArrayList<>());
        for (int[] p : prerequisites) {
            graph.get(p[1]).add(p[0]);
        }
        state = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            if (state[i] == 0 && hasCycle(i)) return false;
        }
        return true;
    }

    private boolean hasCycle(int u) {
        state[u] = 1;
        for (int v : graph.get(u)) {
            if (state[v] == 1) return true;
            if (state[v] == 0 && hasCycle(v)) return true;
        }
        state[u] = 2;
        return false;
    }
}
