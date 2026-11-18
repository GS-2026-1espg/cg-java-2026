package br.com.fiap.citizentech.bo;

import br.com.fiap.citizentech.exception.RegraDeNegocioException;

import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeSet;

/** Validacoes reutilizadas pelos BOs. Todas lancam RegraDeNegocioException (HTTP 400). */
final class Validacao {

    private Validacao() {
    }

    static void obrigatorio(Object valor, String campo) {
        if (valor == null) {
            throw new RegraDeNegocioException("O campo '" + campo + "' e obrigatorio");
        }
    }

    static void texto(String valor, String campo, int tamanhoMax) {
        if (valor == null || valor.isBlank()) {
            throw new RegraDeNegocioException("O campo '" + campo + "' e obrigatorio");
        }
        if (valor.length() > tamanhoMax) {
            throw new RegraDeNegocioException(
                    "O campo '" + campo + "' deve ter no maximo " + tamanhoMax + " caracteres");
        }
    }

    static void valorPermitido(String valor, Set<String> permitidos, String campo) {
        if (valor == null || !permitidos.contains(valor)) {
            throw new RegraDeNegocioException(
                    "O campo '" + campo + "' deve ser um de: " + String.join(", ", new TreeSet<>(permitidos)));
        }
    }

    static void inteiroEntre(Integer valor, String campo, int min, int max) {
        obrigatorio(valor, campo);
        if (valor < min || valor > max) {
            throw new RegraDeNegocioException("O campo '" + campo + "' deve estar entre " + min + " e " + max);
        }
    }

    static void inteiroPositivo(Integer valor, String campo) {
        obrigatorio(valor, campo);
        if (valor <= 0) {
            throw new RegraDeNegocioException("O campo '" + campo + "' deve ser maior que zero");
        }
    }

    static void naoNegativo(BigDecimal valor, String campo) {
        obrigatorio(valor, campo);
        if (valor.signum() < 0) {
            throw new RegraDeNegocioException("O campo '" + campo + "' nao pode ser negativo");
        }
    }

    static void positivo(BigDecimal valor, String campo) {
        obrigatorio(valor, campo);
        if (valor.signum() <= 0) {
            throw new RegraDeNegocioException("O campo '" + campo + "' deve ser maior que zero");
        }
    }

    static void intervalo(BigDecimal valor, String campo, int min, int max) {
        obrigatorio(valor, campo);
        if (valor.compareTo(BigDecimal.valueOf(min)) < 0 || valor.compareTo(BigDecimal.valueOf(max)) > 0) {
            throw new RegraDeNegocioException(
                    "O campo '" + campo + "' deve estar entre " + min + " e " + max);
        }
    }
}
