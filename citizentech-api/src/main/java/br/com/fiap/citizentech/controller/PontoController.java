package br.com.fiap.citizentech.controller;

import br.com.fiap.citizentech.bo.PontoBO;
import br.com.fiap.citizentech.model.Ponto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pontos")
public class PontoController {

    private final PontoBO bo;

    public PontoController(PontoBO bo) {
        this.bo = bo;
    }

    @GetMapping
    public List<Ponto> listar() {
        return bo.listar();
    }

    @GetMapping("/{id}")
    public Ponto buscar(@PathVariable Long id) {
        return bo.buscar(id);
    }

    @PostMapping
    public ResponseEntity<Ponto> criar(@RequestBody Ponto ponto) {
        Ponto criado = bo.criar(ponto);
        return ResponseEntity.created(URI.create("/pontos/" + criado.idPonto())).body(criado);
    }

    @PutMapping("/{id}")
    public Ponto atualizar(@PathVariable Long id, @RequestBody Ponto ponto) {
        return bo.atualizar(id, ponto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        bo.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
