package br.com.fiap.citizentech.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Abre conexoes JDBC com o Oracle da FIAP.
 * Os DAOs recebem esta classe e usam try-with-resources para fechar cada conexao.
 */
@Component
public class ConnectionFactory {

    private final String url;
    private final String user;
    private final String password;

    public ConnectionFactory(@Value("${citizentech.db.url}") String url,
                             @Value("${citizentech.db.user}") String user,
                             @Value("${citizentech.db.password}") String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
