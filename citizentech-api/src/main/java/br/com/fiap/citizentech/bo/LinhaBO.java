package br.com.fiap.citizentech.bo;

import br.com.fiap.citizentech.dao.LinhaDAO;
import br.com.fiap.citizentech.exception.RecursoNaoEncontradoException;
import br.com.fiap.citizentech.exception.RegraDeNegocioException;
import br.com.fiap.citizentech.model.Linha;
import br.com.fiap.citizentech.model.LinhaRanking;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinhaBO {

    private final LinhaDAO dao;
    private final RegiaoBO regiaoBO;

    public LinhaBO(LinhaDAO dao, RegiaoBO regiaoBO) {
        this.dao = dao;
        this.regiaoBO = regiaoBO;
    }

    public List<Linha> listar() {
        return dao.listar();
    }

    public Linha buscar(Long id) {
        return dao.buscarPorId(id).orElseThrow(() -> new RecursoNaoEncontradoException("Linha", id));
    }

    public Linha criar(Linha l) {
        validar(l);
        return dao.inserir(l);
    }

    public Linha atualizar(Long id, Linha l) {
        validar(l);
        Linha atualizada = l.comId(id);
        if (!dao.atualizar(atualizada)) {
            throw new RecursoNaoEncontradoException("Linha", id);
        }
        return atualizada;
    }

    public void excluir(Long id) {
        if (!dao.deletar(id)) {
            throw new RecursoNaoEncontradoException("Linha", id);
        }
    }

    public List<LinhaRanking> ranking() {
        return dao.ranking();
    }

    /** Verifica se a linha existe; usado por Medicao, Relato e Alerta. */
    public void garantirExiste(Long id) {
        Validacao.obrigatorio(id, "idLinha");
        if (dao.buscarPorId(id).isEmpty()) {
            throw new RegraDeNegocioException("A linha " + id + " nao existe");
        }
    }

    private void validar(Linha l) {
        Validacao.obrigatorio(l, "corpo da requisicao");
        Validacao.texto(l.codigo(), "codigo", 10);
        if (l.codigo().length() < 4) {
            throw new RegraDeNegocioException("O campo 'codigo' deve ter pelo menos 4 caracteres");
        }
        Validacao.texto(l.nome(), "nome", 100);
        regiaoBO.garantirExiste(l.idRegiao());
        Validacao.obrigatorio(l.frotaAtual(), "frotaAtual");
        if (l.frotaAtual() < 0) {
            throw new RegraDeNegocioException("O campo 'frotaAtual' nao pode ser negativo");
        }
        Validacao.positivo(l.intervaloMedioMin(), "intervaloMedioMin");
    }
}
