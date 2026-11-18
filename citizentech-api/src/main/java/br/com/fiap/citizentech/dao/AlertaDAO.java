package br.com.fiap.citizentech.dao;

import br.com.fiap.citizentech.exception.DaoException;
import br.com.fiap.citizentech.model.Alerta;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AlertaDAO {

    private static final String COLUNAS =
            "id_alerta, id_linha, id_medicao, dt_alerta, nivel, mensagem, status";

    private final ConnectionFactory connectionFactory;

    public AlertaDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public List<Alerta> listar() {
        String sql = "SELECT " + COLUNAS + " FROM alerta ORDER BY id_alerta";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Alerta> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("Erro ao listar alertas", e);
        }
    }

    public Optional<Alerta> buscarPorId(Long id) {
        String sql = "SELECT " + COLUNAS + " FROM alerta WHERE id_alerta = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao buscar alerta", e);
        }
    }

    public Alerta inserir(Alerta a) {
        String sql = "INSERT INTO alerta (id_linha, id_medicao, dt_alerta, nivel, mensagem, status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID_ALERTA"})) {
            preencher(ps, a);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return a.comId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao inserir alerta", e);
        }
    }

    public boolean atualizar(Alerta a) {
        String sql = "UPDATE alerta SET id_linha = ?, id_medicao = ?, dt_alerta = ?, nivel = ?, "
                + "mensagem = ?, status = ? WHERE id_alerta = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencher(ps, a);
            ps.setLong(7, a.idAlerta());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao atualizar alerta", e);
        }
    }

    public boolean deletar(Long id) {
        String sql = "DELETE FROM alerta WHERE id_alerta = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao excluir alerta", e);
        }
    }

    private void preencher(PreparedStatement ps, Alerta a) throws SQLException {
        ps.setLong(1, a.idLinha());
        JdbcUtil.setLongOuNulo(ps, 2, a.idMedicao());
        ps.setTimestamp(3, JdbcUtil.toTimestamp(a.dtAlerta()));
        ps.setString(4, a.nivel());
        ps.setString(5, a.mensagem());
        ps.setString(6, a.status());
    }

    private Alerta mapear(ResultSet rs) throws SQLException {
        return new Alerta(
                rs.getLong("id_alerta"),
                rs.getLong("id_linha"),
                JdbcUtil.getLongOuNulo(rs, "id_medicao"),
                JdbcUtil.getDataHora(rs, "dt_alerta"),
                rs.getString("nivel"),
                rs.getString("mensagem"),
                rs.getString("status"));
    }
}
