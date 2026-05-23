package com.kitchen.app.datastructures;

public class TrieNode {
    // Fixed array of 256 positions to cover basic ASCII characters
    public TrieNode[] children;
    public boolean isEndOfWord;

    // Data for Autocomplete
    public String originalName;
    public int recipeId;

    public TrieNode() {
        this.children = new TrieNode[256];
        this.isEndOfWord = false;
        this.originalName = null;
        this.recipeId = -1;
    }
}