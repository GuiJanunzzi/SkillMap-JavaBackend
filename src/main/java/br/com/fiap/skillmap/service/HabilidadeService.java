package br.com.fiap.skillmap.service;

import br.com.fiap.skillmap.dto.HabilidadeCreateRequestDTO;
import br.com.fiap.skillmap.dto.HabilidadeResponseDTO;
import br.com.fiap.skillmap.dto.HabilidadeUpdateRequestDTO;
import br.com.fiap.skillmap.exception.ResourceInUseException;
import br.com.fiap.skillmap.exception.ResourceNotFoundException;
import br.com.fiap.skillmap.model.Categoria;
import br.com.fiap.skillmap.model.Habilidade;
import br.com.fiap.skillmap.repository.CategoriaRepository;
import br.com.fiap.skillmap.repository.HabilidadeRepository;
import br.com.fiap.skillmap.repository.UsuarioRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HabilidadeService {

    private final HabilidadeRepository habilidadeRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public HabilidadeService(HabilidadeRepository habilidadeRepository, CategoriaRepository categoriaRepository, UsuarioRepository usuarioRepository) {
        this.habilidadeRepository = habilidadeRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // 'key' para o cache funcionar com paginação
    @Cacheable(value = "habilidades", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    @Transactional(readOnly = true)
    public Page<HabilidadeResponseDTO> listarTodas(Pageable pageable) {
        Page<Habilidade> habilidadesPage = habilidadeRepository.findAll(pageable);
        return habilidadesPage.map(HabilidadeResponseDTO::new); // Page.map já faz a conversão
    }

    @Cacheable(value = "habilidade", key = "#id") // Cache para busca individual
    @Transactional(readOnly = true)
    public HabilidadeResponseDTO buscarPorId(Long id) {
        Habilidade habilidade = findHabilidadeById(id);
        return new HabilidadeResponseDTO(habilidade);
    }

    @CacheEvict(value = {"habilidades", "habilidade"}, allEntries = true) // Limpa ambos os caches
    @Transactional
    public HabilidadeResponseDTO criar(HabilidadeCreateRequestDTO dto) {
        Categoria categoria = findCategoriaById(dto.getCategoriaId());

        Habilidade novaHabilidade = new Habilidade();
        novaHabilidade.setNome(dto.getNome());
        novaHabilidade.setDescricao(dto.getDescricao());
        novaHabilidade.setCategoria(categoria);

        Habilidade habilidadeSalva = habilidadeRepository.save(novaHabilidade);
        return new HabilidadeResponseDTO(habilidadeSalva);
    }

    @CacheEvict(value = {"habilidades", "habilidade"}, allEntries = true)
    @Transactional
    public HabilidadeResponseDTO atualizar(Long id, HabilidadeUpdateRequestDTO dto) {
        Habilidade habilidade = findHabilidadeById(id);

        // Só atualiza o que foi enviado
        if (dto.getNome() != null) {
            habilidade.setNome(dto.getNome());
        }
        if (dto.getDescricao() != null) {
            habilidade.setDescricao(dto.getDescricao());
        }
        if (dto.getCategoriaId() != null) {
            Categoria novaCategoria = findCategoriaById(dto.getCategoriaId());
            habilidade.setCategoria(novaCategoria);
        }

        Habilidade habilidadeAtualizada = habilidadeRepository.save(habilidade);
        return new HabilidadeResponseDTO(habilidadeAtualizada);
    }

    @CacheEvict(value = {"habilidades", "habilidade"}, allEntries = true)
    @Transactional
    public void deletar(Long id) {
        Habilidade habilidade = findHabilidadeById(id);

        // Verifica se a habilidade está sendo usada em alguma lista de usuário
        boolean emUso = usuarioRepository.existsByHabilidadesPossuidasId(id) ||
                usuarioRepository.existsByMetasId(id);

        if (emUso) {
            // Se estiver em uso, lança a exceção 409 CONFLICT
            throw new ResourceInUseException(
                    "Não é possível deletar a Habilidade (ID: " + id + ") pois ela está associada a um ou mais usuários (em habilidades ou metas)."
            );
        }

        // Se não estiver em uso deleta
        habilidadeRepository.delete(habilidade);
    }

    private Habilidade findHabilidadeById(Long id) {
        return habilidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habilidade não encontrada. ID: " + id));
    }

    private Categoria findCategoriaById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada para associar. ID: " + id));
    }
}