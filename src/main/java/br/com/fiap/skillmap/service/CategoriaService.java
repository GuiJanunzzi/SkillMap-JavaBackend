package br.com.fiap.skillmap.service;

import br.com.fiap.skillmap.dto.CategoriaRequestDTO;
import br.com.fiap.skillmap.dto.CategoriaResponseDTO;
import br.com.fiap.skillmap.exception.ResourceInUseException;
import br.com.fiap.skillmap.exception.ResourceNotFoundException;
import br.com.fiap.skillmap.model.Categoria;
import br.com.fiap.skillmap.repository.CategoriaRepository;
import br.com.fiap.skillmap.repository.HabilidadeRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final HabilidadeRepository habilidadeRepository;
    private final MessageService messageService;

    public CategoriaService(CategoriaRepository categoriaRepository,
                            HabilidadeRepository habilidadeRepository,
                            MessageService messageService) {
        this.categoriaRepository = categoriaRepository;
        this.habilidadeRepository = habilidadeRepository;
        this.messageService = messageService;
    }

    // 'key' para o cache funcionar com paginação
    @Cacheable(value = "categorias", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    @Transactional(readOnly = true)
    public Page<CategoriaResponseDTO> listarTodas(Pageable pageable) {
        return categoriaRepository.findAll(pageable)
                .map(CategoriaResponseDTO::new); // Page.map já faz a conversão
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarPorId(Long id) {
        Categoria categoria = findCategoriaById(id);
        return new CategoriaResponseDTO(categoria);
    }

    @CacheEvict(value = "categorias", allEntries = true)
    @Transactional
    public CategoriaResponseDTO criar(CategoriaRequestDTO dto) {
        Categoria novaCategoria = new Categoria();
        novaCategoria.setNome(dto.getNome());

        Categoria categoriaSalva = categoriaRepository.save(novaCategoria);
        return new CategoriaResponseDTO(categoriaSalva);
    }

    @CacheEvict(value = "categorias", allEntries = true)
    @Transactional
    public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = findCategoriaById(id); // Busca o existente
        categoria.setNome(dto.getNome()); // Atualiza o nome

        Categoria categoriaAtualizada = categoriaRepository.save(categoria);
        return new CategoriaResponseDTO(categoriaAtualizada);
    }

    @CacheEvict(value = "categorias", allEntries = true)
    @Transactional
    public void deletar(Long id) {
        Categoria categoria = findCategoriaById(id);

        if (habilidadeRepository.existsByCategoriaId(id)) {
            throw new ResourceInUseException(
                    messageService.get("categoria.inuse", id)
            );
        }

        categoriaRepository.delete(categoria);
    }

    private Categoria findCategoriaById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.get("categoria.notfound", id)
                ));
    }
}