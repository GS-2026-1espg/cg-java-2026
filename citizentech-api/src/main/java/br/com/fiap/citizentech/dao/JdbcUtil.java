package br.com.fiap.citizentech.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;

/** Auxiliares de JDBC para colunas opcionais (NULL) e datas. */
final class JdbcUtil {

    private JdbcUtil() {
    }

    static void setLongOuNulo(PreparedStatement ps, int pos, Long valor) throws SQLException {
        if (valor == null) {
            ps.setNull(pos, Types.NUMERIC);
        } else {
            ps.setLong(pos, valor);
        }
    }

    static void setIntOuNulo(PreparedStatement ps, int pos, Integer valor) throws SQLException {
        if (valor == null) {
            ps.setNull(pos, Types.NUMERIC);
        } else {
            ps.setInt(pos, valor);
        }
    }

    static Long getLongOuNulo(ResultSet rs, String coluna) throws SQLException {
        long v = rs.getLong(coluna);
        return rs.wasNull() ? null : v;
    }

    static Integer getIntOuNulo(ResultSet rs, String coluna) throws SQLException {
        int v = rs.getInt(coluna);
        return rs.wasNull() ? null : v;
    }

    static Timestamp toTimestamp(LocalDateTime data) {
        return Timestamp.valueOf(data);
    }

    static LocalDateTime getDataHora(ResultSet rs, String coluna) throws SQLException {
        return rs.getTimestamp(coluna).toLocalDateTime();
    }
}
