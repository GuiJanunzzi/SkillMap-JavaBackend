package br.com.fiap.skillmap.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HabilidadeUpdateRequestDTO {

    // Todos os campos são opcionais na atualização
    @Size(min = 2, message = "O nome da habilidade deve ter no mínimo 2 caracteres")
    private String nome;

    private String descricao;

    private Long categoriaId; // Para permitir a troca de categoria
}
