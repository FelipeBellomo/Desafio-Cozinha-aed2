package com.kitchen.app.model;

import java.util.ArrayList;
import java.util.List;

public class ChefRecommender {

    /**
     * Greedy Algorithm: Builds a menu maximizing the rating within a given budget.
     */
    public List<Recipe> generateMenuByBudget(List<Recipe> allRecipes, double maxBudget) {
        List<Recipe> recommendedMenu = new ArrayList<>();
        double remainingBudget = maxBudget;

        // 1. Create a copy to avoid mutating the original list's order
        List<Recipe> candidates = new ArrayList<>(allRecipes);

        // 2. The GREEDY step: Sort by heuristic (Rating / Cost) in descending order
        candidates.sort((r1, r2) -> {
            // Avoid division by zero in case any recipe has a cost of 0
            double cost1 = r1.getCost() > 0 ? r1.getCost() : 0.01;
            double cost2 = r2.getCost() > 0 ? r2.getCost() : 0.01;

            double valueRatio1 = r1.getRating() / cost1;
            double valueRatio2 = r2.getRating() / cost2;

            // Descending order (from highest value ratio to lowest)
            return Double.compare(valueRatio2, valueRatio1);
        });

        // 3. Iterate and pick the best local choice until the budget runs out
        for (Recipe recipe : candidates) {
            if (recipe.getCost() <= remainingBudget) {
                recommendedMenu.add(recipe);
                remainingBudget -= recipe.getCost();
            }

            // Optimization: stop iterating if there is no budget left
            if (remainingBudget <= 0) {
                break;
            }
        }

        return recommendedMenu;
    }
}
