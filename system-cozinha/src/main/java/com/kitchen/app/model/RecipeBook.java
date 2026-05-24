package com.kitchen.app.model;

import com.kitchen.app.datastructures.HashTable;
import com.kitchen.app.datastructures.RWayTrie;

import java.text.Normalizer;
import java.util.List;

public class RecipeBook {

    private RWayTrie searchTrie;
    private HashTable<Integer, String> integrityHashTable;
    
    public RecipeBook() {
        this.searchTrie = new RWayTrie();
        this.integrityHashTable = new HashTable<>();
    }

    public void initializeSystem(List<Recipe> loadedRecipes) {
        for (Recipe recipe : loadedRecipes) {
            // stores the original hash to verify tampering later
            integrityHashTable.put(recipe.getId(), recipe.generateIntegrityHash());

            // inserts the name into the trie (converted to lowercase for standard the searching)
            String normalizedName = normalizeText(recipe.getName());
            searchTrie.insert(normalizedName, recipe.getName(), recipe.getId());

            // BTree.insert(recipe) .... will be here;
        }
        System.out.println("Sistema inicializado com " + loadedRecipes.size() + " receitas na memória RAM.");
    }

    public String normalizeText(String text) {
        if (text == null) return "";
        // Separa os acentos das letras base
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        // Remove os caracteres de acento usando Expressão Regular
        normalized = normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return normalized.toLowerCase();
    }

    /**
     * Compara uma lista de receitas (que no futuro virão do disco)
     * com os hashes originais salvos na memória RAM.
     */
    public void verifySabotage(List<Recipe> recipesToCheck) {
        System.out.println("\n[INVESTIGAÇÃO] Iniciando varredura de integridade...");
        boolean sabotageFound = false;

        for (Recipe r : recipesToCheck) {
            // Pega a assinatura original que guardamos no início
            String originalHash = integrityHashTable.get(r.getId());

            // Calcula a assinatura de como a receita está AGORA
            String currentHash = r.generateIntegrityHash();

            if (originalHash == null) {
                System.out.println("[ALERTA] Receita ID " + r.getId() + " (" + r.getName() + ") não estava no carregamento original! (Infiltração/Adição Indevida)");
                sabotageFound = true;
            } else if (!originalHash.equals(currentHash)) {
                System.out.println("[SABOTAGEM DETECTADA] A receita ID " + r.getId() + " (" + r.getName() + ") foi adulterada!");
                sabotageFound = true;
            }
        }

        if (!sabotageFound) {
            System.out.println("[OK] Nenhuma sabotagem detectada. Todas as receitas conferem com os registros originais.");
        }
    }

    public RWayTrie getSearchTrie() { return searchTrie; }
    public HashTable<Integer, String> getIntegrityHashTable() { return integrityHashTable; }
}