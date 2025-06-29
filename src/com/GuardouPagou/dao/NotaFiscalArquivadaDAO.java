// Localização: com/GuardouPagou/models/NotaFiscalArquivadaDTO.java
package com.GuardouPagou.dao; // Ou com.GuardouPagou.models.dto

import java.time.LocalDate;

@SuppressWarnings("unused")
public class NotaFiscalArquivadaDAO {
    private final String numeroNota;
    private final int quantidadeFaturas;
    private final String marca;
    private final LocalDate dataArquivamento;
    private String marcaColor;

    public NotaFiscalArquivadaDAO(String numeroNota, int quantidadeFaturas, String marca, LocalDate dataArquivamento) {
        this.numeroNota = numeroNota;
        this.quantidadeFaturas = quantidadeFaturas;
        this.marca = marca;
        this.dataArquivamento = dataArquivamento;
    }

    // Getters
    public String getNumeroNota() { return numeroNota; }
    public int getQuantidadeFaturas() { return quantidadeFaturas; }
    public String getMarca() { return marca; }
    public LocalDate getDataArquivamento() { return dataArquivamento; }

    public String getMarcaColor() {
        return marcaColor;
    }

    // Setter
    public void setMarcaColor(String marcaColor) {
        this.marcaColor = marcaColor;
    }
}