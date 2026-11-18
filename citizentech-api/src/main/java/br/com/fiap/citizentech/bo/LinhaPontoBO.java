package br.com.fiap.citizentech.bo;

import br.com.fiap.citizentech.dao.LinhaPontoDAO;
import br.com.fiap.citizentech.exception.RecursoNaoEncontradoException;
import br.com.fiap.citizentech.model.LinhaPonto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinhaPontoBO {

    private final LinhaPontoDAO dao;
    private final LinhaBO linhaBO;
    private final PontoBO pontoBO;

    public LinhaPontoBO(LinhaPontoDAO dao, LinhaBO linhaBO, PontoBO pontoBO) {
        this.dao = dao;
        this.linhaBO = linhaBO;
        this.pontoBO = pontoBO;
    }

    public List<LinhaPonto> listarPorLinha(Long idLinha) {
        linhaBO.buscar(idLinha);   // 404 se a linha nao existe
        return dao.listarPorLinha(idLinha);
    }

    public LinhaPonto vincular(Long idLinha, LinhaPonto corpo) {
        Validacao.obrigatorio(corpo, "corpo da requisicao");
        linhaBO.buscar(idLinha);
        pontoBO.garantirExiste(corpo.idPonto());
        Validacao.inteiroPositivo(corpo.ordem(), "ordem");
        LinhaPonto lp = new LinhaPonto(idLinha, corpo.idPonto(), corpo.ordem());
        dao.inserir(lp);           // PK duplicada vira 409 pelo handler
        return lp;
    }

    public LinhaPonto atualizarOrdem(Long idLinha, Long idPonto, LinhaPonto corpo) {
        Validacao.obrigatorio(corpo, "corpo da requisicao");
        Validacao.inteiroPositivo(corpo.ordem(), "ordem");
        if (!dao.atualizarOrdem(idLinha, idPonto, corpo.ordem())) {
            throw new RecursoNaoEncontradoException("Vinculo linha-ponto", idLinha);
        }
        return new LinhaPonto(idLinha, idPonto, corpo.ordem());
    }

    public void desvincular(Long idLinha, Long idPonto) {
        if (!dao.deletar(idLinha, idPonto)) {
            throw new RecursoNaoEncontradoException("Vinculo linha-ponto", idLinha);
        }
    }
}
