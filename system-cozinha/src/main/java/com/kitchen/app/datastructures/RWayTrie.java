package com.kitchen.app.datastructures;

public class RWayTrie<V> {
    private TrieNode<V> root; // Agora o tipo V é passado explicitamente
    private int size;

    public RWayTrie() {
        root = new TrieNode<>();
        size = 0;
    }

    public void put(String key, V value) {
        if (key == null) throw new IllegalArgumentException("Chave nula.");

        TrieNode<V> current = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (current.next[c] == null) {
                current.next[c] = new TrieNode<>();
            }
            current = current.next[c];
        }

        if (current.val == null && value != null) size++;
        else if (current.val != null && value == null) size--;

        current.val = value;
    }

    public V get(String key) {
        if (key == null) throw new IllegalArgumentException("Chave nula.");

        TrieNode<V> current = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            current = current.next[c];
            if (current == null) return null;
        }
        return current.val; // Não precisa mais de cast!
    }

    public int size() {
        return size;
    }
}