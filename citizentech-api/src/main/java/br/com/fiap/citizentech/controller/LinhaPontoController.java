package br.com.fiap.citizentech.controller;

import br.com.fiap.citizentech.bo.LinhaPontoBO;
import br.com.fiap.citizentech.model.LinhaPonto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/** Trajeto de uma linha: pontos e a ordem em que aparecem (tabela LINHA_PONTO). */
@RestController
@RequestMapping("/linhas/{idLinha}/pontos")
public class LinhaPontoController {

    private final LinhaPontoBO bo;

    public LinhaPontoController(LinhaPontoBO bo) {
        this.bo = bo;
    }

    @GetMapping
    public List<LinhaPonto> listar(@PathVariable Long idLinha) {
        return bo.listarPorLinha(idLinha);
    }

    @PostMapping
    public ResponseEntity<LinhaPonto> vincular(@PathVariable Long idLinha, @RequestBody LinhaPonto corpo) {
        LinhaPonto criado = bo.vincular(idLinha, corpo);
        return ResponseEntity.created(URI.create("/linhas/" + idLinha + "/pontos/" + criado.idPonto())).body(criado);
    }

    @PutMapping("/{idPonto}")
    public LinhaPonto atualizarOrdem(@PathVariable Long idLinha, @PathVariable Long idPonto,
                                     @RequestBody LinhaPonto corpo) {
        return bo.atualizarOrdem(idLinha, idPonto, corpo);
    }

    @DeleteMapping("/{idPonto}")
    public ResponseEntity<Void> desvincular(@PathVariable Long idLinha, @PathVariable Long idPonto) {
        bo.desvincular(idLinha, idPonto);
        return ResponseEntity.noContent().build();
    }
}
