package com.kitchen.app.util;

import com.google.gson.Gson;
import com.kitchen.app.model.Recipe;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileLoader {

    public List<Recipe> loadData(String fileName) {
        Gson gson = new Gson();
        try (Reader reader = new InputStreamReader(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(fileName)))) {
            
            Recipe[] recipeList = gson.fromJson(reader, Recipe[].class);
            return Arrays.asList(recipeList);

        } catch (Exception e) {
            System.out.println("Erro ao ler o ficheiro: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}