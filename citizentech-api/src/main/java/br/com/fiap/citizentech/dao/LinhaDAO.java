package br.com.fiap.citizentech.dao;

import br.com.fiap.citizentech.exception.DaoException;
import br.com.fiap.citizentech.model.Linha;
import br.com.fiap.citizentech.model.LinhaRanking;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class LinhaDAO {

    private static final String COLUNAS =
            "id_linha, codigo, nome, id_regiao, frota_atual, intervalo_medio_min";

    private final ConnectionFactory connectionFactory;

    public LinhaDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public List<Linha> listar() {
        String sql = "SELECT " + COLUNAS + " FROM linha ORDER BY id_linha";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Linha> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("Erro ao listar linhas", e);
        }
    }

    public Optional<Linha> buscarPorId(Long id) {
        String sql = "SELECT " + COLUNAS + " FROM linha WHERE id_linha = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao buscar linha", e);
        }
    }

    public Linha inserir(Linha l) {
        String sql = "INSERT INTO linha (codigo, nome, id_regiao, frota_atual, intervalo_medio_min) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID_LINHA"})) {
            ps.setString(1, l.codigo());
            ps.setString(2, l.nome());
            ps.setLong(3, l.idRegiao());
            ps.setInt(4, l.frotaAtual());
            ps.setBigDecimal(5, l.intervaloMedioMin());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return l.comId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao inserir linha", e);
        }
    }

    public boolean atualizar(Linha l) {
        String sql = "UPDATE linha SET codigo = ?, nome = ?, id_regiao = ?, frota_atual = ?, "
                + "intervalo_medio_min = ? WHERE id_linha = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, l.codigo());
            ps.setString(2, l.nome());
            ps.setLong(3, l.idRegiao());
            ps.setInt(4, l.frotaAtual());
            ps.setBigDecimal(5, l.intervaloMedioMin());
            ps.setLong(6, l.idLinha());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao atualizar linha", e);
        }
    }

    public boolean deletar(Long id) {
        String sql = "DELETE FROM linha WHERE id_linha = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao excluir linha", e);
        }
    }

    /** Ranking: tempo e lotacao medios por linha nos picos (PICO_AM e PICO_PM), pior primeiro. */
    public List<LinhaRanking> ranking() {
        String sql = "SELECT l.id_linha, l.codigo, l.nome, "
                + "ROUND(AVG(m.tempo_viagem_min), 1) AS tempo_medio, "
                + "ROUND(AVG(m.lotacao_pct), 1) AS lotacao_media, "
                + "COUNT(*) AS total "
                + "FROM linha l INNER JOIN medicao m ON m.id_linha = l.id_linha "
                + "WHERE m.faixa_horario IN ('PICO_AM', 'PICO_PM') "
                + "GROUP BY l.id_linha, l.codigo, l.nome "
                + "ORDER BY tempo_medio DESC";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<LinhaRanking> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(new LinhaRanking(
                        rs.getLong("id_linha"),
                        rs.getString("codigo"),
                        rs.getString("nome"),
                        rs.getBigDecimal("tempo_medio"),
                        rs.getBigDecimal("lotacao_media"),
                        rs.getLong("total")));
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("Erro ao calcular ranking de linhas", e);
        }
    }

    private Linha mapear(ResultSet rs) throws SQLException {
        return new Linha(
                rs.getLong("id_linha"),
                rs.getString("codigo"),
                rs.getString("nome"),
                rs.getLong("id_regiao"),
                rs.getInt("frota_atual"),
                rs.getBigDecimal("intervalo_medio_min"));
    }
}
