package com.kitchen.app.services;

import java.util.ArrayList;
import java.util.List;

import com.kitchen.app.model.Recipe;
import com.kitchen.app.model.RecipeBook;

public class QuickSearchService {

    private final RecipeBook recipeBook;

    public QuickSearchService(RecipeBook recipeBook) {
        this.recipeBook = recipeBook;
    }

    public Recipe searchById(int id) {
        return recipeBook.findRecipeById(id);
    }

    public List<String> searchByPrefix(String prefix) {
        return recipeBook.searchByPrefix(prefix);
    }

    public List<Recipe> searchByCategory(String category) {
        return recipeBook.searchByCategory(category);
    }

    public List<Recipe> searchByIngredient(String ingredient) {
        return recipeBook.searchByIngredient(ingredient);
    }

    public List<Recipe> searchRecipesByPrefix(String prefix) {
        List<String> recipeNames = searchByPrefix(prefix);
        List<Recipe> recipes = new ArrayList<>();

        for (String name : recipeNames) {
            for (Recipe recipe : recipeBook.searchByCategory(recipeBook.findRecipeById(recipeBook
                    .getSearchTrie()
                    .get(name.toLowerCase()))
                    .getCategory())) {
                if (recipe.getName().equalsIgnoreCase(name)) {
                    recipes.add(recipe);
                }
            }
        }

        return recipes;
    }

}