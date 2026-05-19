package com.felipe.app;

import java.util.List;

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
            System.out.println(novasReceitas.get(0).toString());
        }

    }
}
