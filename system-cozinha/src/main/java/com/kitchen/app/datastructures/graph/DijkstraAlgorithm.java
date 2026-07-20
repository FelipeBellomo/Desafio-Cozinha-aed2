package com.kitchen.app.datastructures.graph;

import com.kitchen.app.datastructures.graph.CustomList;
import com.kitchen.app.datastructures.graph.LogisticsGraph;
import com.kitchen.app.datastructures.graph.Node;
import com.kitchen.app.datastructures.graph.Route;

public class DijkstraAlgorithm {

    public static double[] calculateShortestTimes(LogisticsGraph graph, int startLocation) {
        int n = graph.getNumLocations();
        double[] shortestTimes = new double[n];
        boolean[] visited = new boolean[n];

        // initialize all distances to infinity value
        for (int i = 0; i < n; i++) {
            shortestTimes[i] = Double.MAX_VALUE;
        }

        shortestTimes[startLocation] = 0;

        for (int i = 0; i < n - 1; i++) {
            // find the unvisited node with the smallest known distance
            int u = getMinDistanceNode(shortestTimes, visited, n);

            // if the remaining nodes are unreachable, stop
            if (u == -1) break;

            visited[u] = true;

            // check all neighbors of the current node
            Node<Route> current = graph.getConnections(u).getHead();
            while (current != null) {
                Route route = current.getData();
                int v = route.getDestinationId();
                double weight = route.getWeight();

                // if a shorter path is found, relax the edge
                if (!visited[v] && shortestTimes[u] != Double.MAX_VALUE
                        && shortestTimes[u] + weight < shortestTimes[v]) {

                    shortestTimes[v] = shortestTimes[u] + weight;
                }
                current = current.getNext();
            }
        }

        return shortestTimes;
    }

    // helper method to find the next node to process
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