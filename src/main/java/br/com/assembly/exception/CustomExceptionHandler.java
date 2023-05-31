package br.com.assembly.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException ex) {
        // Obtém a mensagem de erro da CustomException
        String mensagemErro = ex.getMessage();
        var httpStatus = HttpStatus.valueOf(ex.getHttpStatus());

        // Cria um objeto de resposta de erro personalizado
        var errorResponse = new CustomErrorResponse(httpStatus.value(), mensagemErro);

        // Retorna a resposta de erro com o código de status adequado
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}

