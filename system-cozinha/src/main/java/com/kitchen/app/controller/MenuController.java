package com.kitchen.app.controller;

import java.util.List;
import java.util.Scanner;

import com.kitchen.app.model.Recipe;
import com.kitchen.app.model.RecipeBook;
import com.kitchen.app.services.ChefService;
import com.kitchen.app.services.InvestigationService;
import com.kitchen.app.services.QuickSearchService;

public class MenuController {

    private final Scanner scanner;
    private final RecipeBook recipeBook;
    private final List<Recipe> recipes;
    private final ChefService chefService;
    private final InvestigationService investigationService;
    private final QuickSearchService quickSearchService;

    public MenuController(RecipeBook recipeBook, List<Recipe> recipes) {
        this.recipeBook = recipeBook;
        this.recipes = recipes;
        this.scanner = new Scanner(System.in);
        this.chefService = new ChefService();
        this.investigationService = new InvestigationService();
        this.quickSearchService = new QuickSearchService(recipeBook);
    }

    public void start() {
        int option = -1;

        while (option != 0) {
            showMainMenu();
            option = readInt();

            switch (option) {
                case 1 -> showRecipeModule();
                case 2 -> showQuickSearchModule();
                case 3 -> showChefModule();
                case 4 -> showInvestigationModule();
                case 0 -> System.out.println("\nEncerrando sistema...");
                default -> System.out.println("\nOpção inválida.");
            }
        }

        scanner.close();
    }

    private void showMainMenu() {
        System.out.println("\n=======================================================");
        System.out.println("        DESAFIO NA COZINHA - SISTEMA JACQUIN           ");
        System.out.println("=======================================================");

        System.out.println("1 - Livro de Receitas");
        System.out.println("2 - Consulta Rápida");
        System.out.println("3 - Modo Chef");
        System.out.println("4 - Investigação");
        System.out.println("0 - Sair");

        System.out.print("Escolha: ");
    }

    private int readInt() {
        while (!scanner.hasNextInt()) {
            System.out.println("Digite um número válido.");
            scanner.next();
        }

        return scanner.nextInt();
    }

    private void showRecipeModule() {
        System.out.println("\n===== LIVRO DE RECEITAS =====");

        for (Recipe recipe : recipes)
            System.out.println(recipe);

    }

    // private void showQuickSearchModule() {
    // System.out.println("\n===== CONSULTA RÁPIDA =====");
    // System.out.print("Digite o ID da receita: ");

    // int id = readInt();
    // Recipe recipe = quickSearchService.searchById(id);

    // if (recipe == null) {
    // System.out.println("Receita não encontrada.");
    // } else {
    // System.out.println(recipe);
    // }
    // }

    private void showQuickSearchModule() {
        System.out.println("\n===== CONSULTA RÁPIDA =====");
        System.out.println("1 - Buscar por ID");
        System.out.println("2 - Buscar por prefixo");
        System.out.println("3 - Buscar por categoria");
        System.out.println("4 - Buscar por ingrediente");

        System.out.print("Escolha: ");

        int option = readInt();

        switch (option) {

            case 1 -> {
                System.out.print("Digite o ID: ");
                int id = readInt();
                Recipe recipe = quickSearchService.searchById(id);

                if (recipe == null) {
                    System.out.println("Receita não encontrada.");
                } else {
                    System.out.println(recipe);
                }
            }
            case 2 -> {
                System.out.print("Digite o prefixo: ");
                String prefix = scanner.next();

                List<String> results = quickSearchService.searchByPrefix(prefix);

                if (results.isEmpty()) {
                    System.out.println("Nenhuma receita encontrada.");
                } else {
                    System.out.println("\nResultados:");
                    results.forEach(System.out::println);
                }
            }
            case 3 -> {
                System.out.print("Digite a categoria: ");
                scanner.nextLine();
                String category = scanner.nextLine();
                List<Recipe> results = quickSearchService.searchByCategory(category);

                if (results.isEmpty()) {
                    System.out.println("Nenhuma receita encontrada.");
                } else {
                    results.forEach(System.out::println);
                }
            }
            case 4 -> {
                System.out.print("Digite o ingrediente: ");
                scanner.nextLine();
                String ingredient = scanner.nextLine();
                List<Recipe> results = quickSearchService.searchByIngredient(ingredient);

                if (results.isEmpty()) {
                    System.out.println("Nenhuma receita encontrada.");
                } else {
                    results.forEach(System.out::println);
                }
            }
            default ->
                System.out.println("Opção inválida.");
        }
    }

    private void showChefModule() {
        System.out.println("\n===== MODO CHEF =====");

        System.out.print("Tempo máximo (min): ");
        int maxTime = readInt();

        System.out.print("Orçamento máximo (R$): ");
        double maxBudget = scanner.nextDouble();

        System.out.print("Dificuldade máxima: ");
        int maxDifficulty = readInt();

        Recipe bestRecipe = chefService.recommendBestRecipe(recipes, maxTime, maxBudget, maxDifficulty);

        if (bestRecipe == null) {
            System.out.println("Nenhuma receita encontrada.");
        } else {
            System.out.println("\nMelhor recomendação:");
            System.out.println(bestRecipe);
        }
    }

    private void showInvestigationModule() {
        System.out.println("\n===== INVESTIGAÇÃO =====");

        var tampered = investigationService.detectTamperedRecipes(recipes, recipeBook.getIntegrityHashTable());
        System.out.println("\nReceitas alteradas:");
        if (tampered.isEmpty()) {
            System.out.println("Nenhuma detectada.");
        } else {
            tampered.forEach(System.out::println);
        }

        var duplicates = investigationService.detectDuplicateRecipes(recipes);
        System.out.println("\nReceitas duplicadas:");
        if (duplicates.isEmpty()) {
            System.out.println("Nenhuma detectada.");
        } else {
            duplicates.forEach(System.out::println);
        }

        var conflicts = investigationService.detectVersionConflicts(recipes);
        System.out.println("\nConflitos:");
        if (conflicts.isEmpty()) {
            System.out.println("Nenhum detectado.");
        } else {
            conflicts.forEach(System.out::println);
        }

        var validationErrors = investigationService.validateRecipes(recipes);
        System.out.println("\nValidação:");
        if (validationErrors.isEmpty()) {
            System.out.println("Nenhum problema encontrado.");
        } else {
            validationErrors.forEach(System.out::println);
        }
    }
}