package com.kitchen.app.datastructures;

// Visibilidade de pacote (package-private) para proteger a estrutura
class TrieNode<V> {
    private static final int R = 256;

    // Agora usamos o tipo genérico V explicitamente no atributo
    V val;
    final TrieNode<V>[] next;

    @SuppressWarnings("unchecked")
    TrieNode() {
        // Instancia o array de ponteiros para o próximo nó
        this.next = new TrieNode[R];
    }
}