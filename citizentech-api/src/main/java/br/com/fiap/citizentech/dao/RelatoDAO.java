package br.com.fiap.citizentech.dao;

import br.com.fiap.citizentech.exception.DaoException;
import br.com.fiap.citizentech.model.Relato;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RelatoDAO {

    private static final String COLUNAS =
            "id_relato, id_regiao, id_linha, dt_relato, categoria, descricao, tempo_perdido_min";

    private final ConnectionFactory connectionFactory;

    public RelatoDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public List<Relato> listar() {
        String sql = "SELECT " + COLUNAS + " FROM relato ORDER BY id_relato";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Relato> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("Erro ao listar relatos", e);
        }
    }

    public Optional<Relato> buscarPorId(Long id) {
        String sql = "SELECT " + COLUNAS + " FROM relato WHERE id_relato = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao buscar relato", e);
        }
    }

    public Relato inserir(Relato r) {
        String sql = "INSERT INTO relato (id_regiao, id_linha, dt_relato, categoria, descricao, "
                + "tempo_perdido_min) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID_RELATO"})) {
            preencher(ps, r);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return r.comId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao inserir relato", e);
        }
    }

    public boolean atualizar(Relato r) {
        String sql = "UPDATE relato SET id_regiao = ?, id_linha = ?, dt_relato = ?, categoria = ?, "
                + "descricao = ?, tempo_perdido_min = ? WHERE id_relato = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencher(ps, r);
            ps.setLong(7, r.idRelato());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao atualizar relato", e);
        }
    }

    public boolean deletar(Long id) {
        String sql = "DELETE FROM relato WHERE id_relato = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao excluir relato", e);
        }
    }

    private void preencher(PreparedStatement ps, Relato r) throws SQLException {
        ps.setLong(1, r.idRegiao());
        JdbcUtil.setLongOuNulo(ps, 2, r.idLinha());
        ps.setTimestamp(3, JdbcUtil.toTimestamp(r.dtRelato()));
        ps.setString(4, r.categoria());
        ps.setString(5, r.descricao());
        JdbcUtil.setIntOuNulo(ps, 6, r.tempoPerdidoMin());
    }

    private Relato mapear(ResultSet rs) throws SQLException {
        return new Relato(
                rs.getLong("id_relato"),
                rs.getLong("id_regiao"),
                JdbcUtil.getLongOuNulo(rs, "id_linha"),
                JdbcUtil.getDataHora(rs, "dt_relato"),
                rs.getString("categoria"),
                rs.getString("descricao"),
                JdbcUtil.getIntOuNulo(rs, "tempo_perdido_min"));
    }
}
