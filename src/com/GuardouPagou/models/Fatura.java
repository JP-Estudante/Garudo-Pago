package com.GuardouPagou.models;

import java.time.LocalDate;

public class Fatura {
    private int id;
    private int notaFiscalId;
    private String numeroNota;
    private int numeroFatura;
    private LocalDate vencimento;
    private double valor;
    private String status;
    private String marca;
    private String marcaColor;

    // Construtor vazio
    public Fatura() {
    }

    // Construtor existente
    public Fatura(LocalDate vencimento, double valor, String status) {
        this.vencimento = vencimento;
        this.valor = valor;
        this.status = status;
    }

    // Novo construtor para incluir numeroFatura
    public Fatura(int numeroFatura, LocalDate vencimento, double valor, String status) {
        this.numeroFatura = numeroFatura;
        this.vencimento = vencimento;
        this.valor = valor;
        this.status = status;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNotaFiscalId() {
        return notaFiscalId;
    }

    public void setNotaFiscalId(int notaFiscalId) {
        this.notaFiscalId = notaFiscalId;
    }

    public String getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(String numeroNota) {
        this.numeroNota = numeroNota;
    }

    public int getNumeroFatura() {
        return numeroFatura;
    }

    public void setNumeroFatura(int numeroFatura) {
        this.numeroFatura = numeroFatura;
    }

    public LocalDate getVencimento() {
        return vencimento;
    }

    public void setVencimento(LocalDate vencimento) {
        this.vencimento = vencimento;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getMarcaColor() {
        return marcaColor;
    }

    public void setMarcaColor(String marcaColor) {
        this.marcaColor = marcaColor;
    }

}
