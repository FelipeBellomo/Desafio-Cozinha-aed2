package com.kitchen.app.model;

import com.kitchen.app.datastructures.HashTable;
import com.kitchen.app.datastructures.RWayTrie;
import java.util.List;

public class RecipeBook {

    private RWayTrie<Integer> searchTrie;
    private HashTable<Integer, String> integrityHashTable;
    
    public RecipeBook() {
        this.searchTrie = new RWayTrie<>();
        this.integrityHashTable = new HashTable<>();
    }

    public void initializeSystem(List<Recipe> loadedRecipes) {
        for (Recipe recipe : loadedRecipes) {
            // stores the original hash to verify tampering later
            integrityHashTable.put(recipe.getId(), recipe.generateIntegrityHash());

            // inserts the name into the trie (converted to lowercase for standard the searching)
            searchTrie.put(recipe.getName().toLowerCase(), recipe.getId());

            // BTree.insert(recipe) .... will be here;
        }
        System.out.println("Sistema inicializado com " + loadedRecipes.size() + " receitas na memória RAM.");
    }

    public RWayTrie<Integer> getSearchTrie() { return searchTrie; }
    public HashTable<Integer, String> getIntegrityHashTable() { return integrityHashTable; }
}