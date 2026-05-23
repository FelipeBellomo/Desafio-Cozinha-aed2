package com.kitchen.app;

import java.util.List;
import java.util.Scanner;

import com.kitchen.app.model.ChefRecommender;
import com.kitchen.app.model.RecipeBook;
import com.kitchen.app.model.Recipe;
import com.kitchen.app.util.FileLoader;

public class App 
{
    public static void main( String[] args )
    {
        FileLoader loader = new FileLoader();

        List<Recipe> newRecipes = loader.loadData("pequenasReceitas.json");
        RecipeBook recipeBook = new RecipeBook();
        recipeBook.initializeSystem(newRecipes);
        ChefRecommender chefRecommender = new ChefRecommender();

        Scanner scanner = new Scanner(System.in);
        int option = -1;

        System.out.println("Iniciando o sistema...");
        

        while (option != 0) {
            System.out.println("\n=======================================================");
            System.out.println("        DESAFIO NA COZINHA - SISTEMA JACQUIN           ");
            System.out.println("=======================================================");
            System.out.println("Escolha um Modo de Interação:");
            System.out.println("1 - [Módulo 1] Livro de Receitas (Carregar/Listar)");
            System.out.println("2 - [Módulos 2 e 3] Modo Consulta Rápida");
            System.out.println("3 - [Módulo 5] Modo Chef: Recomendações");
            System.out.println("4 - [Investigação] Encontrar Sabotagem Culinária");
            System.out.println("0 - Sair do Sistema");
            System.out.print("Sua opção: ");

            if (scanner.hasNextInt()) {
                option = scanner.nextInt();

                switch (option) {
                    case 1:
                        System.out.println("\n[LOG] Acessando Módulo 1...");
                        System.out.println("[LOG] Ação: Listando todas as receitas disponíveis no sistema.");
                        break;
                    case 2:
                        System.out.println("\n[LOG] Acessando Modo Consulta Rápida...");
                        System.out.print("Digite o início do nome do prato (ex: 'ome'): ");

                        // Limpa o buffer do scanner (importante após usar nextInt!)
                        scanner.nextLine();
                        String busca = scanner.nextLine();

                        System.out.println("\n--- Resultados para '" + busca + "' ---");

                        // Converte para minúsculo para bater com o que foi salvo na Trie
                        String prefixoNormalizado = busca.toLowerCase();

                        // Chama o método autocomplete, que já imprime na tela e retorna a quantidade
                        // OBS: Substitua 'recipeBook' pelo nome da variável que você usou para instanciar a classe RecipeBook
                        int encontrados = recipeBook.getSearchTrie().autocomplete(prefixoNormalizado);

                        if (encontrados == 0) {
                            System.out.println("Nenhuma receita encontrada com esse prefixo.");
                        } else {
                            System.out.println("\nTotal de " + encontrados + " receita(s) encontrada(s).");
                        }
                        break;
                    case 3:
                        System.out.println("\n[LOG] Acessando Modo Chef...");
                        System.out.print("Digite o seu orçamento máximo (R$): ");

                        // Limpa o buffer do scanner
                        scanner.nextLine();

                        try {
                            double budget = Double.parseDouble(scanner.nextLine());

                            // Chama o algoritmo guloso passando as receitas carregadas e o orçamento
                            List<Recipe> recommendedMenu = chefRecommender.generateMenuByBudget(newRecipes, budget);

                            if (recommendedMenu.isEmpty()) {
                                System.out.println("\n[SISTEMA] Desculpe, seu orçamento é muito baixo para recomendar qualquer receita.");
                            } else {
                                System.out.println("\n--- Menu Recomendado ---");
                                double totalCost = 0.0;

                                for (Recipe recipe : recommendedMenu) {
                                    System.out.printf("- %s (Avaliação: %.1f | Custo: R$%.2f)\n",
                                            recipe.getName(), recipe.getRating(), recipe.getCost());
                                    totalCost += recipe.getCost();
                                }

                                System.out.println("------------------------");
                                System.out.printf("Custo total estimado: R$%.2f\n", totalCost);
                                System.out.printf("Orçamento restante: R$%.2f\n", (budget - totalCost));
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("\n[ERRO] Orçamento inválido! Por favor, digite um valor numérico (ex: 25.50).");
                        }
                        break;
                    case 4:
                        System.out.println("\n[LOG] Acessando Modo Investigação...");

                        // 1. Fazemos a verificação inicial (deve dar tudo OK)
                        System.out.println("\n--- Verificação de Rotina ---");
                        // OBS: Estamos passando a 'novasReceitas' original só para testar.
                        // Quando o projeto estiver pronto, passaremos a lista que vem do Arquivo Binário.
                        recipeBook.verifySabotage(newRecipes);

                        // 2. SIMULAÇÃO DE SABOTAGEM
                        System.out.println("\n[SISTEMA] Simulando um ataque hacker (alterando o custo de uma receita)...");
                        Recipe vitima = newRecipes.get(0); // Pega a primeira receita
                        vitima.setCost(999.99); // Altera o preço de forma indevida

                        // 3. Roda a verificação de novo para ver se o sistema pega o erro
                        System.out.println("\n--- Verificação Pós-Ataque ---");
                        recipeBook.verifySabotage(newRecipes);

                        // Restaura o valor (opcional, só para não quebrar testes futuros)
                        vitima.setCost(3.50);
                        break;
                    case 0:
                        System.out.println("\n[LOG] Encerrando o sistema. Au revoir!");
                        break;
                    default:
                        System.out.println("\n[ERRO] Opção inválida. Por favor, escolha um número de 0 a 4.");
                }
            } else {
                System.out.println("\n[ERRO] Entrada inválida! Por favor, digite apenas números.");
                scanner.next(); // clears the  buffer
            }
        }

        scanner.close();

    }
}
