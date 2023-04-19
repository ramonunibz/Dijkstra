import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Dijkstra {
    public static void main(String[] args) {
        String topology = args[0];
        File file = new File(topology);
        Graph graph = new Graph();
        start(file, graph);
    }

    static Map<String, Integer> distanceFromNode = new HashMap<>();
    static Map<String, String> previousNode = new HashMap<>();
    static Map<String, String> paths = new HashMap<>();


    public static void start(File file, Graph graph) {
        edgesFinder(file, graph);
        nodeSet(graph);
        output(graph);
    }

    public static void output(Graph graph){
        for (String node : graph.nodes) {
            computeDijkstra(graph, node);
            System.out.println("\nRouter " + node + ":");
            System.out.println("Dest, Next hop");
            for (String destination : graph.nodes) {
                if (previousNode.get(destination)==null){
                    System.out.print(destination + "\tdirect\n");
                } else {
                    System.out.print(destination + "\t" + previousNode.get(destination) + "\n");
                }
            }
            File newFile=new File(node+".txt");
            try(FileWriter fileWriter=new FileWriter(newFile)){
                for (String destination : graph.nodes) {
                    if (previousNode.get(destination)==null){
                        fileWriter.write(destination + " direct\n");
                    } else {
                        fileWriter.write(destination + " " + previousNode.get(destination) + "\n");
                    }
                }
            }catch (IOException e){
                System.out.println("File not found to write in");
            }
        }
    }

    public static void edgesFinder(File file, Graph graph) {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String fromNode = scanner.next();
                String toNode = scanner.next();
                int cost = scanner.nextInt();
                graph.edges.add(new Edge(fromNode, toNode, cost));
                graph.edges.add(new Edge(toNode, fromNode, cost));
            }
        } catch (IOException e) {
            System.out.println("File not valid");
        }
    }

    public static void nodeSet(Graph graph) {
        for (Edge edge : graph.edges) {
            graph.nodes.add(edge.fromNode);
        }
    }

    public static void computeDijkstra(Graph graph, String source) {
        paths.clear();
        Set<String> vertexSet = new HashSet<>();
        for (String node : graph.nodes) {
            distanceFromNode.put(node, Integer.MAX_VALUE);
            previousNode.put(node, "dir");
            vertexSet.add(node);
        }

        distanceFromNode.replace(source, 0);

        while (!vertexSet.isEmpty()) {
            String shortestCostNode = getMinKey(distanceFromNode, vertexSet);
            vertexSet.remove(shortestCostNode);
            previousNode.put(shortestCostNode, calculateNextHop(paths, source, shortestCostNode));

            List<String> neighbours = new ArrayList<>();
            for (Edge edge : graph.edges) {
                if (edge.fromNode.equals(shortestCostNode)) {
                    neighbours.add(edge.toNode);
                }
            }

            for (String neighbour : neighbours) {
                if (vertexSet.contains(neighbour)) {
                    int distance = distanceFromNode.get(shortestCostNode) + getDistanceBetweenNodes(shortestCostNode, neighbour, graph);
                    if (distance < distanceFromNode.get(neighbour)) {
                        distanceFromNode.replace(neighbour, distance);
                        paths.put(neighbour, shortestCostNode);
                    }
                }
            }
        }
    }

    public static String getMinKey(Map<String, Integer> map, Set<String> vertexSet) {
        String minKey = null;
        int minValue = Integer.MAX_VALUE;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() < minValue && vertexSet.contains(entry.getKey())) {
                minValue = entry.getValue();
                minKey = entry.getKey();
            }
        }
        return minKey;
    }

    public static int getDistanceBetweenNodes(String node1, String node2, Graph graph) {
        int min = Integer.MAX_VALUE;
        for (Edge edge : graph.edges) {
            if (edge.fromNode.equals(node1) && edge.toNode.equals(node2)) {
                if (edge.cost < min) {
                    min = edge.cost;
                }
            }
        }
        return min;
    }

    public static String calculateNextHop(Map<String, String> map, String source, String current) {
        if (map.get(current) == null) {
            return null;
        } else if (map.get(current).equals(source)) {
            return current;
        }
        else {
            return calculateNextHop(map, source, map.get(current));
        }
    }
}
