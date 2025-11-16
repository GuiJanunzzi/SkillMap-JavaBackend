package br.com.fiap.skillmap.controller;

import br.com.fiap.skillmap.dto.HabilidadeCreateRequestDTO;
import br.com.fiap.skillmap.dto.HabilidadeResponseDTO;
import br.com.fiap.skillmap.dto.HabilidadeUpdateRequestDTO;
import br.com.fiap.skillmap.service.HabilidadeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/habilidades")
public class HabilidadeController {

    private final HabilidadeService habilidadeService;

    public HabilidadeController(HabilidadeService habilidadeService) {
        this.habilidadeService = habilidadeService;
    }

    // Lista todas as habilidades de forma paginada.
    @GetMapping
    public ResponseEntity<Page<HabilidadeResponseDTO>> listarTodas(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        Page<HabilidadeResponseDTO> habilidades = habilidadeService.listarTodas(pageable);
        return ResponseEntity.ok(habilidades);
    }

    // Busca uma habilidade pelo seu ID.
    @GetMapping("/{id}")
    public ResponseEntity<HabilidadeResponseDTO> buscarPorId(@PathVariable Long id) {
        HabilidadeResponseDTO habilidade = habilidadeService.buscarPorId(id);
        return ResponseEntity.ok(habilidade);
    }

     // Adiciona uma nova habilidade.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HabilidadeResponseDTO criar(@RequestBody @Valid HabilidadeCreateRequestDTO dto) {
        return habilidadeService.criar(dto);
    }

    // Atualiza uma habilidade existente.
    @PutMapping("/{id}")
    public ResponseEntity<HabilidadeResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid HabilidadeUpdateRequestDTO dto) {
        HabilidadeResponseDTO habilidadeAtualizada = habilidadeService.atualizar(id, dto);
        return ResponseEntity.ok(habilidadeAtualizada);
    }

    // Deleta uma habilidade (se não estiver em uso).
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        // se a habilidade estiver em uso (retornando 409 Conflict)
        habilidadeService.deletar(id);
    }
}