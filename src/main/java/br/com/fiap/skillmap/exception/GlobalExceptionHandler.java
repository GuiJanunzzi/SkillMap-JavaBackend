package br.com.fiap.skillmap.exception;

import br.com.fiap.skillmap.dto.ErrorResponseDTO;
import br.com.fiap.skillmap.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.retry.NonTransientAiException;
// 👇 IMPORT NOVO (Para capturar erro de banco/duplicidade)
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageService messageService;

    public GlobalExceptionHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    // --- ERROS DE NEGÓCIO ---
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

    // --- ERROS DE BANCO DE DADOS (DUPLICIDADE) ---
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {

        // Mensagem padrão para duplicidade
        String mensagem = "Este e-mail já está cadastrado. Por favor, tente outro.";

        log.warn("Violação de integridade: {}", ex.getMessage());

        ErrorResponseDTO error = new ErrorResponseDTO(
                mensagem,
                request.getRequestURI(),
                HttpStatus.CONFLICT.value(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // --- ERROS DE SEGURANÇA (LOGIN) ---
    @ExceptionHandler({BadCredentialsException.class, InternalAuthenticationServiceException.class})
    public ResponseEntity<ErrorResponseDTO> handleBadCredentials(Exception ex, HttpServletRequest request) {
        String mensagem = "E-mail ou senha inválidos. Por favor, tente novamente.";

        ErrorResponseDTO error = new ErrorResponseDTO(
                mensagem,
                request.getRequestURI(),
                HttpStatus.UNAUTHORIZED.value(),
                Instant.now()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // --- ERROS DE VALIDAÇÃO ---
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {

        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ErrorResponseDTO error = new ErrorResponseDTO(
                mensagem,
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                Instant.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // --- ERROS DE IA ---
    @ExceptionHandler(NonTransientAiException.class)
    public ResponseEntity<ErrorResponseDTO> handleAiException(NonTransientAiException ex, HttpServletRequest request) {
        log.error("Erro da API de IA: {}", ex.getMessage());

        String mensagemUsuario = messageService.get("ai.error.quota");

        ErrorResponseDTO error = new ErrorResponseDTO(
                mensagemUsuario != null ? mensagemUsuario : "Serviço de IA indisponível no momento.",
                request.getRequestURI(),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    // --- ERROS GENÉRICOS (Fallback) ---
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(Exception ex, HttpServletRequest request) {
        log.error("Erro não tratado: ", ex);

        ErrorResponseDTO error = new ErrorResponseDTO(
                "Ocorreu um erro inesperado no servidor.",
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}