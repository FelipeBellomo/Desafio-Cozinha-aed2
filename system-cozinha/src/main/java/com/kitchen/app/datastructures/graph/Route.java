package com.kitchen.app.datastructures.graph;


public class Route {
    private int destinationId;
    private double weight; 
    private int capacity; 

    public Route(int destinationId, double weight, int capacity) {
        this.destinationId = destinationId;
        this.weight = weight;
        this.capacity = capacity;
    }

    public int getDestinationId() { return destinationId; }
    public double getWeight() { return weight; }
    public int getCapacity() { return capacity; }

    public void setCapacity(int capacity) { this.capacity = capacity; }
}
