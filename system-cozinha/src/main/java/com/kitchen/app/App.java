package com.kitchen.app;

import java.util.List;
import java.util.Scanner;

import com.kitchen.app.model.Receita;
import com.kitchen.app.util.LeitorArquivos;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        LeitorArquivos leitor = new LeitorArquivos();

        List<Receita> novasReceitas = leitor.carregarDados();

        if (!novasReceitas.isEmpty()) {
            System.out.println("\nPrimeira receita da lista:");
            System.out.println(novasReceitas.getFirst().toString());
        }

        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        System.out.println("Iniciando o sistema...");
        // Aqui no futuro você instanciará o seu LeitorArquivos e carregará os dados!

        while (opcao != 0) {
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

            // Verifica se o usuário digitou um número para evitar que o programa quebre
            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();

                switch (opcao) {
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
                scanner.next(); // Limpa a entrada errada do scanner para não travar num loop infinito
            }
        }

        scanner.close();

    }
}
