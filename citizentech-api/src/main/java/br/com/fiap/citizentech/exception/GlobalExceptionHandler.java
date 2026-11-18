package br.com.fiap.citizentech.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/** Converte excecoes em respostas HTTP padronizadas. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> naoEncontrado(RecursoNaoEncontradoException e) {
        return resposta(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroResposta> regraDeNegocio(RegraDeNegocioException e) {
        return resposta(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErroResposta> requisicaoInvalida(Exception e) {
        return resposta(HttpStatus.BAD_REQUEST, "Requisicao invalida: corpo JSON ou parametro mal formatado");
    }

    @ExceptionHandler(DaoException.class)
    public ResponseEntity<ErroResposta> erroDeBanco(DaoException e) {
        switch (e.getCodigoOracle()) {
            case 1:     // ORA-00001: unique constraint
                return resposta(HttpStatus.CONFLICT, "Ja existe um registro com esse valor unico (nome ou codigo)");
            case 2291:  // ORA-02291: chave estrangeira pai nao existe
                return resposta(HttpStatus.CONFLICT, "Referencia invalida: o registro relacionado nao existe");
            case 2292:  // ORA-02292: existem registros filhos
                return resposta(HttpStatus.CONFLICT, "Nao e possivel excluir: existem registros que dependem deste");
            case 2290:  // ORA-02290: check constraint
                return resposta(HttpStatus.BAD_REQUEST, "Valor fora das regras permitidas pelo banco");
            default:
                log.error("Erro de banco", e);
                return resposta(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao acessar o banco de dados");
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> generico(Exception e) {
        log.error("Erro inesperado", e);
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
    }

    private ResponseEntity<ErroResposta> resposta(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status)
                .body(ErroResposta.de(status.value(), status.getReasonPhrase(), mensagem));
    }
}
