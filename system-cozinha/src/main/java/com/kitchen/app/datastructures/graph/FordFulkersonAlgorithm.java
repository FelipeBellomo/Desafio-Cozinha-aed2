package com.kitchen.app.datastructures.graph;


import com.kitchen.app.datastructures.graph.LogisticsGraph;
import com.kitchen.app.datastructures.graph.Node;
import com.kitchen.app.datastructures.graph.Route;

public class FordFulkersonAlgorithm {

    public static int calculateMaxCapacity(LogisticsGraph graph, int source, int sink) {
        int numLocations = graph.getNumLocations();

        // build the residual graph using a matrix to easily manage back-edges
        int[][] residualGraph = new int[numLocations][numLocations];

        for (int u = 0; u < numLocations; u++) {
            Node<Route> current = graph.getConnections(u).getHead();
            while (current != null) {
                Route route = current.getData();
                int v = route.getDestinationId();
                // initialize residual capacity with the original route capacity
                residualGraph[u][v] = route.getCapacity();
                current = current.getNext();
            }
        }

        int[] parent = new int[numLocations];
        int maxFlow = 0;

        // augment the flow while there is a path from source to sink
        while (true) {
            boolean[] visited = new boolean[numLocations];

            // try to find an augmenting path using DFS
            if (!dfs(residualGraph, source, sink, visited, parent, numLocations)) {
                break; 
            }

            // find the maximum flow through the path found
            int pathFlow = Integer.MAX_VALUE;
            int v = sink;
            while (v != source) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, residualGraph[u][v]);
                v = parent[v];
            }

            // update residual capacities of the edges and reverse edges
            v = sink;
            while (v != source) {
                int u = parent[v];
                residualGraph[u][v] -= pathFlow;
                residualGraph[v][u] += pathFlow;
                v = parent[v];
            }

            // add path flow to overall max flow
            maxFlow += pathFlow;
        }

        return maxFlow;
    }

    private static boolean dfs(int[][] residualGraph, int current, int sink, boolean[] visited, int[] parent, int numLocations) {
        visited[current] = true;

        if (current == sink) {
            return true;
        }

        for (int v = 0; v < numLocations; v++) {
            if (!visited[v] && residualGraph[current][v] > 0) {
                parent[v] = current;
                if (dfs(residualGraph, v, sink, visited, parent, numLocations)) {
                    return true;
                }
            }
        }
        return false;
    }
}
