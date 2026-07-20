package com.kitchen.app.datastructures.graph;



import com.kitchen.app.datastructures.graph.LogisticsGraph;
import com.kitchen.app.datastructures.graph.Node;
import com.kitchen.app.datastructures.graph.Route;

public class PrimAlgorithm {

    // helper class to return both the connections (parents) and the weights
    public static class MSTResult {
        public int[] parents;
        public double[] edgeWeights;

        public MSTResult(int[] parents, double[] edgeWeights) {
            this.parents = parents;
            this.edgeWeights = edgeWeights;
        }
    }

   
    public static MSTResult calculateMinimumInfrastructure(LogisticsGraph graph) {
        int n = graph.getNumLocations();
        double[] minWeight = new double[n];
        int[] parent = new int[n];
        boolean[] inMST = new boolean[n];

        for (int i = 0; i < n; i++) {
            minWeight[i] = Double.MAX_VALUE;
            parent[i] = -1;
        }

        minWeight[0] = 0;

        for (int i = 0; i < n - 1; i++) {
            int u = getMinWeightNode(minWeight, inMST, n);

            if (u == -1) break;

            inMST[u] = true;

            Node<Route> current = graph.getConnections(u).getHead();
            while (current != null) {
                Route route = current.getData();
                int v = route.getDestinationId();
                double weight = route.getWeight();

                if (!inMST[v] && weight < minWeight[v]) {
                    parent[v] = u;
                    minWeight[v] = weight;
                }
                current = current.getNext();
            }
        }

        return new MSTResult(parent, minWeight);
    }

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
