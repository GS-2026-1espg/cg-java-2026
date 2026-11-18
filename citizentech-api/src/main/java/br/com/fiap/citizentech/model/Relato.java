package br.com.fiap.citizentech.model;

import java.time.LocalDateTime;

/** Tabela RELATO. idLinha e tempoPerdidoMin sao opcionais (podem ser nulos). */
public record Relato(
        Long idRelato,
        Long idRegiao,
        Long idLinha,
        LocalDateTime dtRelato,
        String categoria,
        String descricao,
        Integer tempoPerdidoMin) {

    public Relato comId(Long id) {
        return new Relato(id, idRegiao, idLinha, dtRelato, categoria, descricao, tempoPerdidoMin);
    }
}
