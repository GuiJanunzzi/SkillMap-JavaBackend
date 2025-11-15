package br.com.fiap.skillmap.dto;

import br.com.fiap.skillmap.model.Categoria;
import lombok.Data;

@Data
public class CategoriaResponseDTO {

    private Long id;
    private String nome;

    // Construtor que converte o Model para este DTO
    public CategoriaResponseDTO(Categoria categoria) {
        this.id = categoria.getId();
        this.nome = categoria.getNome();
    }
}
