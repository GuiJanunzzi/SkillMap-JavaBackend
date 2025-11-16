package br.com.fiap.skillmap.controller;

import br.com.fiap.skillmap.dto.UsuarioHabilidadeRequestDTO;
import br.com.fiap.skillmap.dto.UsuarioResponseDTO;
import br.com.fiap.skillmap.dto.UsuarioUpdateRequestDTO;
import br.com.fiap.skillmap.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // CRUD BÁSICO DE USUARIO

    // Lista todos os usuarios de forma paginada
    @GetMapping
    public ResponseEntity<Page<UsuarioResponseDTO>> listarTodos(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        Page<UsuarioResponseDTO> usuarios = usuarioService.listarTodos(pageable);
        return ResponseEntity.ok(usuarios);
    }

    // Busca um usuario pelo seu ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        UsuarioResponseDTO usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    // Atualiza um usuário (nome ou email)
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioUpdateRequestDTO dto) {
        UsuarioResponseDTO usuarioAtualizado = usuarioService.atualizar(id, dto);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    // Deleta um usuario
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna 204 No Content
    public void deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
    }

    // GERENCIAMENTO DE HABILIDADES (SUB-RECURSO)

    // Adiciona uma habilidade a um usuario
    @PostMapping("/{id}/habilidades")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDTO adicionarHabilidade(
            @PathVariable Long id,
            @RequestBody @Valid UsuarioHabilidadeRequestDTO dto) {

        return usuarioService.adicionarHabilidade(id, dto);
    }

    //Remove uma habilidade de um usuario
    @DeleteMapping("/{id}/habilidades/{habilidadeId}")
    public UsuarioResponseDTO removerHabilidade(
            @PathVariable Long id,
            @PathVariable Long habilidadeId) {

        return usuarioService.removerHabilidade(id, habilidadeId);
    }

    // GERENCIAMENTO DE METAS (SUB-RECURSO)

    // Adiciona uma meta a um usuario
    @PostMapping("/{id}/metas")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDTO adicionarMeta(
            @PathVariable Long id,
            @RequestBody @Valid UsuarioHabilidadeRequestDTO dto) {

        return usuarioService.adicionarMeta(id, dto);
    }

    // Remove uma meta de um usuario
    @DeleteMapping("/{id}/metas/{metaId}")
    public UsuarioResponseDTO removerMeta(
            @PathVariable Long id,
            @PathVariable Long metaId) {

        return usuarioService.removerMeta(id, metaId);
    }


    // ENDPOINT DE SPRING AI

    // Endpoint do Spring AI que gera um conselho para o usuario
    @GetMapping("/{id}/conselho-carreira")
    public ResponseEntity<Map<String, String>> gerarConselho(@PathVariable Long id) {
        String conselho = usuarioService.gerarConselhoCarreira(id);

        // Retorna um JSON simples: {"conselho": "..."}
        return ResponseEntity.ok(Map.of("conselho", conselho));
    }
}