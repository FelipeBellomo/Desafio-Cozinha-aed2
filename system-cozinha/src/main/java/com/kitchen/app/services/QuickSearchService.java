package com.kitchen.app.services;

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

}