package br.com.fiap.citizentech.bo;

import br.com.fiap.citizentech.dao.MedicaoDAO;
import br.com.fiap.citizentech.exception.RecursoNaoEncontradoException;
import br.com.fiap.citizentech.exception.RegraDeNegocioException;
import br.com.fiap.citizentech.model.Medicao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class MedicaoBO {

    private static final Set<String> FAIXAS = Set.of("PICO_AM", "ENTREPICO", "PICO_PM", "NOITE");

    private final MedicaoDAO dao;
    private final LinhaBO linhaBO;
    private final PontoBO pontoBO;

    public MedicaoBO(MedicaoDAO dao, LinhaBO linhaBO, PontoBO pontoBO) {
        this.dao = dao;
        this.linhaBO = linhaBO;
        this.pontoBO = pontoBO;
    }

    public List<Medicao> listar() {
        return dao.listar();
    }

    public Medicao buscar(Long id) {
        return dao.buscarPorId(id).orElseThrow(() -> new RecursoNaoEncontradoException("Medicao", id));
    }

    public Medicao criar(Medicao m) {
        validar(m);
        return dao.inserir(m);
    }

    public Medicao atualizar(Long id, Medicao m) {
        validar(m);
        Medicao atualizada = m.comId(id);
        if (!dao.atualizar(atualizada)) {
            throw new RecursoNaoEncontradoException("Medicao", id);
        }
        return atualizada;
    }

    public void excluir(Long id) {
        if (!dao.deletar(id)) {
            throw new RecursoNaoEncontradoException("Medicao", id);
        }
    }

    /** Verifica se a medicao existe; usado por Alerta. */
    public void garantirExiste(Long id) {
        Validacao.obrigatorio(id, "idMedicao");
        if (dao.buscarPorId(id).isEmpty()) {
            throw new RegraDeNegocioException("A medicao " + id + " nao existe");
        }
    }

    private void validar(Medicao m) {
        Validacao.obrigatorio(m, "corpo da requisicao");
        linhaBO.garantirExiste(m.idLinha());
        pontoBO.garantirExiste(m.idPonto());
        Validacao.obrigatorio(m.dtMedicao(), "dtMedicao");
        Validacao.valorPermitido(m.faixaHorario(), FAIXAS, "faixaHorario");
        Validacao.inteiroPositivo(m.tempoViagemMin(), "tempoViagemMin");
        Validacao.inteiroEntre(m.lotacaoPct(), "lotacaoPct", 0, 100);
        Validacao.positivo(m.intervaloMin(), "intervaloMin");
    }
}
