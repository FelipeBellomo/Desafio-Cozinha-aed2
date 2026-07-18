package com.kitchen.app.datastructures.graph;

import com.kitchen.app.datastructures.graph.CustomList;
import com.kitchen.app.datastructures.graph.LogisticsGraph;
import com.kitchen.app.datastructures.graph.Node;
import com.kitchen.app.datastructures.graph.Route;

public class DijkstraAlgorithm {

    /**
     * Calculates the shortest time/distance from a starting location to all other points.
     * Returns an array where the index is the destination ID and the value is the minimum time.
     */
    public static double[] calculateShortestTimes(LogisticsGraph graph, int startLocation) {
        int n = graph.getNumLocations();
        double[] shortestTimes = new double[n];
        boolean[] visited = new boolean[n];

        // Initialize all distances to "Infinity"
        for (int i = 0; i < n; i++) {
            shortestTimes[i] = Double.MAX_VALUE;
        }

        // Time to start location is exactly 0
        shortestTimes[startLocation] = 0;

        for (int i = 0; i < n - 1; i++) {
            // Find the unvisited node with the smallest known distance
            int u = getMinDistanceNode(shortestTimes, visited, n);

            // If the remaining nodes are unreachable, we stop
            if (u == -1) break;

            visited[u] = true;

            // Check all neighbors of the current node
            Node<Route> current = graph.getConnections(u).getHead();
            while (current != null) {
                Route route = current.getData();
                int v = route.getDestinationId();
                double weight = route.getWeight();

                // If a shorter path is found, relax the edge (update the distance)
                if (!visited[v] && shortestTimes[u] != Double.MAX_VALUE
                        && shortestTimes[u] + weight < shortestTimes[v]) {

                    shortestTimes[v] = shortestTimes[u] + weight;
                }
                current = current.getNext();
            }
        }

        return shortestTimes;
    }

    // Helper method to find the next node to process without using Java's PriorityQueue
    private static int getMinDistanceNode(double[] shortestTimes, boolean[] visited, int n) {
        double min = Double.MAX_VALUE;
        int minIndex = -1;

        for (int v = 0; v < n; v++) {
            if (!visited[v] && shortestTimes[v] <= min) {
                min = shortestTimes[v];
                minIndex = v;
            }
        }
        return minIndex;
    }
}