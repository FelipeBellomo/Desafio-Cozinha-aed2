package com.kitchen.app.datastructures;

import java.util.ArrayList;
import java.util.List;

public class RWayTrie<V> {
    private TrieNode<V> root; // Agora o tipo V é passado explicitamente
    private int size;

    public RWayTrie() {
        root = new TrieNode<>();
        size = 0;
    }

    public void put(String key, V value) {
        if (key == null)
            throw new IllegalArgumentException("Chave nula.");

        key = key.toLowerCase();
        
        TrieNode<V> current = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (current.next[c] == null) {
                current.next[c] = new TrieNode<>();
            }
            current = current.next[c];
        }

        if (current.val == null && value != null)
            size++;
        else if (current.val != null && value == null)
            size--;

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

    private TrieNode<V> getNode(String key) {
        TrieNode<V> current = root;

        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            current = current.next[c];
            if (current == null)
                return null;
        }

        return current;
    }

    public boolean contains(String key){
        return getNode(key)!=null;
    }

    public List<String> keysWithPrefix(String prefix) {
        List<String> results = new ArrayList<>();

        prefix = prefix.toLowerCase();

        TrieNode<V> node = getNode(prefix);

        if (node == null)
            return results;

        collect(node, new StringBuilder(prefix), results);

        return results;
    }

    private void collect(TrieNode<V> node, StringBuilder prefix, List<String> results) {
        if (node == null)
            return;

        if (node.val != null)
            results.add(prefix.toString());

        for (char c = 0; c < 256; c++) {
            if (node.next[c] != null) {
                prefix.append(c);
                collect(node.next[c], prefix, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}