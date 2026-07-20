package com.kitchen.app.datastructures.graph;


public class LogisticsGraph {
    private int numLocations;
    // O pulo do gato: A mesma lista, agora guardando Rotas!
    private CustomList<Route>[] adjacencies;

    @SuppressWarnings("unchecked")
    public LogisticsGraph(int numLocations) {
        this.numLocations = numLocations;
        this.adjacencies = new CustomList[numLocations];

        for (int i = 0; i < numLocations; i++) {
            adjacencies[i] = new CustomList<>();
        }
    }

    public void addTwoWayRoute(int locationA, int locationB, double weight, int capacity) {
        adjacencies[locationA].add(new Route(locationB, weight, capacity));
        adjacencies[locationB].add(new Route(locationA, weight, capacity));
    }

    public void addOneWayRoute(int source, int destination, double weight, int capacity) {
        adjacencies[source].add(new Route(destination, weight, capacity));
    }

    public CustomList<Route> getConnections(int locationId) {
        return adjacencies[locationId];
    }

    public int getNumLocations() {
        return numLocations;
    }
}
