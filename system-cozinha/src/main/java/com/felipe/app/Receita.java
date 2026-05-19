package com.felipe.app;

import java.util.List;

public class Receita {
    private int id;
    private List<String> ingredientes;

    public int getId() {
        return id;
    }

    public List<String> getIngredientes() {
        return ingredientes;
    }

    @Override
    public String toString() {
        return "Receita ID: " + id + " | Ingredientes: " + ingredientes;
    }
}