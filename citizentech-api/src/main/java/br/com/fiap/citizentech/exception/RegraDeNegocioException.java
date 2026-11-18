package br.com.fiap.citizentech.exception;

/** Dado invalido ou regra de negocio violada (HTTP 400). */
public class RegraDeNegocioException extends RuntimeException {

    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
