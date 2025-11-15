package br.com.fiap.skillmap.dto;

import br.com.fiap.skillmap.model.Usuario;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private Set<HabilidadeResponseDTO> habilidadesPossuidas;
    private Set<HabilidadeResponseDTO> metas;

    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();

        // Converte o Set<Habilidade> (Model) para Set<HabilidadeResponseDTO>
        this.habilidadesPossuidas = usuario.getHabilidadesPossuidas().stream()
                .map(HabilidadeResponseDTO::new) // Usa o construtor do DTO
                .collect(Collectors.toSet());

        // Faz o mesmo para as metas
        this.metas = usuario.getMetas().stream()
                .map(HabilidadeResponseDTO::new)
                .collect(Collectors.toSet());
    }
}