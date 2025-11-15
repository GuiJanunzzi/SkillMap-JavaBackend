package br.com.fiap.skillmap.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "T_SKM_CATEGORIA")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SKM_CATEGORIA")
    @SequenceGenerator(name = "SEQ_SKM_CATEGORIA", sequenceName = "SEQ_SKM_CATEGORIA", allocationSize = 1)
    @Column(name = "ID_CATEGORIA")
    private Long id;

    @Column(name = "NM_CATEGORIA", nullable = false, unique = true)
    private String nome;

}