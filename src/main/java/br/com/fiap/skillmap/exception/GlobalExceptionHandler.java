package br.com.fiap.skillmap.exception;

import br.com.fiap.skillmap.dto.ErrorResponseDTO;
import br.com.fiap.skillmap.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice // "Aconselha" todos os @RestControllers
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageService messageService;

    public GlobalExceptionHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    // Este metodo captura QUALQUER ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {

        ErrorResponseDTO error = new ErrorResponseDTO(
                ex.getMessage(),
                request.getRequestURI(),
                HttpStatus.NOT_FOUND.value(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Este metodo captura QUALQUER ResourceInUseException
    @ExceptionHandler(ResourceInUseException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceInUse(ResourceInUseException ex, HttpServletRequest request) {

        ErrorResponseDTO error = new ErrorResponseDTO(
                ex.getMessage(),
                request.getRequestURI(),
                HttpStatus.CONFLICT.value(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // Captura erros de API da Spring AI (como Quota Excedida 429, ou API Key inválida 401)
    @ExceptionHandler(NonTransientAiException.class)
    public ResponseEntity<ErrorResponseDTO> handleAiException(NonTransientAiException ex, HttpServletRequest request) {

        log.error("Erro da API de IA: {}", ex.getMessage());

        String mensagemUsuario = messageService.get("ai.error.quota");

        ErrorResponseDTO error = new ErrorResponseDTO(
                mensagemUsuario,
                request.getRequestURI(),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }
}