package com.kitchen.app.datastructures.graph;


import com.kitchen.app.datastructures.graph.LogisticsGraph;
import com.kitchen.app.datastructures.graph.Node;
import com.kitchen.app.datastructures.graph.Route;

public class FordFulkersonAlgorithm {

    /**
     * Calculates the maximum flow (capacity) from a source to a sink location.
     * Uses Ford-Fulkerson method with a Depth-First Search (DFS) for augmenting paths.
     */
    public static int calculateMaxCapacity(LogisticsGraph graph, int source, int sink) {
        int numLocations = graph.getNumLocations();

        // 1. Build the Residual Graph using a Matrix to easily manage back-edges
        int[][] residualGraph = new int[numLocations][numLocations];

        for (int u = 0; u < numLocations; u++) {
            Node<Route> current = graph.getConnections(u).getHead();
            while (current != null) {
                Route route = current.getData();
                int v = route.getDestinationId();
                // Initialize residual capacity with the original route capacity
                residualGraph[u][v] = route.getCapacity();
                current = current.getNext();
            }
        }

        int[] parent = new int[numLocations];
        int maxFlow = 0;

        // 2. Augment the flow while there is a path from source to sink
        while (true) {
            boolean[] visited = new boolean[numLocations];

            // Try to find an augmenting path using DFS
            if (!dfs(residualGraph, source, sink, visited, parent, numLocations)) {
                break; // Break the loop if no more paths are available
            }

            // 3. Find the maximum flow (bottleneck) through the path found
            int pathFlow = Integer.MAX_VALUE;
            int v = sink;
            while (v != source) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, residualGraph[u][v]);
                v = parent[v];
            }

            // 4. Update residual capacities of the edges and reverse edges (back-tracking)
            v = sink;
            while (v != source) {
                int u = parent[v];
                residualGraph[u][v] -= pathFlow;
                residualGraph[v][u] += pathFlow; // Add capacity to the back-edge
                v = parent[v];
            }

            // Add path flow to overall max flow
            maxFlow += pathFlow;
        }

        return maxFlow;
    }

    // Depth-First Search to find a path in the residual graph
    private static boolean dfs(int[][] residualGraph, int current, int sink, boolean[] visited, int[] parent, int numLocations) {
        visited[current] = true;

        // If we reached the destination (sink), a path was found
        if (current == sink) {
            return true;
        }

        for (int v = 0; v < numLocations; v++) {
            // If the node is unvisited and there is available capacity in the residual graph
            if (!visited[v] && residualGraph[current][v] > 0) {
                parent[v] = current; // Record the path
                if (dfs(residualGraph, v, sink, visited, parent, numLocations)) {
                    return true;
                }
            }
        }
        return false;
    }
}
