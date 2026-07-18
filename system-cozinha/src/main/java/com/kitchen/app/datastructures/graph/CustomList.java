package com.kitchen.app.datastructures.graph;


public class CustomList<T> {
    private Node<T> head;

    public CustomList() {
        this.head = null;
    }

    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.setNext(head);
        head = newNode;
    }

    public Node<T> getHead() {
        return head;
    }
}
