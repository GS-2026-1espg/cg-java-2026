package br.com.fiap.citizentech.model;

import java.math.BigDecimal;

/** Resultado do ranking: tempo e lotacao medios por linha nos horarios de pico. */
public record LinhaRanking(
        Long idLinha,
        String codigo,
        String nome,
        BigDecimal tempoMedioMin,
        BigDecimal lotacaoMediaPct,
        Long totalMedicoes) {
}
