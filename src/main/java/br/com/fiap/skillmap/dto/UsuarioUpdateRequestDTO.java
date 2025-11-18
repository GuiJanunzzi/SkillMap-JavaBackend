package br.com.fiap.skillmap.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioUpdateRequestDTO {

    @Size(min = 3, message = "{validation.usuario.nome.size}")
    private String nome;

    @Email(message = "{validation.usuario.email.format}")
    private String email;
}
