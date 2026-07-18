package com.kitchen.app.datastructures.graph;

public class GraphStack {
    private int[] elements;
    private int topIndex;

    public GraphStack(int capacity) {
        this.elements = new int[capacity];
        this.topIndex = -1;
    }

    public void push(int value) {
        elements[++topIndex] = value;
    }

    public int pop() {
        if (topIndex >= 0) {
            return elements[topIndex--];
        }
        return -1; // Returns -1 if the stack is empty
    }

    public boolean isEmpty() {
        return topIndex == -1;
    }
}
