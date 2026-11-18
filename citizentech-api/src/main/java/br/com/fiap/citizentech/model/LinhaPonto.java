package br.com.fiap.citizentech.model;

/** Tabela associativa LINHA_PONTO: ordem do ponto no trajeto da linha. */
public record LinhaPonto(Long idLinha, Long idPonto, Integer ordem) {
}
