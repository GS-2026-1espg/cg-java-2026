package br.com.fiap.citizentech.controller;

import br.com.fiap.citizentech.bo.AlertaBO;
import br.com.fiap.citizentech.model.Alerta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/alertas")
public class AlertaController {

    private final AlertaBO bo;

    public AlertaController(AlertaBO bo) {
        this.bo = bo;
    }

    @GetMapping
    public List<Alerta> listar() {
        return bo.listar();
    }

    @GetMapping("/{id}")
    public Alerta buscar(@PathVariable Long id) {
        return bo.buscar(id);
    }

    @PostMapping
    public ResponseEntity<Alerta> criar(@RequestBody Alerta alerta) {
        Alerta criado = bo.criar(alerta);
        return ResponseEntity.created(URI.create("/alertas/" + criado.idAlerta())).body(criado);
    }

    @PutMapping("/{id}")
    public Alerta atualizar(@PathVariable Long id, @RequestBody Alerta alerta) {
        return bo.atualizar(id, alerta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        bo.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
