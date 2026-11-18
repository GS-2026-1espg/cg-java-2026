package br.com.fiap.citizentech.bo;

import br.com.fiap.citizentech.dao.RelatoDAO;
import br.com.fiap.citizentech.exception.RecursoNaoEncontradoException;
import br.com.fiap.citizentech.exception.RegraDeNegocioException;
import br.com.fiap.citizentech.model.Relato;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RelatoBO {

    private static final Set<String> CATEGORIAS =
            Set.of("LOTACAO", "ATRASO", "FREQUENCIA", "SEGURANCA", "OUTRO");

    private final RelatoDAO dao;
    private final RegiaoBO regiaoBO;
    private final LinhaBO linhaBO;

    public RelatoBO(RelatoDAO dao, RegiaoBO regiaoBO, LinhaBO linhaBO) {
        this.dao = dao;
        this.regiaoBO = regiaoBO;
        this.linhaBO = linhaBO;
    }

    public List<Relato> listar() {
        return dao.listar();
    }

    public Relato buscar(Long id) {
        return dao.buscarPorId(id).orElseThrow(() -> new RecursoNaoEncontradoException("Relato", id));
    }

    public Relato criar(Relato r) {
        validar(r);
        return dao.inserir(r);
    }

    public Relato atualizar(Long id, Relato r) {
        validar(r);
        Relato atualizado = r.comId(id);
        if (!dao.atualizar(atualizado)) {
            throw new RecursoNaoEncontradoException("Relato", id);
        }
        return atualizado;
    }

    public void excluir(Long id) {
        if (!dao.deletar(id)) {
            throw new RecursoNaoEncontradoException("Relato", id);
        }
    }

    private void validar(Relato r) {
        Validacao.obrigatorio(r, "corpo da requisicao");
        regiaoBO.garantirExiste(r.idRegiao());
        if (r.idLinha() != null) {          // linha e opcional: relato pode ser geral da regiao
            linhaBO.garantirExiste(r.idLinha());
        }
        Validacao.obrigatorio(r.dtRelato(), "dtRelato");
        Validacao.valorPermitido(r.categoria(), CATEGORIAS, "categoria");
        Validacao.texto(r.descricao(), "descricao", 300);
        if (r.tempoPerdidoMin() != null && r.tempoPerdidoMin() < 0) {
            throw new RegraDeNegocioException("O campo 'tempoPerdidoMin' nao pode ser negativo");
        }
    }
}
