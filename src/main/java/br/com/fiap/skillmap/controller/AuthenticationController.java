package br.com.fiap.skillmap.controller;

import br.com.fiap.skillmap.dto.LoginRequestDTO;
import br.com.fiap.skillmap.dto.LoginResponseDTO;
import br.com.fiap.skillmap.dto.UsuarioCreateRequestDTO;
import br.com.fiap.skillmap.dto.UsuarioResponseDTO;
import br.com.fiap.skillmap.model.Usuario;
import br.com.fiap.skillmap.security.TokenService;
import br.com.fiap.skillmap.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioService usuarioService;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    TokenService tokenService,
                                    UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.usuarioService = usuarioService;
    }

    // Endpoint para registrar um novo usuário.
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED) // Retorna 201 Created
    public UsuarioResponseDTO register(@RequestBody @Valid UsuarioCreateRequestDTO dto) {
        // O UsuarioService já foi atualizado para criptografar a senha
        return usuarioService.criar(dto);
    }

    // Endpoint público para logar e obter um token JWT.
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        // Cria um token de autenticação com o email e senha
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha());

        // O Spring Security usa o AuthenticationManager para validar
        Authentication auth = authenticationManager.authenticate(usernamePassword);

        // Se a autenticação foi bem-sucedida, pega o 'Usuario'
        Usuario usuarioAutenticado = (Usuario) auth.getPrincipal();

        // Gera um token JWT para este usuário
        String token = tokenService.gerarToken(usuarioAutenticado);

        // Retorna o token para o cliente
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}