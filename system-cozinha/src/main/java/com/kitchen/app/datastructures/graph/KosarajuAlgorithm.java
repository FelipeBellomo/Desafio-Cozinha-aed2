package com.kitchen.app.datastructures.graph;

public class KosarajuAlgorithm extends DependencyGraph {

    public KosarajuAlgorithm(int numRecipes) {
        super(numRecipes);
    }

    private void dfsForward(int vertex, boolean[] visited, GraphStack stack) {
        visited[vertex] = true;

        CustomList<Integer> neighbors = this.getNeighbors(vertex);
        
        if (neighbors != null) {
            Node<Integer> current = neighbors.getHead();
            while (current != null) {
                int neighbor = current.getData();
                if (!visited[neighbor]) {
                    dfsForward(neighbor, visited, stack);
                }
                current = current.getNext(); 
            }
        }
        
        stack.push(vertex);
    }

    private KosarajuAlgorithm getTransposedGraph() {
        int numVertices = this.getNumVertices();
        KosarajuAlgorithm transposed = new KosarajuAlgorithm(numVertices);

        for (int i = 0; i < numVertices; i++) {
            CustomList<Integer> neighbors = this.getNeighbors(i);
            if (neighbors != null) {
                Node<Integer> current = neighbors.getHead();
                while (current != null) {
                    int neighbor = current.getData();
                    // i -> neighbor ===> neighbor -> i
                    transposed.addDependency(neighbor, i);
                    current = current.getNext();
                }
            }
        }
        return transposed;
    }

    private void dfsReverse(int vertex, boolean[] visited, CustomList<Integer> currentComponent) {
        visited[vertex] = true;
        currentComponent.add(vertex);

        CustomList<Integer> neighbors = this.getNeighbors(vertex);
        if (neighbors != null) {
            Node<Integer> current = neighbors.getHead();
            while (current != null) {
                int neighbor = current.getData();
                if (!visited[neighbor]) {
                    dfsReverse(neighbor, visited, currentComponent);
                }
                current = current.getNext();
            }
        }
    }

    public CustomList<CustomList<Integer>> findDependencyCycles() {
        int numVertices = this.getNumVertices();
        GraphStack stack = new GraphStack(numVertices);
        boolean[] visited = new boolean[numVertices];

        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                dfsForward(i, visited, stack);
            }
        }

        KosarajuAlgorithm transposedGraph = getTransposedGraph();

        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }

        CustomList<CustomList<Integer>> foundCycles = new CustomList<>();

        while (!stack.isEmpty()) {
            int v = stack.pop();

            if (!visited[v]) {
                CustomList<Integer> newSCC = new CustomList<>();
                
                transposedGraph.dfsReverse(v, visited, newSCC);

                int sccSize = 0;
                Node<Integer> nodeCounter = newSCC.getHead();
                while (nodeCounter != null) {
                    sccSize++;
                    nodeCounter = nodeCounter.getNext();
                }

                if (sccSize > 1) {
                    foundCycles.add(newSCC);
                }
            }
        }

        return foundCycles;
    }
}