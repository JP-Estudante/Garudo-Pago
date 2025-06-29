package com.GuardouPagou.models;

import java.time.LocalDate;
import java.util.List;

public class NotaFiscal {
    private String numeroNota;
    private LocalDate dataEmissao;
    private String marca;
    private List<Fatura> faturas;
    
    // Construtor padrão
    public NotaFiscal() {
    }

    // Construtor com parâmetros
    public NotaFiscal(String numeroNota, LocalDate dataEmissao, String marca, List<Fatura> faturas) {
        this.numeroNota = numeroNota;
        this.dataEmissao = dataEmissao;
        this.marca = marca;
        this.faturas = faturas;
    }

    public String getNumeroNota() {
        return numeroNota;
    }

    public void setNumeroNota(String numeroNota) {
        this.numeroNota = numeroNota;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public List<Fatura> getFaturas() {
        return faturas;
    }

    public void setFaturas(List<Fatura> faturas) {
        this.faturas = faturas;
    }
}