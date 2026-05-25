package com.kitchen.app;

import java.io.IOException;
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

        // // duplicate test
        // int duplicateId = 45;
        // Recipe duplicateRecipe = new Recipe();
        // duplicateRecipe.setId(duplicateId);
        // duplicateRecipe.setName(recipes.get(duplicateId).getName());
        // duplicateRecipe.setCategory(recipes.get(duplicateId).getCategory());
        // duplicateRecipe.setPrepTime(recipes.get(duplicateId).getPrepTime());
        // duplicateRecipe.setCost(recipes.get(duplicateId).getCost());
        // duplicateRecipe.setDifficulty(recipes.get(duplicateId).getDifficulty());
        // duplicateRecipe.setRating(recipes.get(duplicateId).getRating());
        // duplicateRecipe.setOrderCount(recipes.get(duplicateId).getOrderCount());
        // duplicateRecipe.setIngredients(List.of("bananas maduras", "farinha de trigo", "açúcar", "manteiga", "ovos", "bicarbonato de sódio"));
        // recipes.add(duplicateRecipe);
        // int duplicateId2 = 46;
        // Recipe duplicateRecipe2 = new Recipe();
        // duplicateRecipe2.setId(duplicateId2);
        // duplicateRecipe2.setName(recipes.get(duplicateId2).getName());
        // duplicateRecipe2.setCategory(recipes.get(duplicateId2).getCategory());
        // duplicateRecipe2.setPrepTime(recipes.get(duplicateId2).getPrepTime());
        // duplicateRecipe2.setCost(recipes.get(duplicateId2).getCost());
        // duplicateRecipe2.setDifficulty(recipes.get(duplicateId2).getDifficulty());
        // duplicateRecipe2.setRating(recipes.get(duplicateId2).getRating());
        // duplicateRecipe2.setOrderCount(recipes.get(duplicateId2).getOrderCount());
        // duplicateRecipe2.setIngredients(List.of("massa de biscoito", "cream cheese", "açúcar", "ovos", "morangos"));
        // recipes.add(duplicateRecipe2);

        MenuController controller = new MenuController(recipeBook, recipes);

        controller.start();
    }
}