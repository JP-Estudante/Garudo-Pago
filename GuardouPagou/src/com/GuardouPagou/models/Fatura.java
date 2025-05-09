package com.GuardouPagou.models;

import java.time.LocalDate;

public class Fatura {
    private int id;
    private int numeroFatura;
    private String numeroNota; // Novo campo para número da nota
    private LocalDate vencimento;
    private double valor;
    private String status;

    public Fatura() {
    }

    public Fatura(LocalDate vencimento, double valor, String status) {
        this.vencimento = vencimento;
        this.valor = valor;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumeroFatura() {
        return numeroFatura;
    }

    public void setNumeroFatura(int numeroFatura) {
        this.numeroFatura = numeroFatura;
    }

    public String getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(String numeroNota) {
        this.numeroNota = numeroNota;
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
}