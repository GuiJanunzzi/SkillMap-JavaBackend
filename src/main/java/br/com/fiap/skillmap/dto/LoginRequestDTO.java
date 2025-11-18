package br.com.fiap.skillmap.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "{validation.geral.notblank}")
    @Email(message = "{validation.usuario.email.format}")
    private String email;

    @NotBlank(message = "{validation.geral.notblank}")
    private String senha;
}