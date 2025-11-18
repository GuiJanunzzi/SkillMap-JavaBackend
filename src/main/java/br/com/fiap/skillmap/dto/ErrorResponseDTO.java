package br.com.fiap.skillmap.dto;
import java.time.Instant;

public record ErrorResponseDTO(
        String message,
        String path,
        int statusCode,
        Instant timestamp
) {}