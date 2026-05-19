package com.felipe.app;

import java.util.List;

public class Receita {
    private String id;
    private String nome;
    private String categoria;
    private List<String> ingredientes;
    private int tempoPreparo; // em minutos
    private double custoEstimado;
    private String nivelDificuldade;
    private double avaliacao;
    private int quantidadePedidos;

    public Receita(String id, String nome, String categoria, List<String> ingredientes, int tempoPreparo, double custoEstimado, String nivelDificuldade, double avaliacao, int quantidadePedidos) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.ingredientes = ingredientes;
        this.tempoPreparo = tempoPreparo;
        this.custoEstimado = custoEstimado;
        this.nivelDificuldade = nivelDificuldade;
        this.avaliacao = avaliacao;
        this.quantidadePedidos = quantidadePedidos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public List<String> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<String> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public int getTempoPreparo() {
        return tempoPreparo;
    }

    public void setTempoPreparo(int tempoPreparo) {
        this.tempoPreparo = tempoPreparo;
    }

    public double getCustoEstimado() {
        return custoEstimado;
    }

    public void setCustoEstimado(double custoEstimado) {
        this.custoEstimado = custoEstimado;
    }

    public String getNivelDificuldade() {
        return nivelDificuldade;
    }

    public void setNivelDificuldade(String nivelDificuldade) {
        this.nivelDificuldade = nivelDificuldade;
    }

    public double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public int getQuantidadePedidos() {
        return quantidadePedidos;
    }

    public void setQuantidadePedidos(int quantidadePedidos) {
        this.quantidadePedidos = quantidadePedidos;
    }
}