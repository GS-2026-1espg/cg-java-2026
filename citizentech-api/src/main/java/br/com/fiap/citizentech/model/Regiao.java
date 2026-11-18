package br.com.fiap.citizentech.model;

import java.math.BigDecimal;

/** Tabela REGIAO. */
public record Regiao(
        Long idRegiao,
        String nome,
        String zona,
        BigDecimal distanciaTerminalKm,
        BigDecimal rendaMedia,
        Long populacao) {

    public Regiao comId(Long id) {
        return new Regiao(id, nome, zona, distanciaTerminalKm, rendaMedia, populacao);
    }
}
