package br.com.fiap.skillmap.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "T_SKM_USUARIO")
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "T_SKM_USU_HABILIDADE",
            joinColumns = @JoinColumn(name = "ID_USUARIO"),
            inverseJoinColumns = @JoinColumn(name = "ID_HABILIDADE")
    )
    private Set<Habilidade> habilidadesPossuidas = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "T_SKM_USU_META",
            joinColumns = @JoinColumn(name = "ID_USUARIO"),
            inverseJoinColumns = @JoinColumn(name = "ID_HABILIDADE")
    )
    private Set<Habilidade> metas = new HashSet<>();



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.senha; // Retorna a senha do banco
    }

    @Override
    public String getUsername() {
        return this.email; // O "username" é o email
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // A conta nunca expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // A conta nunca é bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // As credenciais nunca expiram
    }

    @Override
    public boolean isEnabled() {
        return true; // A conta está sempre habilitada
    }
}