package br.com.fiap.skillmap.controller;

import br.com.fiap.skillmap.dto.CategoriaRequestDTO;
import br.com.fiap.skillmap.dto.CategoriaResponseDTO;
import br.com.fiap.skillmap.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // Lista todas as categorias de forma paginada.
    @GetMapping
    public ResponseEntity<Page<CategoriaResponseDTO>> listarTodos(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        Page<CategoriaResponseDTO> categorias = categoriaService.listarTodas(pageable);
        return ResponseEntity.ok(categorias);
    }

    // Busca uma categoria pelo seu ID.
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(@PathVariable Long id) {
        CategoriaResponseDTO categoria = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(categoria);
    }


    // Adiciona uma nova categoria.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Retorna 201 Created
    public CategoriaResponseDTO criar(@RequestBody @Valid CategoriaRequestDTO dto) {
        return categoriaService.criar(dto);
    }

    // Atualiza uma categoria existente.
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid CategoriaRequestDTO dto) {
        CategoriaResponseDTO categoriaAtualizada = categoriaService.atualizar(id, dto);
        return ResponseEntity.ok(categoriaAtualizada);
    }

    // Deleta uma categoria.
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna 204 No Content
    public void deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
    }
}