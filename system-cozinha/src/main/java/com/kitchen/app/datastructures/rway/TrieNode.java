package com.kitchen.app.datastructures.rway;

class TrieNode<V> {
    private static final int R = 256;

    V val;
    final TrieNode<V>[] next;

    @SuppressWarnings("unchecked")
    TrieNode() {
        this.next = new TrieNode[R];
    }
}