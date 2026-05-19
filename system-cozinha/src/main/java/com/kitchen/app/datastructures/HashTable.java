package com.kitchen.app.datastructures;

public class HashTable<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;
    
    private HashNode<K, V>[] table;
    private int size;
    private int capacity;

    @SuppressWarnings("unchecked")
    public HashTable() {
        size = 0;
        capacity = DEFAULT_CAPACITY;  
        table = new HashNode[this.capacity];
    }

    private int calculateIndex(K key, int currentCapacity) {
        return (key.hashCode() & 0x7FFFFFFF) % currentCapacity;
    }

    public void put(K key, V value) {

        // verify if the load factor exceeds the threshold, if true, resize the table
        if ((size/capacity) >= LOAD_FACTOR_THRESHOLD) {
            resize();
        }

        // apply hash function to calculate the index for the key
        int index = calculateIndex(key , capacity);

        HashNode<K, V> current = table[index];

        while (current != null) {
            // verify if exists the key, if true, update value and return
            if (current.key.equals(key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        // insert new node at the beginning 
        HashNode<K, V> newNode = new HashNode<>(key, value);
        newNode.next = table[index];
        table[index] = newNode;
        size++;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        capacity *= 2;
        HashNode<K, V>[] newTable = new HashNode[capacity];

        for (HashNode<K, V> node : table) {
            while (node != null) {
                int newIndex = calculateIndex(node.key, capacity);
                HashNode<K, V> nextNode = node.next;

                node.next = newTable[newIndex];
                newTable[newIndex] = node;

                node = nextNode;
            }
        }
        table = newTable;
    }

    public V get(K key) {
        int index = calculateIndex(key, capacity);
        HashNode<K, V> current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        return null; 
    }

    public int size() {
        return size;
    }
}
