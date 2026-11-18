package br.com.fiap.citizentech.controller;

import br.com.fiap.citizentech.bo.RegiaoBO;
import br.com.fiap.citizentech.model.Regiao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/regioes")
public class RegiaoController {

    private final RegiaoBO bo;

    public RegiaoController(RegiaoBO bo) {
        this.bo = bo;
    }

    @GetMapping
    public List<Regiao> listar() {
        return bo.listar();
    }

    @GetMapping("/{id}")
    public Regiao buscar(@PathVariable Long id) {
        return bo.buscar(id);
    }

    @PostMapping
    public ResponseEntity<Regiao> criar(@RequestBody Regiao regiao) {
        Regiao criada = bo.criar(regiao);
        return ResponseEntity.created(URI.create("/regioes/" + criada.idRegiao())).body(criada);
    }

    @PutMapping("/{id}")
    public Regiao atualizar(@PathVariable Long id, @RequestBody Regiao regiao) {
        return bo.atualizar(id, regiao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        bo.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
