package com.kitchen.app.datastructures.graph;



import com.kitchen.app.datastructures.graph.LogisticsGraph;
import com.kitchen.app.datastructures.graph.Node;
import com.kitchen.app.datastructures.graph.Route;

public class PrimAlgorithm {

    // Helper class to return both the connections (parents) and the weights
    public static class MSTResult {
        public int[] parents;
        public double[] edgeWeights;

        public MSTResult(int[] parents, double[] edgeWeights) {
            this.parents = parents;
            this.edgeWeights = edgeWeights;
        }
    }

    /**
     * Calculates the Minimum Spanning Tree (MST) to find the minimum infrastructure network.
     * Uses Prim's Algorithm without relying on built-in PriorityQueue.
     */
    public static MSTResult calculateMinimumInfrastructure(LogisticsGraph graph) {
        int n = graph.getNumLocations();
        double[] minWeight = new double[n];
        int[] parent = new int[n];
        boolean[] inMST = new boolean[n];

        // 1. Initialize all weights as "Infinity" and parents as -1
        for (int i = 0; i < n; i++) {
            minWeight[i] = Double.MAX_VALUE;
            parent[i] = -1;
        }

        // 2. Start from the main restaurant (ID 0)
        minWeight[0] = 0;

        // 3. Process all vertices
        for (int i = 0; i < n - 1; i++) {
            // Find the unvisited node with the smallest edge connection available
            int u = getMinWeightNode(minWeight, inMST, n);

            // If the graph is disconnected, we stop to avoid errors
            if (u == -1) break;

            inMST[u] = true;

            // 4. Update adjacent nodes
            Node<Route> current = graph.getConnections(u).getHead();
            while (current != null) {
                Route route = current.getData();
                int v = route.getDestinationId();
                double weight = route.getWeight();

                // If vertex 'v' is NOT in MST and the edge weight is strictly smaller
                // than its current assigned minimum weight, we update it.
                if (!inMST[v] && weight < minWeight[v]) {
                    parent[v] = u;
                    minWeight[v] = weight;
                }
                current = current.getNext();
            }
        }

        return new MSTResult(parent, minWeight);
    }

    // Helper method to find the next node with the minimum edge weight
    private static int getMinWeightNode(double[] minWeight, boolean[] inMST, int n) {
        double min = Double.MAX_VALUE;
        int minIndex = -1;

        for (int v = 0; v < n; v++) {
            if (!inMST[v] && minWeight[v] <= min) {
                min = minWeight[v];
                minIndex = v;
            }
        }
        return minIndex;
    }
}
