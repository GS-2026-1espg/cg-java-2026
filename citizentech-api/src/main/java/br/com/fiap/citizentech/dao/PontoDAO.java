package br.com.fiap.citizentech.dao;

import br.com.fiap.citizentech.exception.DaoException;
import br.com.fiap.citizentech.model.Ponto;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PontoDAO {

    private static final String COLUNAS = "id_ponto, nome, endereco, latitude, longitude, id_regiao";

    private final ConnectionFactory connectionFactory;

    public PontoDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public List<Ponto> listar() {
        String sql = "SELECT " + COLUNAS + " FROM ponto ORDER BY id_ponto";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Ponto> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("Erro ao listar pontos", e);
        }
    }

    public Optional<Ponto> buscarPorId(Long id) {
        String sql = "SELECT " + COLUNAS + " FROM ponto WHERE id_ponto = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao buscar ponto", e);
        }
    }

    public Ponto inserir(Ponto p) {
        String sql = "INSERT INTO ponto (nome, endereco, latitude, longitude, id_regiao) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID_PONTO"})) {
            ps.setString(1, p.nome());
            ps.setString(2, p.endereco());
            ps.setBigDecimal(3, p.latitude());
            ps.setBigDecimal(4, p.longitude());
            ps.setLong(5, p.idRegiao());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return p.comId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao inserir ponto", e);
        }
    }

    public boolean atualizar(Ponto p) {
        String sql = "UPDATE ponto SET nome = ?, endereco = ?, latitude = ?, longitude = ?, "
                + "id_regiao = ? WHERE id_ponto = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.nome());
            ps.setString(2, p.endereco());
            ps.setBigDecimal(3, p.latitude());
            ps.setBigDecimal(4, p.longitude());
            ps.setLong(5, p.idRegiao());
            ps.setLong(6, p.idPonto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao atualizar ponto", e);
        }
    }

    public boolean deletar(Long id) {
        String sql = "DELETE FROM ponto WHERE id_ponto = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao excluir ponto", e);
        }
    }

    private Ponto mapear(ResultSet rs) throws SQLException {
        return new Ponto(
                rs.getLong("id_ponto"),
                rs.getString("nome"),
                rs.getString("endereco"),
                rs.getBigDecimal("latitude"),
                rs.getBigDecimal("longitude"),
                rs.getLong("id_regiao"));
    }
}
