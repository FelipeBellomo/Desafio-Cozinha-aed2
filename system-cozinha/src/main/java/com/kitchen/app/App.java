package com.kitchen.app;

import java.util.List;
import java.util.Scanner;

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
                        System.out.println("[LOG] Ação: Preparando para buscar por nome, ID, categoria ou ingredientes.");
                        break;
                    case 3:
                        System.out.println("\n[LOG] Acessando Modo Chef...");
                        System.out.println("[LOG] Ação: Preparando algoritmo guloso para recomendar pratos sob restrições (tempo, custo, dificuldade).");
                        break;
                    case 4:
                        System.out.println("\n[LOG] Acessando Modo Investigação...");
                        System.out.println("[LOG] Ação: Verificando integridade das receitas e buscando conteúdos corrompidos/duplicados.");
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
