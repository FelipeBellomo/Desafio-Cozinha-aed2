package com.kitchen.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.kitchen.app.controller.MenuController;
import com.kitchen.app.model.Recipe;
import com.kitchen.app.model.RecipeBook;
import com.kitchen.app.util.FileLoader;

public class App {

    public static void main(String[] args) throws IOException {
        System.out.println("Inicializando sistema...");

        FileLoader loader = new FileLoader();

        List<Recipe> recipes = loader.loadData("pequenasReceitas.json");

        // conflict test
        // List<Recipe> recipes =
        // new ArrayList<>(
        //         loader.loadData("pequenasReceitas.json")
        // );

        RecipeBook recipeBook = new RecipeBook();

        recipeBook.initializeSystem(recipes);

        // conflict test
        // Recipe conflictRecipe = new Recipe();
        // conflictRecipe.setId(999);
        // conflictRecipe.setName(recipes.get(0).getName());
        // conflictRecipe.setIngredients(List.of("Ingrediente diferente"));
        // recipes.add(conflictRecipe);

        MenuController controller = new MenuController(recipeBook, recipes);

        controller.start();
    }
}