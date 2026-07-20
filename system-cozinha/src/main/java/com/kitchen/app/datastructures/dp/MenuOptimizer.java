package com.kitchen.app.datastructures.dp;



import java.util.ArrayList;
import java.util.List;
import com.kitchen.app.model.Recipe;

public class MenuOptimizer {

    public static List<Recipe> optimizeMenu(List<Recipe> availableRecipes, int maxCapacity, int constraintType, int optimizationGoal) {
        int n = availableRecipes.size();

        // DP Matrix: Rows = recipes, Columns = capacity
        double[][] dpTable = new double[n + 1][maxCapacity + 1];

        // build the DP Table (using Bottom-Up)
        for (int i = 1; i <= n; i++) {
            Recipe currentRecipe = availableRecipes.get(i - 1);

            int weight = getWeight(currentRecipe, constraintType);
            double value = getValue(currentRecipe, optimizationGoal);

            for (int w = 1; w <= maxCapacity; w++) {
                if (weight <= w) {
                    double includeValue = value + dpTable[i - 1][w - weight];
                    double excludeValue = dpTable[i - 1][w];
                    dpTable[i][w] = Math.max(includeValue, excludeValue);
                } else {
                    dpTable[i][w] = dpTable[i - 1][w];
                }
            }
        }

        // backtracking, trace back to find which recipes were selected
        List<Recipe> optimizedMenu = new ArrayList<>();
        int remainingCapacity = maxCapacity;

        for (int i = n; i > 0 && remainingCapacity > 0; i--) {
            if (dpTable[i][remainingCapacity] != dpTable[i - 1][remainingCapacity]) {
                Recipe chosenRecipe = availableRecipes.get(i - 1);
                optimizedMenu.add(chosenRecipe);
                remainingCapacity -= getWeight(chosenRecipe, constraintType);
            }
        }

        return optimizedMenu;
    }

    // helper method to get the correct "weight" for the knapsack
    private static int getWeight(Recipe recipe, int constraintType) {
        if (constraintType == 1) {
            return recipe.getPrepTime();
        } else {
            // multiply by 100 to handle budget as integers (cents) to fit in the DP array index
            return (int) Math.round(recipe.getCost() * 100);
        }
    }

    // helper method to get the correct "value" to maximize
    private static double getValue(Recipe recipe, int optimizationGoal) {
        if (optimizationGoal == 1) {
            return recipe.getRating();
        } else {
            return recipe.getProfit();
        }
    }
}