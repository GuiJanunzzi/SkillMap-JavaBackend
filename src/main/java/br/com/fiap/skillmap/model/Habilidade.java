package br.com.fiap.skillmap.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "T_SKM_HABILIDADE")
public class Habilidade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SKM_HABILIDADE")
    @SequenceGenerator(name = "SEQ_SKM_HABILIDADE", sequenceName = "SEQ_SKM_HABILIDADE", allocationSize = 1)
    @Column(name = "ID_HABILIDADE")
    private Long id;

    @Column(name = "NM_HABILIDADE", nullable = false, unique = true)
    private String nome;

    @Column(name = "DS_HABILIDADE")
    private String descricao;

    @ManyToOne // Relacionamento: Muitas Habilidades pertencem a UMA Categoria
    @JoinColumn(name = "ID_CATEGORIA", nullable = false) // Chave estrangeira
    private Categoria categoria;
}
