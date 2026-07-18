package com.kitchen.app.controller;

import java.util.List;
import java.util.Scanner;

import com.kitchen.app.datastructures.graph.*;
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

    private final DependencyGraph productionGraph;
    private final LogisticsGraph logisticsGraph; // Novo grafo logístico

    public MenuController(RecipeBook recipeBook, List<Recipe> recipes) {
        this.recipeBook = recipeBook;
        this.recipes = recipes;
        this.scanner = new Scanner(System.in);
        this.chefService = new ChefService();
        this.investigationService = new InvestigationService();
        this.quickSearchService = new QuickSearchService(recipeBook);

        this.productionGraph = new DependencyGraph(60);
        setupMockDependencies();

        // Inicializando o Grafo Logístico com 5 pontos (0 a 4)
        this.logisticsGraph = new LogisticsGraph(5);
        setupMockLogistics();
    }

    private void setupMockDependencies() {
        productionGraph.addDependency(1, 2);
        productionGraph.addDependency(11, 12);
        // productionGraph.addDependency(12, 11); // Descomente para testar ciclo
    }

    private void setupMockLogistics() {
        // ID 0: Restaurante Principal
        // ID 1: Ponto Centro
        // ID 2: Ponto Fragata
        // ID 3: Ponto Três Vendas
        // ID 4: Ponto Laranjal

        // addTwoWayRoute(Origem, Destino, Tempo em Minutos, Capacidade de Pedidos)
        logisticsGraph.addTwoWayRoute(0, 1, 10.0, 50); // Restaurante <-> Centro (10 min)
        logisticsGraph.addTwoWayRoute(0, 2, 25.0, 30); // Restaurante <-> Fragata (25 min)
        logisticsGraph.addTwoWayRoute(1, 3, 15.0, 40); // Centro <-> Três Vendas (15 min)
        logisticsGraph.addTwoWayRoute(2, 4, 20.0, 20); // Fragata <-> Laranjal (20 min)
        logisticsGraph.addTwoWayRoute(3, 4, 30.0, 15); // Três Vendas <-> Laranjal (30 min)
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
                case 5 -> showProductionModule();
                case 6 -> showLogisticsModule(); // Chamada do Módulo 7
                case 0 -> System.out.println("\nEncerrando sistema...");
                default -> System.out.println("\nOpção inválida.");
            }
        }

        scanner.close();
    }

    private void showMainMenu() {
        System.out.println("\n=======================================================");
        System.out.println("                   DESAFIO NA COZINHA                  ");
        System.out.println("=======================================================");

        System.out.println("1 - Livro de Receitas");
        System.out.println("2 - Consulta Rápida");
        System.out.println("3 - Modo Chef (Módulo 6)");
        System.out.println("4 - Investigação");
        System.out.println("5 - Oficina de Produção (Módulo 5)");
        System.out.println("6 - Logística e Delivery (Módulo 7)"); // Nova Opção
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

    // =========================================================================
    // MÓDULOS 1, 2, 3 E 4 (Mantidos inalterados)
    // =========================================================================

    private void showRecipeModule() {
        System.out.println("\n===== LIVRO DE RECEITAS =====");
        for (Recipe recipe : recipes)
            System.out.println(recipe);
    }

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
                if (recipe == null) System.out.println("Receita não encontrada.");
                else System.out.println(recipe);
            }
            case 2 -> {
                System.out.print("Digite o prefixo: ");
                String prefix = scanner.next();
                List<String> results = quickSearchService.searchByPrefix(prefix);
                if (results.isEmpty()) System.out.println("Nenhuma receita encontrada.");
                else {
                    System.out.println("\nResultados:");
                    results.forEach(System.out::println);
                }
            }
            case 3 -> {
                System.out.print("Digite a categoria: ");
                scanner.nextLine();
                String category = scanner.nextLine();
                List<Recipe> results = quickSearchService.searchByCategory(category);
                if (results.isEmpty()) System.out.println("Nenhuma receita encontrada.");
                else results.forEach(System.out::println);
            }
            case 4 -> {
                System.out.print("Digite o ingrediente: ");
                scanner.nextLine();
                String ingredient = scanner.nextLine();
                List<Recipe> results = quickSearchService.searchByIngredient(ingredient);
                if (results.isEmpty()) System.out.println("Nenhuma receita encontrada.");
                else results.forEach(System.out::println);
            }
            default -> System.out.println("Opção inválida.");
        }
    }

    private void showTopRecommendations() {
        System.out.print("Tempo máximo (min): ");
        int maxTime = readInt();
        System.out.print("Orçamento máximo (R$): ");
        double maxBudget = scanner.nextDouble();
        System.out.print("Dificuldade máxima: ");
        int maxDifficulty = readInt();
        scanner.nextLine();

        List<Recipe> recommendations = chefService.recommendTopRecipes(recipes, maxTime, maxBudget, maxDifficulty,3);
        if (recommendations == null || recommendations.isEmpty()) {
            System.out.println("\n Nenhuma receita encontrada.");
        } else {
            System.out.println("\n Top " + recommendations.size() + " seleções do Chef:");
            for (int i = 0; i < recommendations.size(); i++) {
                Recipe recipe = recommendations.get(i);
                System.out.printf("%d° - ",(i+1));
                System.out.println(recipe);
            }
        }
    }

    private void showVipMenuOptimization() {
        System.out.println("\n--- MENU VIP: OTIMIZAÇÃO (Programação Dinâmica) ---");
        System.out.println("Qual restrição será aplicada no evento?");
        System.out.println("1 - Tempo Máximo de Preparo (minutos)");
        System.out.println("2 - Orçamento Máximo (R$)");
        System.out.print("Escolha: ");
        int constraintType = readInt();

        double limit = 0;
        if (constraintType == 1) {
            System.out.print("Informe o TEMPO MÁXIMO em minutos (Ex: 60): ");
            limit = readInt();
        } else {
            System.out.print("Informe o ORÇAMENTO MÁXIMO em R$ (Ex: 100,00): ");
            limit = scanner.nextDouble();
        }

        System.out.println("\nO que o Chef deseja maximizar?");
        System.out.println("1 - Avaliação Média (Rating)");
        System.out.println("2 - Lucro Esperado");
        System.out.print("Escolha: ");
        int optimizationGoal = readInt();

        List<Recipe> vipMenu = chefService.generateOptimizedVipMenu(recipes, limit, constraintType, optimizationGoal);

        if (vipMenu == null || vipMenu.isEmpty()) {
            System.out.println("\n[ALERTA] Os limites são muito rigorosos. Nenhuma receita cabe neste evento.");
        } else {
            System.out.println("\n[SUCESSO] Menu Degustação VIP gerado com sucesso!");
            System.out.println("===============================================================");

            double totalRating = 0;
            int totalTimeUsed = 0;
            double totalCost = 0;
            double totalProfit = 0;

            for (Recipe r : vipMenu) {
                totalRating += r.getRating();
                totalTimeUsed += r.getPrepTime();
                totalCost += r.getCost();
                totalProfit += r.getProfit();
            }

            System.out.println("RESUMO DO MENU VIP:");
            System.out.println(">> Total de Pratos Selecionados: " + vipMenu.size());
            System.out.println(">> Tempo Total Requerido: " + totalTimeUsed + " minutos");
            System.out.printf(">> Custo de Produção Total: R$ %.2f\n", totalCost);
            System.out.printf(">> Lucro Total Esperado: R$ %.2f\n", totalProfit);
            System.out.printf(">> Somatório de Avaliações (Rating): %.1f\n", totalRating);
            System.out.println("===============================================================");

            System.out.println("\n[LOG] DETALHAMENTO DAS RECEITAS ESCOLHIDAS:");
            for (Recipe r : vipMenu) {
                System.out.printf("[LOG] -> Adicionado: %s (Tempo: %d min | Custo: R$%.2f | Lucro: R$%.2f | Rating: %.1f)\n",
                        r.getName(), r.getPrepTime(), r.getCost(), r.getProfit(), r.getRating());
            }
        }
    }

    private void showChefModule() {
        System.out.println("\n===== MODO CHEF =====");
        System.out.println("1 - Top Recomendações");
        System.out.println("2 - Gerar Menu VIP Otimizado (Módulo 6)");
        System.out.print("Escolha: ");

        int option = readInt();

        switch (option) {
            case 1 -> showTopRecommendations();
            case 2 -> showVipMenuOptimization();
            default -> System.out.println("\nOpção inválida.");
        }
    }

    private void showInvestigationModule() {
        System.out.println("\n===== INVESTIGAÇÃO =====");
        var tampered = investigationService.detectTamperedRecipes(recipes, recipeBook.getIntegrityHashTable());
        System.out.println("\nReceitas alteradas:");
        if (tampered.isEmpty()) System.out.println("Nenhuma detectada.");
        else tampered.forEach(System.out::println);

        var duplicates = investigationService.detectDuplicateRecipes(recipes);
        System.out.println("\nReceitas duplicadas:");
        if (duplicates.isEmpty()) System.out.println("Nenhuma detectada.");
        else duplicates.forEach(System.out::println);

        var conflicts = investigationService.detectVersionConflicts(recipes);
        System.out.println("\nConflitos:");
        if (conflicts.isEmpty()) System.out.println("Nenhum detectado.");
        else conflicts.forEach(System.out::println);

        var validationErrors = investigationService.validateRecipes(recipes);
        System.out.println("\nValidação:");
        if (validationErrors.isEmpty()) System.out.println("Nenhum problema encontrado.");
        else validationErrors.forEach(System.out::println);
    }

    // =========================================================================
    // MÓDULO 5: OFICINA DE PRODUÇÃO
    // =========================================================================
    private void showProductionModule() {
        System.out.println("\n===== OFICINA DE PRODUÇÃO =====");
        System.out.println("1 - Verificar Inconsistências (Erros de Dependência)");
        System.out.println("2 - Gerar Sequência de Produção do Menu");
        System.out.println("3 - Adicionar Nova Dependência");
        System.out.print("Escolha: ");

        int option = readInt();

        switch (option) {
            case 1 -> {
                GraphStack cycle = productionGraph.getDependencyCycle();
                if (cycle != null) {
                    System.out.println("\n[ALERTA] Inconsistência detectada! Existe um ciclo de dependências.");
                    System.out.print("[LOG] Caminho do Ciclo: ");

                    int startOfCycle = -1;
                    boolean isFirst = true;

                    while (!cycle.isEmpty()) {
                        int currentId = cycle.pop();
                        if (isFirst) {
                            startOfCycle = currentId;
                            isFirst = false;
                        } else {
                            System.out.print(" -> ");
                        }

                        Recipe recipe = quickSearchService.searchById(currentId);
                        String name = (recipe != null) ? recipe.getName() : "Receita Desconhecida";
                        System.out.print(name + " [ID: " + currentId + "]");
                    }

                    Recipe startRecipe = quickSearchService.searchById(startOfCycle);
                    String startName = (startRecipe != null) ? startRecipe.getName() : "Receita Desconhecida";
                    System.out.println(" -> " + startName + " [ID: " + startOfCycle + "] (Fecha o ciclo!)");

                } else {
                    System.out.println("\n[SUCESSO] Todas as dependências estão corretas. Nenhum ciclo encontrado.");
                }
            }
            case 2 -> {
                if (productionGraph.getDependencyCycle() != null) {
                    System.out.println("\n[ERRO] Não é possível gerar a sequência. Corrija o ciclo de dependências primeiro.");
                } else {
                    GraphStack prepOrder = productionGraph.getCorrectSequence();

                    System.out.println("\n[ORDEM DE PRODUÇÃO]");
                    boolean isFirst = true;

                    while (!prepOrder.isEmpty()) {
                        int currentId = prepOrder.pop();
                        Recipe recipe = quickSearchService.searchById(currentId);

                        if (recipe != null) {
                            if (!isFirst) System.out.print(" -> ");
                            System.out.print(recipe.getName() + " [ID: " + currentId + "]");
                            isFirst = false;
                        }
                    }
                    System.out.println("\n");
                }
            }
            case 3 -> {
                System.out.println("\n--- ADICIONAR DEPENDÊNCIA ---");
                System.out.println("Qual receita precisa estar pronta PRIMEIRO?");
                System.out.print("Digite o ID do pré-requisito: ");
                int prerequisiteId = readInt();

                System.out.println("Qual receita será finalizada DEPOIS?");
                System.out.print("Digite o ID da receita final: ");
                int finalRecipeId = readInt();

                productionGraph.addDependency(prerequisiteId, finalRecipeId);
                System.out.println("\n[SUCESSO] Dependência registrada no sistema!");
            }
            default -> System.out.println("\nOpção inválida.");
        }
    }

    // =========================================================================
    // MÓDULO 7: LOGÍSTICA E DELIVERY
    // =========================================================================
    private void showLogisticsModule() {
        System.out.println("\n===== MÓDULO 7: O PESADELO LOGÍSTICO =====");
        System.out.println("1 - Estimar Tempos de Entrega (Algoritmo de Dijkstra)");
        System.out.println("2 - Planejar Infraestrutura Mínima (Algoritmo de Prim)");
        System.out.println("3 - Calcular Capacidade Máxima de Pedidos / Gargalos (Ford-Fulkerson)");
        System.out.print("Escolha: ");
        int option = readInt();

        String[] locationNames = {"Restaurante Principal", "Ponto Centro", "Ponto Fragata", "Ponto Três Vendas", "Ponto Laranjal"};

        switch (option) {
            case 1 -> {
                System.out.println("\n[LOG] Calculando as rotas mais rápidas a partir do Restaurante Principal (ID 0)...");
                double[] shortestTimes = DijkstraAlgorithm.calculateShortestTimes(logisticsGraph, 0);

                System.out.println("\n--- ESTIMATIVA DE TEMPO DE ENTREGA ---");
                for (int i = 1; i < logisticsGraph.getNumLocations(); i++) {
                    if (shortestTimes[i] == Double.MAX_VALUE) {
                        System.out.printf("Destino: %-20s | Status: Rota Inacessível!\n", locationNames[i]);
                    } else {
                        System.out.printf("Destino: %-20s | Tempo Mínimo Estimado: %.1f minutos\n", locationNames[i], shortestTimes[i]);
                    }
                }
                System.out.println("--------------------------------------");
            }
            case 2 -> {
                System.out.println("\n[LOG] Calculando a Árvore Geradora Mínima (MST) para otimizar infraestrutura...");
                PrimAlgorithm.MSTResult mst = PrimAlgorithm.calculateMinimumInfrastructure(logisticsGraph);

                System.out.println("\n--- REDE DE INFRAESTRUTURA MÍNIMA ---");
                double totalCost = 0;

                for (int i = 1; i < logisticsGraph.getNumLocations(); i++) {
                    int parent = mst.parents[i];
                    double weight = mst.edgeWeights[i];

                    if (parent != -1) {
                        System.out.printf("Conectar: [%s] <---> [%s] (Custo: %.1f)\n",
                                locationNames[parent], locationNames[i], weight);
                        totalCost += weight;
                    } else {
                        System.out.printf("[ALERTA] [%s] está isolado e não pode ser conectado!\n", locationNames[i]);
                    }
                }
                System.out.println("--------------------------------------");
                System.out.printf("[RESUMO] Custo total da infraestrutura conectada: %.1f\n", totalCost);
            }
            case 3 -> {
                System.out.println("\n--- ANÁLISE DE GARGALOS E CAPACIDADE OPERACIONAL ---");
                System.out.println("Escolha o destino para verificar a capacidade simultânea:");
                for (int i = 1; i < locationNames.length; i++) {
                    System.out.println(i + " - " + locationNames[i]);
                }
                System.out.print("Destino: ");
                int destination = readInt();

                if (destination < 1 || destination >= logisticsGraph.getNumLocations()) {
                    System.out.println("\n[ERRO] Destino inválido.");
                } else {
                    System.out.println("\n[LOG] Analisando o fluxo da rede a partir do Restaurante Principal...");

                    int maxOrders = FordFulkersonAlgorithm.calculateMaxCapacity(logisticsGraph, 0, destination);

                    System.out.println("===============================================================");
                    System.out.printf("[RESULTADO] A capacidade máxima de atendimento simultâneo para o destino [%s] é de %d pedidos.\n", locationNames[destination], maxOrders);
                    System.out.println("[ALERTA] Qualquer demanda acima desse valor causará travamento logístico (gargalo).");
                    System.out.println("===============================================================");
                }
            }
            default -> System.out.println("\nOpção inválida.");
        }
    }
}