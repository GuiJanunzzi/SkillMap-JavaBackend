package br.com.fiap.skillmap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HabilidadeCreateRequestDTO {

    @NotBlank(message = "O nome da habilidade é obrigatório")
    private String nome;

    private String descricao;

    @NotNull(message = "O ID da categoria é obrigatório")
    private Long categoriaId;
}
