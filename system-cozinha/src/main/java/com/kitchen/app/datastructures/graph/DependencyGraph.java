package com.kitchen.app.datastructures.graph;

import com.kitchen.app.datastructures.graph.CustomList;
import com.kitchen.app.datastructures.graph.GraphStack;
import com.kitchen.app.datastructures.graph.Node;

public class DependencyGraph {
    private int numRecipes;
    // Agora o Grafo usa a lista universal guardando Inteiros
    private CustomList<Integer>[] adjacencies;

    @SuppressWarnings("unchecked")
    public DependencyGraph(int numRecipes) {
        this.numRecipes = numRecipes;
        // Workaround do Java para criar arrays de tipos Genéricos
        this.adjacencies = new CustomList[numRecipes];

        for (int i = 0; i < numRecipes; i++) {
            adjacencies[i] = new CustomList<>();
        }
    }

    public void addDependency(int prerequisiteId, int finalRecipeId) {
        adjacencies[prerequisiteId].add(finalRecipeId);
    }

    public GraphStack getDependencyCycle() {
        int[] state = new int[numRecipes];
        int[] parent = new int[numRecipes];

        for (int i = 0; i < numRecipes; i++) {
            parent[i] = -1;
        }

        for (int i = 0; i < numRecipes; i++) {
            if (state[i] == 0) {
                GraphStack cycle = dfsCycleCheck(i, state, parent);
                if (cycle != null) {
                    return cycle;
                }
            }
        }
        return null;
    }

    private GraphStack dfsCycleCheck(int vertex, int[] state, int[] parent) {
        state[vertex] = 1;

        // Repare no Node<Integer> aqui!
        Node<Integer> current = adjacencies[vertex].getHead();
        while (current != null) {
            int neighbor = current.getData();

            if (state[neighbor] == 1) {
                GraphStack cycleStack = new GraphStack(numRecipes);
                int curr = vertex;
                while (curr != neighbor && curr != -1) {
                    cycleStack.push(curr);
                    curr = parent[curr];
                }
                cycleStack.push(neighbor);
                return cycleStack;
            }

            if (state[neighbor] == 0) {
                parent[neighbor] = vertex;
                GraphStack cycle = dfsCycleCheck(neighbor, state, parent);
                if (cycle != null) {
                    return cycle;
                }
            }
            current = current.getNext();
        }

        state[vertex] = 2;
        return null;
    }

    public GraphStack getCorrectSequence() {
        if (getDependencyCycle() != null) {
            return null;
        }

        int[] state = new int[numRecipes];
        GraphStack sequence = new GraphStack(numRecipes);

        for (int i = 0; i < numRecipes; i++) {
            if (state[i] == 0) {
                dfsTopologicalSort(i, state, sequence);
            }
        }

        return sequence;
    }

    private void dfsTopologicalSort(int vertex, int[] state, GraphStack sequence) {
        state[vertex] = 1;

        Node<Integer> current = adjacencies[vertex].getHead();
        while (current != null) {
            int neighbor = current.getData();
            if (state[neighbor] == 0) {
                dfsTopologicalSort(neighbor, state, sequence);
            }
            current = current.getNext();
        }

        state[vertex] = 2;
        sequence.push(vertex);
    }
}