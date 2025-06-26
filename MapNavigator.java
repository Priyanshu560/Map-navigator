import java.util.*;

class Graph {
    private final Map<String, Map<String, Integer>> adj;

    public Graph() {
        this.adj = new HashMap<>();
    }

    public void addEdge(String source, String destination, int distance) {
        adj.putIfAbsent(source, new HashMap<>());
        adj.putIfAbsent(destination, new HashMap<>());
        adj.get(source).put(destination, distance);
        adj.get(destination).put(source, distance); // bidirectional
    }

    public Map<String, Integer> getNeighbors(String node) {
        return adj.getOrDefault(node, new HashMap<>());
    }

    public Set<String> getNodes() {
        return adj.keySet();
    }
}

class Dijkstra {
    public static Map<String, Integer> computeDistances(Graph graph, String start) {
        Map<String, Integer> distances = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        Set<String> visited = new HashSet<>();

        for (String node : graph.getNodes()) {
            distances.put(node, Integer.MAX_VALUE);
        }

        distances.put(start, 0);
        pq.add(start);

        while (!pq.isEmpty()) {
            String current = pq.poll();
            if (!visited.add(current)) continue;

            for (Map.Entry<String, Integer> neighbor : graph.getNeighbors(current).entrySet()) {
                String next = neighbor.getKey();
                int newDist = distances.get(current) + neighbor.getValue();
                if (newDist < distances.get(next)) {
                    distances.put(next, newDist);
                    pq.add(next);
                }
            }
        }

        return distances;
    }

    public static List<String> getShortestPath(Graph graph, String start, String end) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (String node : graph.getNodes()) {
            distances.put(node, Integer.MAX_VALUE);
        }

        distances.put(start, 0);
        pq.add(start);

        while (!pq.isEmpty()) {
            String current = pq.poll();

            for (Map.Entry<String, Integer> neighbor : graph.getNeighbors(current).entrySet()) {
                String next = neighbor.getKey();
                int newDist = distances.get(current) + neighbor.getValue();
                if (newDist < distances.get(next)) {
                    distances.put(next, newDist);
                    previous.put(next, current);
                    pq.add(next);
                }
            }
        }

        // Reconstruct path
        List<String> path = new ArrayList<>();
        for (String at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }
}

public class MapNavigator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Graph map = new Graph();

        // Sample Map
        map.addEdge("A", "B", 4);
        map.addEdge("A", "C", 2);
        map.addEdge("B", "C", 1);
        map.addEdge("B", "D", 5);
        map.addEdge("C", "D", 8);
        map.addEdge("C", "E", 10);
        map.addEdge("D", "E", 2);
        map.addEdge("D", "Z", 6);
        map.addEdge("E", "Z", 3);

        // Show available nodes
        System.out.println("Available nodes: " + map.getNodes());

        System.out.print("Enter source node: ");
        String source = scanner.nextLine().toUpperCase();

        System.out.print("Enter destination node: ");
        String destination = scanner.nextLine().toUpperCase();

        // Validate nodes
        if (!map.getNodes().contains(source) || !map.getNodes().contains(destination)) {
            System.out.println("❌ Invalid source or destination node.");
            return;
        }

        List<String> path = Dijkstra.getShortestPath(map, source, destination);
        Integer totalDistance = Dijkstra.computeDistances(map, source).get(destination);

        if (totalDistance == Integer.MAX_VALUE) {
            System.out.println("❌ No path exists between " + source + " and " + destination);
        } else {
            System.out.println("\n✅ Shortest path from " + source + " to " + destination + ": " + path);
            System.out.println("📏 Total distance: " + totalDistance + " units");
        }
    }
}
