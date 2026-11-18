package br.com.fiap.citizentech.bo;

import br.com.fiap.citizentech.dao.AlertaDAO;
import br.com.fiap.citizentech.exception.RecursoNaoEncontradoException;
import br.com.fiap.citizentech.model.Alerta;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AlertaBO {

    private static final Set<String> NIVEIS = Set.of("BAIXO", "MEDIO", "ALTO", "CRITICO");
    private static final Set<String> STATUS = Set.of("ABERTO", "EM_ANALISE", "RESOLVIDO");

    private final AlertaDAO dao;
    private final LinhaBO linhaBO;
    private final MedicaoBO medicaoBO;

    public AlertaBO(AlertaDAO dao, LinhaBO linhaBO, MedicaoBO medicaoBO) {
        this.dao = dao;
        this.linhaBO = linhaBO;
        this.medicaoBO = medicaoBO;
    }

    public List<Alerta> listar() {
        return dao.listar();
    }

    public Alerta buscar(Long id) {
        return dao.buscarPorId(id).orElseThrow(() -> new RecursoNaoEncontradoException("Alerta", id));
    }

    public Alerta criar(Alerta a) {
        Alerta comPadrao = aplicarStatusPadrao(a);
        validar(comPadrao);
        return dao.inserir(comPadrao);
    }

    public Alerta atualizar(Long id, Alerta a) {
        Alerta comPadrao = aplicarStatusPadrao(a);
        validar(comPadrao);
        Alerta atualizado = comPadrao.comId(id);
        if (!dao.atualizar(atualizado)) {
            throw new RecursoNaoEncontradoException("Alerta", id);
        }
        return atualizado;
    }

    public void excluir(Long id) {
        if (!dao.deletar(id)) {
            throw new RecursoNaoEncontradoException("Alerta", id);
        }
    }

    /** Se o status nao for informado, o alerta nasce ABERTO (padrao do banco). */
    private Alerta aplicarStatusPadrao(Alerta a) {
        Validacao.obrigatorio(a, "corpo da requisicao");
        return a.status() == null ? a.comStatus("ABERTO") : a;
    }

    private void validar(Alerta a) {
        linhaBO.garantirExiste(a.idLinha());
        if (a.idMedicao() != null) {        // medicao e opcional: alerta pode ser preventivo
            medicaoBO.garantirExiste(a.idMedicao());
        }
        Validacao.obrigatorio(a.dtAlerta(), "dtAlerta");
        Validacao.valorPermitido(a.nivel(), NIVEIS, "nivel");
        Validacao.texto(a.mensagem(), "mensagem", 200);
        Validacao.valorPermitido(a.status(), STATUS, "status");
    }
}
