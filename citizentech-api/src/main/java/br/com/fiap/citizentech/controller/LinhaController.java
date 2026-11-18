package br.com.fiap.citizentech.controller;

import br.com.fiap.citizentech.bo.LinhaBO;
import br.com.fiap.citizentech.model.Linha;
import br.com.fiap.citizentech.model.LinhaRanking;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/linhas")
public class LinhaController {

    private final LinhaBO bo;

    public LinhaController(LinhaBO bo) {
        this.bo = bo;
    }

    @GetMapping
    public List<Linha> listar() {
        return bo.listar();
    }

    /** Ranking das linhas mais problematicas no pico (tempo e lotacao medios). */
    @GetMapping("/ranking")
    public List<LinhaRanking> ranking() {
        return bo.ranking();
    }

    @GetMapping("/{id}")
    public Linha buscar(@PathVariable Long id) {
        return bo.buscar(id);
    }

    @PostMapping
    public ResponseEntity<Linha> criar(@RequestBody Linha linha) {
        Linha criada = bo.criar(linha);
        return ResponseEntity.created(URI.create("/linhas/" + criada.idLinha())).body(criada);
    }

    @PutMapping("/{id}")
    public Linha atualizar(@PathVariable Long id, @RequestBody Linha linha) {
        return bo.atualizar(id, linha);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        bo.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
