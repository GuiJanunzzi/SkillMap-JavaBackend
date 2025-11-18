package br.com.fiap.skillmap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HabilidadeCreateRequestDTO {

    @NotBlank(message = "{validation.habilidade.nome.notblank}")
    private String nome;

    private String descricao;

    @NotNull(message = "{validation.geral.notnull}")
    private Long categoriaId;
}
