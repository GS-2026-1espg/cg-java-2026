package br.com.fiap.citizentech.bo;

import br.com.fiap.citizentech.dao.RegiaoDAO;
import br.com.fiap.citizentech.exception.RecursoNaoEncontradoException;
import br.com.fiap.citizentech.exception.RegraDeNegocioException;
import br.com.fiap.citizentech.model.Regiao;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public class RegiaoBO {

    private static final Set<String> ZONAS = Set.of("Norte", "Sul", "Leste", "Oeste", "Centro");

    private final RegiaoDAO dao;

    public RegiaoBO(RegiaoDAO dao) {
        this.dao = dao;
    }

    public List<Regiao> listar() {
        return dao.listar();
    }

    public Regiao buscar(Long id) {
        return dao.buscarPorId(id).orElseThrow(() -> new RecursoNaoEncontradoException("Regiao", id));
    }

    public Regiao criar(Regiao r) {
        validar(r);
        return dao.inserir(r);
    }

    public Regiao atualizar(Long id, Regiao r) {
        validar(r);
        Regiao atualizada = r.comId(id);
        if (!dao.atualizar(atualizada)) {
            throw new RecursoNaoEncontradoException("Regiao", id);
        }
        return atualizada;
    }

    public void excluir(Long id) {
        if (!dao.deletar(id)) {
            throw new RecursoNaoEncontradoException("Regiao", id);
        }
    }

    /** Verifica se a regiao existe; usado por Linha, Ponto e demais BOs. */
    public void garantirExiste(Long id) {
        Validacao.obrigatorio(id, "idRegiao");
        if (dao.buscarPorId(id).isEmpty()) {
            throw new RegraDeNegocioException("A regiao " + id + " nao existe");
        }
    }

    private void validar(Regiao r) {
        Validacao.obrigatorio(r, "corpo da requisicao");
        Validacao.texto(r.nome(), "nome", 80);
        if (r.zona() == null || !ZONAS.contains(r.zona())) {
            throw new RegraDeNegocioException("A zona deve ser uma de: Norte, Sul, Leste, Oeste, Centro");
        }
        Validacao.naoNegativo(r.distanciaTerminalKm(), "distanciaTerminalKm");
        Validacao.naoNegativo(r.rendaMedia(), "rendaMedia");
        Validacao.obrigatorio(r.populacao(), "populacao");
        if (r.populacao() <= 0) {
            throw new RegraDeNegocioException("O campo 'populacao' deve ser maior que zero");
        }
    }
}
