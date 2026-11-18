package br.com.fiap.citizentech.dao;

import br.com.fiap.citizentech.exception.DaoException;
import br.com.fiap.citizentech.model.LinhaPonto;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LinhaPontoDAO {

    private final ConnectionFactory connectionFactory;

    public LinhaPontoDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    /** Pontos de uma linha, na ordem do trajeto. */
    public List<LinhaPonto> listarPorLinha(Long idLinha) {
        String sql = "SELECT id_linha, id_ponto, ordem FROM linha_ponto WHERE id_linha = ? ORDER BY ordem";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idLinha);
            try (ResultSet rs = ps.executeQuery()) {
                List<LinhaPonto> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(new LinhaPonto(rs.getLong("id_linha"), rs.getLong("id_ponto"), rs.getInt("ordem")));
                }
                return lista;
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao listar pontos da linha", e);
        }
    }

    public void inserir(LinhaPonto lp) {
        String sql = "INSERT INTO linha_ponto (id_linha, id_ponto, ordem) VALUES (?, ?, ?)";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, lp.idLinha());
            ps.setLong(2, lp.idPonto());
            ps.setInt(3, lp.ordem());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Erro ao vincular ponto a linha", e);
        }
    }

    public boolean atualizarOrdem(Long idLinha, Long idPonto, Integer ordem) {
        String sql = "UPDATE linha_ponto SET ordem = ? WHERE id_linha = ? AND id_ponto = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ordem);
            ps.setLong(2, idLinha);
            ps.setLong(3, idPonto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao atualizar ordem do ponto", e);
        }
    }

    public boolean deletar(Long idLinha, Long idPonto) {
        String sql = "DELETE FROM linha_ponto WHERE id_linha = ? AND id_ponto = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, idLinha);
            ps.setLong(2, idPonto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao desvincular ponto da linha", e);
        }
    }
}
