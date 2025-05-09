package com.GuardouPagou.models;

public class Marca {
    private int id;
    private String nome;
    private String descricao;
    private String cor;

    public Marca() {
    }

    public Marca(String nome, String descricao, String cor) {
        this.nome = nome;
        this.descricao = descricao;
        this.cor = cor;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }
}