package br.com.fiap.citizentech.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Tabela MEDICAO. */
public record Medicao(
        Long idMedicao,
        Long idLinha,
        Long idPonto,
        LocalDateTime dtMedicao,
        String faixaHorario,
        Integer tempoViagemMin,
        Integer lotacaoPct,
        BigDecimal intervaloMin) {

    public Medicao comId(Long id) {
        return new Medicao(id, idLinha, idPonto, dtMedicao, faixaHorario, tempoViagemMin, lotacaoPct, intervaloMin);
    }
}
