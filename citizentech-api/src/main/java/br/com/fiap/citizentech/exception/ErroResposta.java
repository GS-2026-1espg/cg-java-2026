package br.com.fiap.citizentech.exception;

import java.time.Instant;

/** Corpo padrao de todas as respostas de erro da API. */
public record ErroResposta(int status, String erro, String mensagem, Instant timestamp) {

    public static ErroResposta de(int status, String erro, String mensagem) {
        return new ErroResposta(status, erro, mensagem, Instant.now());
    }
}
