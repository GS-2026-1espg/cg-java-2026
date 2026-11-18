package br.com.fiap.citizentech.model;

import java.time.LocalDateTime;

/** Tabela ALERTA. idMedicao e opcional; status padrao e ABERTO. */
public record Alerta(
        Long idAlerta,
        Long idLinha,
        Long idMedicao,
        LocalDateTime dtAlerta,
        String nivel,
        String mensagem,
        String status) {

    public Alerta comId(Long id) {
        return new Alerta(id, idLinha, idMedicao, dtAlerta, nivel, mensagem, status);
    }

    public Alerta comStatus(String novoStatus) {
        return new Alerta(idAlerta, idLinha, idMedicao, dtAlerta, nivel, mensagem, novoStatus);
    }
}
