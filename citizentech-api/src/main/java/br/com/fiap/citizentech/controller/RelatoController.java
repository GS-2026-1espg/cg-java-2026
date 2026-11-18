package br.com.fiap.citizentech.controller;

import br.com.fiap.citizentech.bo.RelatoBO;
import br.com.fiap.citizentech.model.Relato;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/relatos")
public class RelatoController {

    private final RelatoBO bo;

    public RelatoController(RelatoBO bo) {
        this.bo = bo;
    }

    @GetMapping
    public List<Relato> listar() {
        return bo.listar();
    }

    @GetMapping("/{id}")
    public Relato buscar(@PathVariable Long id) {
        return bo.buscar(id);
    }

    @PostMapping
    public ResponseEntity<Relato> criar(@RequestBody Relato relato) {
        Relato criado = bo.criar(relato);
        return ResponseEntity.created(URI.create("/relatos/" + criado.idRelato())).body(criado);
    }

    @PutMapping("/{id}")
    public Relato atualizar(@PathVariable Long id, @RequestBody Relato relato) {
        return bo.atualizar(id, relato);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        bo.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
