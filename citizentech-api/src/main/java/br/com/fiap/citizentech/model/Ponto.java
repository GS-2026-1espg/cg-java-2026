package br.com.fiap.citizentech.model;

import java.math.BigDecimal;

/** Tabela PONTO. */
public record Ponto(
        Long idPonto,
        String nome,
        String endereco,
        BigDecimal latitude,
        BigDecimal longitude,
        Long idRegiao) {

    public Ponto comId(Long id) {
        return new Ponto(id, nome, endereco, latitude, longitude, idRegiao);
    }
}
