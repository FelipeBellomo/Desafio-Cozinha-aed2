package com.kitchen.app.services;

import com.kitchen.app.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class ChefService {
    private List<Recipe> filterRecipes(List<Recipe> recipes, int maxPrepTime, double maxCost, int maxDifficulty) {
        List<Recipe> filtered = new ArrayList<>();

        for (Recipe recipe : recipes) {
            if (recipe.getPrepTime() <= maxPrepTime && recipe.getCost() <= maxCost
                    && recipe.getDifficulty() <= maxDifficulty) {
                filtered.add(recipe);
            }
        }

        return filtered;
    }

    private double calculateScore(Recipe recipe) {
        return (recipe.getRating() * 10)
                + (recipe.getOrderCount() * 0.5)
                - (recipe.getCost() * 0.3)
                - (recipe.getPrepTime() * 0.2)
                - (recipe.getDifficulty() * 2);
    }

    public Recipe recommendBestRecipe(List<Recipe> recipes, int maxPrepTime, double maxCost, int maxDifficulty) {
        List<Recipe> filtered = filterRecipes(recipes, maxPrepTime, maxCost, maxDifficulty);

        if (filtered.isEmpty())
            return null;

        Recipe bestRecipe = filtered.get(0);
        double bestScore = calculateScore(bestRecipe);

        for (Recipe recipe : filtered) {
            double currentScore = calculateScore(recipe);

            if (currentScore > bestScore) {
                bestScore = currentScore;
                bestRecipe = recipe;
            }
        }

        return bestRecipe;
    }

    public List<Recipe> recommendTopRecipes(List<Recipe> recipes, int maxPrepTime, double maxCost, int maxDifficulty, int limit) {

        List<Recipe> filtered = filterRecipes(recipes, maxPrepTime, maxCost, maxDifficulty);

        filtered.sort((a, b) -> Double.compare(calculateScore(b), calculateScore(a)));

        if (filtered.size() > limit)
            return filtered.subList(0, limit);

        return filtered;
    }
}