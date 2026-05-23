package com.kitchen.app.model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Recipe implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String category;
    private int prepTime; // min
    private double cost;
    private int difficulty; // Ex: 1 a 5
    private double rating;
    private int orderCount;
    private List<String> ingredients;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getPrepTime() { return prepTime; }
    public double getCost() { return cost; }
    public int getDifficulty() { return difficulty; }
    public double getRating() { return rating; }
    public int getOrderCount() { return orderCount; }
    public List<String> getIngredients() { return ingredients; }

    // generate a SHA-256 hash based on the recipe content to detect tampering
    public String generateIntegrityHash() {
        try {
            String content = id + name + category + prepTime + cost + 
                              difficulty + rating + ingredients.toString();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(content.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash de integridade", e);
        }
    }

    @Override
    public String toString() {
        return "Receita [" + id + "] " + name + " | Custo: " + cost + " | Avaliação: " + rating;
    }

    public void setCost(double v) {
        this.cost = v;
    }
}