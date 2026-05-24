package com.kitchen.app.datastructures;

public class RWayTrie {

    private TrieNode root;

    public RWayTrie() {
        root = new TrieNode();
    }

    /**
     * Inserts the recipe into the Trie.
     *
     * @param normalizedName The string used for routing (lowercase, no accents)
     * @param originalName The original string to be displayed
     * @param id The recipe ID
     */
    public void insert(String normalizedName, String originalName, int id) {
        TrieNode current = root;

        for (int i = 0; i < normalizedName.length(); i++) {
            char c = normalizedName.charAt(i);

            // If the child node for this letter doesn't exist yet, we create it
            if (current.children[c] == null) {
                current.children[c] = new TrieNode();
            }
            current = current.children[c]; // Move down to the next node
        }

        current.isEndOfWord = true;
        current.originalName = originalName;
        current.recipeId = id;
    }

    /**
     * Searches and prints suggestions directly (avoiding Java's ArrayList).
     * Returns the amount of results found.
     *
     * @param normalizedPrefix The prefix typed by the user (normalized)
     * @return Number of recipes found
     */
    public int autocomplete(String normalizedPrefix) {
        TrieNode current = root;

        // 1. Navigate to the end of the prefix
        for (int i = 0; i < normalizedPrefix.length(); i++) {
            char c = normalizedPrefix.charAt(i);

            if (current.children[c] == null) {
                return 0; // Prefix does not exist in the tree
            }
            current = current.children[c];
        }

        // 2. Depth-First Search to find the words
        return collectWords(current);
    }

    /**
     * Depth-First Search (DFS). Prints the result and returns how many were found.
     */
    private int collectWords(TrieNode node) {
        int count = 0;

        if (node.isEndOfWord) {
            System.out.println(" -> [ID: " + node.recipeId + "] " + node.originalName);
            count++;
        }

        // Iterate through the 256 array positions looking for valid paths
        for (int i = 0; i < 256; i++) {
            if (node.children[i] != null) {
                count += collectWords(node.children[i]);
            }
        }

        return count;
    }
}