package br.com.fiap.skillmap.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriaRequestDTO {

    @NotBlank(message = "{validation.categoria.nome.notblank}")
    private String nome;
}