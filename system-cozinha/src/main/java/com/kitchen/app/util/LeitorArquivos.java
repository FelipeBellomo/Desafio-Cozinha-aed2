package com.kitchen.app.util;

import com.google.gson.Gson;
import com.kitchen.app.model.Receita;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeitorArquivos {

    public List<Receita> carregarDados() {
        Gson gson = new Gson();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("/home/felipe-bellomo/Documentos/aed-II/desafio-cozinha/system-cozinha/src/main/resources/pequenasReceitas.json"));
            Receita[] listaReceitas = gson.fromJson(reader, Receita[].class);

            return Arrays.asList(listaReceitas);

        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
