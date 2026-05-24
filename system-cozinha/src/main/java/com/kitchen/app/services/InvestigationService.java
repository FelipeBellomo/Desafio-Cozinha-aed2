package com.kitchen.app.services;

import com.kitchen.app.datastructures.HashTable;
import com.kitchen.app.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class InvestigationService {

    public List<Recipe> detectTamperedRecipes(List<Recipe> recipes, HashTable<Integer, String> integrityTable) {
        List<Recipe> tampered = new ArrayList<>();

        for (Recipe recipe : recipes) {
            String originalHash = integrityTable.get(recipe.getId());
            String currentHash = recipe.generateIntegrityHash();
            if (originalHash != null && !originalHash.equals(currentHash))
                tampered.add(recipe);
        }

        return tampered;
    }

    public List<Recipe> detectDuplicateRecipes(List<Recipe> recipes) {
        List<Recipe> duplicates = new ArrayList<>();
        HashTable<String, Boolean> seen = new HashTable<>();

        for (Recipe recipe : recipes) {
            String signature = recipe.getName().toLowerCase() + recipe.getIngredients().toString();

            if (seen.containsKey(signature)) {
                duplicates.add(recipe);
            } else {
                seen.put(signature, true);
            }
        }
        return duplicates;
    }

    public List<String> detectVersionConflicts(List<Recipe> recipes) {
        List<String> conflicts = new ArrayList<>();
        HashTable<String, String> recipeContents = new HashTable<>();

        for (Recipe recipe : recipes) {
            String recipeName = recipe.getName().toLowerCase();
            String content = recipe.getIngredients().toString();
            String existing = recipeContents.get(recipeName);

            if (existing != null && !existing.equals(content)) {
                conflicts.add("Conflito detectado: " + recipe.getName());
            } else {
                recipeContents.put(recipeName, content);
            }
        }
        return conflicts;
    }

    public List<String> validateRecipes(List<Recipe> recipes) {
        List<String> errors = new ArrayList<>();

        for (Recipe recipe : recipes) {
            if (recipe.getId() <= 0)
                errors.add("ID inválido: " + recipe.getName());

            if (recipe.getName() == null || recipe.getName().isBlank())
                errors.add("Receita sem nome.");

            if (recipe.getIngredients() == null || recipe.getIngredients().isEmpty())
                errors.add("Receita sem ingredientes: " + recipe.getName());

            if (recipe.getCost() < 0)
                errors.add("Custo inválido: " + recipe.getName());

        }

        return errors;
    }
}