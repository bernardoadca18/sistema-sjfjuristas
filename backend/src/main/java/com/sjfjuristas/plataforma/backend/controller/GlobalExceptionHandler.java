package com.sjfjuristas.plataforma.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sjfjuristas.plataforma.backend.exceptions.RegistrationConflictException;
import com.sjfjuristas.plataforma.backend.exceptions.UserNotFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * Manipulador de exceções global para a aplicação.
 * Captura exceções específicas e as converte em respostas HTTP padronizadas.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Captura erros de desserialização do corpo da requisição.
     * Isso acontece antes da validação, quando o formato dos dados está incorreto (ex: texto onde deveria ser número).
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = "O corpo da requisição está malformado. Verifique os tipos de dados enviados.";
        // Tenta fornecer uma mensagem mais específica, se disponível.
        Throwable cause = ex.getMostSpecificCause();
        if (cause != null) {
            message = "Falha ao ler a requisição: " + cause.getMessage();
        }
        log.warn("Falha na desserialização da requisição: {}", message, ex);
        return new ResponseEntity<>(Map.of("error", message), HttpStatus.BAD_REQUEST);
    }

    /**
     * Captura erros de validação de DTOs anotados com @Valid.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.warn("Erro de validação nos dados da requisição: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Captura erros de violação de integridade do banco de dados (ex: chaves únicas duplicadas).
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Erro de integridade dos dados. Um campo único (como CPF ou Email) provavelmente já existe no sistema.";
        // Tenta obter a causa mais específica do erro do banco de dados.
        if (ex.getMostSpecificCause() != null) {
            message = "Conflito de dados: " + ex.getMostSpecificCause().getMessage();
        }
        log.error("Erro de integridade de dados: {}", message, ex);
        // 409 Conflict é mais semântico para chaves duplicadas.
        return new ResponseEntity<>(Map.of("error", message), HttpStatus.CONFLICT);
    }

    /**
     * Captura exceções de regras de negócio (ex: status não encontrado, perfil inválido).
     */
    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<Map<String, String>> handleBusinessExceptions(RuntimeException ex) {
        log.warn("Erro de regra de negócio: {}", ex.getMessage());
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Captura todas as outras exceções não tratadas.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
        log.error("Erro inesperado no servidor", ex);
        return new ResponseEntity<>(Map.of("error", "Ocorreu um erro interno inesperado."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex) {
        log.warn("Tentativa de acesso com usuário não encontrado: {}", ex.getMessage());
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    //@ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    //public ResponseEntity<Map<String, String>> handleBusinessExceptionsEntity(RuntimeException ex) {
    //    log.warn("Erro de regra de negócio: {}", ex.getMessage());
    //    return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.BAD_REQUEST);
    //}

    @ExceptionHandler(RegistrationConflictException.class)
    public ResponseEntity<Map<String, String>> handleRegistrationConflict(RegistrationConflictException ex) {
        log.warn("Conflito de cadastro: {}", ex.getMessage());
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.CONFLICT);
    }
}