package br.com.fiap.citizentech.dao;

import br.com.fiap.citizentech.exception.DaoException;
import br.com.fiap.citizentech.model.Regiao;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RegiaoDAO {

    private static final String COLUNAS =
            "id_regiao, nome, zona, distancia_terminal_km, renda_media, populacao";

    private final ConnectionFactory connectionFactory;

    public RegiaoDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public List<Regiao> listar() {
        String sql = "SELECT " + COLUNAS + " FROM regiao ORDER BY id_regiao";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Regiao> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("Erro ao listar regioes", e);
        }
    }

    public Optional<Regiao> buscarPorId(Long id) {
        String sql = "SELECT " + COLUNAS + " FROM regiao WHERE id_regiao = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao buscar regiao", e);
        }
    }

    public Regiao inserir(Regiao r) {
        String sql = "INSERT INTO regiao (nome, zona, distancia_terminal_km, renda_media, populacao) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID_REGIAO"})) {
            ps.setString(1, r.nome());
            ps.setString(2, r.zona());
            ps.setBigDecimal(3, r.distanciaTerminalKm());
            ps.setBigDecimal(4, r.rendaMedia());
            ps.setLong(5, r.populacao());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return r.comId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao inserir regiao", e);
        }
    }

    /** Retorna true se alguma linha foi atualizada. */
    public boolean atualizar(Regiao r) {
        String sql = "UPDATE regiao SET nome = ?, zona = ?, distancia_terminal_km = ?, "
                + "renda_media = ?, populacao = ? WHERE id_regiao = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.nome());
            ps.setString(2, r.zona());
            ps.setBigDecimal(3, r.distanciaTerminalKm());
            ps.setBigDecimal(4, r.rendaMedia());
            ps.setLong(5, r.populacao());
            ps.setLong(6, r.idRegiao());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao atualizar regiao", e);
        }
    }

    /** Retorna true se alguma linha foi removida. */
    public boolean deletar(Long id) {
        String sql = "DELETE FROM regiao WHERE id_regiao = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao excluir regiao", e);
        }
    }

    private Regiao mapear(ResultSet rs) throws SQLException {
        return new Regiao(
                rs.getLong("id_regiao"),
                rs.getString("nome"),
                rs.getString("zona"),
                rs.getBigDecimal("distancia_terminal_km"),
                rs.getBigDecimal("renda_media"),
                rs.getLong("populacao"));
    }
}
