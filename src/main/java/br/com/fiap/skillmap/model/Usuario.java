package br.com.fiap.skillmap.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Data
@Entity
@Table(name = "T_SKM_USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SKM_USUARIO")
    @SequenceGenerator(name = "SEQ_SKM_USUARIO", sequenceName = "SEQ_SKM_USUARIO", allocationSize = 1)
    @Column(name = "ID_USUARIO")
    private Long id;

    @Column(name = "NM_USUARIO", nullable = false)
    private String nome;

    @Column(name = "DS_EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "DS_SENHA", nullable = false)
    private String senha;

    // --- Relacionamento Muitos-para-Muitos (Habilidades Possuídas) ---
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "T_SKM_USU_HABILIDADE", // Tabela de junção
            joinColumns = @JoinColumn(name = "ID_USUARIO"), // Chave deste lado
            inverseJoinColumns = @JoinColumn(name = "ID_HABILIDADE") // Chave do outro lado
    )
    private Set<Habilidade> habilidadesPossuidas;

    // --- Relacionamento Muitos-para-Muitos (Metas) ---
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "T_SKM_USU_META", // Tabela de junção
            joinColumns = @JoinColumn(name = "ID_USUARIO"), // Chave deste lado
            inverseJoinColumns = @JoinColumn(name = "ID_HABILIDADE") // Chave do outro lado
    )
    private Set<Habilidade> metas;
}
