package br.com.fiap.citizentech.exception;

import java.sql.SQLException;

/** Erro de acesso ao banco. Guarda o codigo ORA-xxxxx para o handler decidir o status HTTP. */
public class DaoException extends RuntimeException {

    private final int codigoOracle;

    public DaoException(String mensagem, SQLException causa) {
        super(mensagem, causa);
        this.codigoOracle = causa.getErrorCode();
    }

    public int getCodigoOracle() {
        return codigoOracle;
    }
}
