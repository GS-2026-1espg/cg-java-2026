package br.com.fiap.citizentech.bo;

import br.com.fiap.citizentech.dao.PontoDAO;
import br.com.fiap.citizentech.exception.RecursoNaoEncontradoException;
import br.com.fiap.citizentech.exception.RegraDeNegocioException;
import br.com.fiap.citizentech.model.Ponto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PontoBO {

    private final PontoDAO dao;
    private final RegiaoBO regiaoBO;

    public PontoBO(PontoDAO dao, RegiaoBO regiaoBO) {
        this.dao = dao;
        this.regiaoBO = regiaoBO;
    }

    public List<Ponto> listar() {
        return dao.listar();
    }

    public Ponto buscar(Long id) {
        return dao.buscarPorId(id).orElseThrow(() -> new RecursoNaoEncontradoException("Ponto", id));
    }

    public Ponto criar(Ponto p) {
        validar(p);
        return dao.inserir(p);
    }

    public Ponto atualizar(Long id, Ponto p) {
        validar(p);
        Ponto atualizado = p.comId(id);
        if (!dao.atualizar(atualizado)) {
            throw new RecursoNaoEncontradoException("Ponto", id);
        }
        return atualizado;
    }

    public void excluir(Long id) {
        if (!dao.deletar(id)) {
            throw new RecursoNaoEncontradoException("Ponto", id);
        }
    }

    /** Verifica se o ponto existe; usado por Medicao e LINHA_PONTO. */
    public void garantirExiste(Long id) {
        Validacao.obrigatorio(id, "idPonto");
        if (dao.buscarPorId(id).isEmpty()) {
            throw new RegraDeNegocioException("O ponto " + id + " nao existe");
        }
    }

    private void validar(Ponto p) {
        Validacao.obrigatorio(p, "corpo da requisicao");
        Validacao.texto(p.nome(), "nome", 100);
        Validacao.texto(p.endereco(), "endereco", 150);
        Validacao.intervalo(p.latitude(), "latitude", -90, 90);
        Validacao.intervalo(p.longitude(), "longitude", -180, 180);
        regiaoBO.garantirExiste(p.idRegiao());
    }
}
