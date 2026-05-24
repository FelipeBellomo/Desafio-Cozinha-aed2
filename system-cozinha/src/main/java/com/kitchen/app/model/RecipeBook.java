package com.kitchen.app.model;

import com.kitchen.app.datastructures.BTree;
import com.kitchen.app.datastructures.HashTable;
import com.kitchen.app.datastructures.RWayTrie;
import com.kitchen.app.util.RecipeStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeBook {

    private RWayTrie<Integer> searchTrie;
    private HashTable<Integer, String> integrityHashTable;
    private HashTable<String, List<Recipe>> categoryIndex;
    private HashTable<String, List<Recipe>> ingredientIndex;
    private BTree bTree;
    private RecipeStorage recipeStorage;

    public RecipeBook() throws IOException {
        this.searchTrie = new RWayTrie<>();
        this.integrityHashTable = new HashTable<>();
        this.categoryIndex = new HashTable<>();
        this.ingredientIndex = new HashTable<>();
        this.bTree = new BTree("db_recipes.bin", 4);
        this.recipeStorage = new RecipeStorage("recipes.dat");
    }

    public void initializeSystem(List<Recipe> loadedRecipes) throws IOException {
        for (Recipe recipe : loadedRecipes) {
            // stores the original hash to verify tampering later
            integrityHashTable.put(recipe.getId(), recipe.generateIntegrityHash());

            // inserts the name into the trie (converted to lowercase for standard the
            // searching)
            searchTrie.put(recipe.getName().toLowerCase(), recipe.getId());

            addToCategoryIndex(recipe);
            addToIngredientIndex(recipe);
            
            long position = recipeStorage.saveRecipe(recipe);

            bTree.insert(recipe.getId(), position);
        }
        System.out.println("Sistema inicializado com " + loadedRecipes.size() + " receitas na memória RAM.");
    }

    public Recipe findRecipeById(int id) {
        try {
            Long position = bTree.search(id);

            if (position == null)
                return null;

            return recipeStorage.loadRecipe(position);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addToCategoryIndex(Recipe recipe) {
        String category = recipe.getCategory().toLowerCase();

        List<Recipe> recipes = categoryIndex.get(category);

        if (recipes == null) {
            recipes = new ArrayList<>();
            categoryIndex.put(category, recipes);
        }

        recipes.add(recipe);
    }

    private void addToIngredientIndex(Recipe recipe) {
        for (String ingredient : recipe.getIngredients()) {
            ingredient = ingredient.toLowerCase();
            List<Recipe> recipes = ingredientIndex.get(ingredient);
            if (recipes == null) {
                recipes = new ArrayList<>();
                ingredientIndex.put(ingredient, recipes);
            }
            recipes.add(recipe);
        }
    }

    public List<String> searchByPrefix(String prefix) {
        return searchTrie.keysWithPrefix(prefix.toLowerCase());
    }

    public List<Recipe> searchByCategory(String category) {
        List<Recipe> result = categoryIndex.get(category.toLowerCase());

        if (result == null) {
            return new ArrayList<>();
        }

        return result;
    }

    public List<Recipe> searchByIngredient(String ingredient) {
        List<Recipe> result = ingredientIndex.get(ingredient.toLowerCase());

        if (result == null) {
            return new ArrayList<>();
        }

        return result;
    }

    public RWayTrie<Integer> getSearchTrie() {
        return searchTrie;
    }

    public HashTable<Integer, String> getIntegrityHashTable() {
        return integrityHashTable;
    }

    public BTree getBTree() {
        return bTree;
    }
}