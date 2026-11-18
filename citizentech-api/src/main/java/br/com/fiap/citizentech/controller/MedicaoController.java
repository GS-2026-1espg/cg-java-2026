package br.com.fiap.citizentech.controller;

import br.com.fiap.citizentech.bo.MedicaoBO;
import br.com.fiap.citizentech.model.Medicao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/medicoes")
public class MedicaoController {

    private final MedicaoBO bo;

    public MedicaoController(MedicaoBO bo) {
        this.bo = bo;
    }

    @GetMapping
    public List<Medicao> listar() {
        return bo.listar();
    }

    @GetMapping("/{id}")
    public Medicao buscar(@PathVariable Long id) {
        return bo.buscar(id);
    }

    @PostMapping
    public ResponseEntity<Medicao> criar(@RequestBody Medicao medicao) {
        Medicao criada = bo.criar(medicao);
        return ResponseEntity.created(URI.create("/medicoes/" + criada.idMedicao())).body(criada);
    }

    @PutMapping("/{id}")
    public Medicao atualizar(@PathVariable Long id, @RequestBody Medicao medicao) {
        return bo.atualizar(id, medicao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        bo.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
