package br.com.fiap.citizentech.dao;

import br.com.fiap.citizentech.exception.DaoException;
import br.com.fiap.citizentech.model.Medicao;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MedicaoDAO {

    private static final String COLUNAS =
            "id_medicao, id_linha, id_ponto, dt_medicao, faixa_horario, tempo_viagem_min, lotacao_pct, intervalo_min";

    private final ConnectionFactory connectionFactory;

    public MedicaoDAO(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public List<Medicao> listar() {
        String sql = "SELECT " + COLUNAS + " FROM medicao ORDER BY id_medicao";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Medicao> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("Erro ao listar medicoes", e);
        }
    }

    public Optional<Medicao> buscarPorId(Long id) {
        String sql = "SELECT " + COLUNAS + " FROM medicao WHERE id_medicao = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao buscar medicao", e);
        }
    }

    public Medicao inserir(Medicao m) {
        String sql = "INSERT INTO medicao (id_linha, id_ponto, dt_medicao, faixa_horario, "
                + "tempo_viagem_min, lotacao_pct, intervalo_min) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID_MEDICAO"})) {
            preencher(ps, m);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return m.comId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new DaoException("Erro ao inserir medicao", e);
        }
    }

    public boolean atualizar(Medicao m) {
        String sql = "UPDATE medicao SET id_linha = ?, id_ponto = ?, dt_medicao = ?, faixa_horario = ?, "
                + "tempo_viagem_min = ?, lotacao_pct = ?, intervalo_min = ? WHERE id_medicao = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencher(ps, m);
            ps.setLong(8, m.idMedicao());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao atualizar medicao", e);
        }
    }

    public boolean deletar(Long id) {
        String sql = "DELETE FROM medicao WHERE id_medicao = ?";
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Erro ao excluir medicao", e);
        }
    }

    private void preencher(PreparedStatement ps, Medicao m) throws SQLException {
        ps.setLong(1, m.idLinha());
        ps.setLong(2, m.idPonto());
        ps.setTimestamp(3, JdbcUtil.toTimestamp(m.dtMedicao()));
        ps.setString(4, m.faixaHorario());
        ps.setInt(5, m.tempoViagemMin());
        ps.setInt(6, m.lotacaoPct());
        ps.setBigDecimal(7, m.intervaloMin());
    }

    private Medicao mapear(ResultSet rs) throws SQLException {
        return new Medicao(
                rs.getLong("id_medicao"),
                rs.getLong("id_linha"),
                rs.getLong("id_ponto"),
                JdbcUtil.getDataHora(rs, "dt_medicao"),
                rs.getString("faixa_horario"),
                rs.getInt("tempo_viagem_min"),
                rs.getInt("lotacao_pct"),
                rs.getBigDecimal("intervalo_min"));
    }
}
