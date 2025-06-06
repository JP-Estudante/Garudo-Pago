// Localização: com/GuardouPagou/models/NotaFiscalArquivadaDTO.java
package com.GuardouPagou.dao; // Ou com.GuardouPagou.models.dto

import java.time.LocalDate;

public class NotaFiscalArquivadaDAO {
    private String numeroNota;
    private int quantidadeFaturas;
    private String marca;
    private LocalDate dataArquivamento;

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
}