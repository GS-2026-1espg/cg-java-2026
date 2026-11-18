package br.com.fiap.citizentech.controller;

import br.com.fiap.citizentech.dao.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Verifica se a API esta no ar e se alcanca o Oracle.
 * Usado para validar o deploy no Render logo no inicio.
 */
@RestController
public class HealthController {

    private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    private final ConnectionFactory connectionFactory;

    public HealthController(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        try (Connection conn = connectionFactory.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT 1 FROM dual")) {
            rs.next();
            return ResponseEntity.ok(Map.of("api", "ok", "banco", "ok"));
        } catch (SQLException e) {
            log.error("Falha ao conectar no Oracle", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("api", "ok", "banco", "indisponivel"));
        }
    }
}
