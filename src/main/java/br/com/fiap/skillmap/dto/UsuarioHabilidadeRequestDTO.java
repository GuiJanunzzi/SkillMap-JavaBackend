package br.com.fiap.skillmap.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioHabilidadeRequestDTO {

    @NotNull(message = "O ID da habilidade é obrigatório")
    private Long habilidadeId;
}