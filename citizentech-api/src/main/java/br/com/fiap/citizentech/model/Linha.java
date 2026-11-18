package br.com.fiap.citizentech.model;

import java.math.BigDecimal;

/** Tabela LINHA. */
public record Linha(
        Long idLinha,
        String codigo,
        String nome,
        Long idRegiao,
        Integer frotaAtual,
        BigDecimal intervaloMedioMin) {

    public Linha comId(Long id) {
        return new Linha(id, codigo, nome, idRegiao, frotaAtual, intervaloMedioMin);
    }
}
