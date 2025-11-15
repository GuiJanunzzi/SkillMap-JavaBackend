package br.com.fiap.skillmap.dto;

import br.com.fiap.skillmap.model.Habilidade;
import lombok.Data;

@Data
public class HabilidadeResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private CategoriaResponseDTO categoria; // Aninha o DTO da Categoria

    public HabilidadeResponseDTO(Habilidade habilidade) {
        this.id = habilidade.getId();
        this.nome = habilidade.getNome();
        this.descricao = habilidade.getDescricao();
        // Converte a Categoria (Model) para CategoriaResponseDTO
        this.categoria = new CategoriaResponseDTO(habilidade.getCategoria());
    }
}