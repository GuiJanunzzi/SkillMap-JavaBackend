package br.com.fiap.skillmap.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioCreateRequestDTO {

    @NotBlank(message = "{validation.geral.notblank}")
    @Size(min = 3, message = "{validation.usuario.nome.size}")
    private String nome;

    @NotBlank(message = "{validation.geral.notblank}")
    @Email(message = "{validation.usuario.email.format}")
    private String email;

    @NotBlank(message = "{validation.geral.notblank}")
    @Size(min = 6, message = "{validation.usuario.senha.size}")
    private String senha;
}
