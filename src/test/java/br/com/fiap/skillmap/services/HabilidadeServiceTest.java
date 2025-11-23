package br.com.fiap.skillmap.service;

import br.com.fiap.skillmap.dto.HabilidadeCreateRequestDTO;
import br.com.fiap.skillmap.dto.HabilidadeResponseDTO;
import br.com.fiap.skillmap.exception.ResourceInUseException;
import br.com.fiap.skillmap.exception.ResourceNotFoundException;
import br.com.fiap.skillmap.model.Categoria;
import br.com.fiap.skillmap.model.Habilidade;
import br.com.fiap.skillmap.repository.CategoriaRepository;
import br.com.fiap.skillmap.repository.HabilidadeRepository;
import br.com.fiap.skillmap.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Inicializa o Mockito sem subir o Spring inteiro (rápido)
class HabilidadeServiceTest {

    @Mock // Cria uma versão "falsa" do repositório
    private HabilidadeRepository habilidadeRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private MessageService messageService;

    @InjectMocks // Injeta os Mocks acima dentro do Service real
    private HabilidadeService habilidadeService;

    @Test
    @DisplayName("Deve criar uma habilidade com sucesso")
    void deveCriarHabilidadeComSucesso() {
        // CENÁRIO (Arrange)
        Long categoriaId = 1L;
        HabilidadeCreateRequestDTO dto = new HabilidadeCreateRequestDTO();
        dto.setNome("Java");
        dto.setDescricao("Linguagem OO");
        dto.setCategoriaId(categoriaId);

        Categoria categoriaMock = new Categoria();
        categoriaMock.setId(categoriaId);

        Habilidade habilidadeSalva = new Habilidade();
        habilidadeSalva.setId(10L);
        habilidadeSalva.setNome("Java");
        habilidadeSalva.setCategoria(categoriaMock);

        // Ensinando os mocks o que fazer
        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoriaMock));
        when(habilidadeRepository.save(any(Habilidade.class))).thenReturn(habilidadeSalva);

        // AÇÃO (Act)
        HabilidadeResponseDTO resultado = habilidadeService.criar(dto);

        // VERIFICAÇÃO (Assert)
        assertNotNull(resultado);
        assertEquals("Java", resultado.getNome());
        assertEquals(10L, resultado.getId());

        // Verifica se o save foi chamado uma vez
        verify(habilidadeRepository, times(1)).save(any(Habilidade.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar habilidade em uso")
    void deveLancarExcecaoAoDeletarHabilidadeEmUso() {
        // CENÁRIO
        Long idHabilidade = 1L;
        Habilidade habilidadeMock = new Habilidade();
        habilidadeMock.setId(idHabilidade);

        when(habilidadeRepository.findById(idHabilidade)).thenReturn(Optional.of(habilidadeMock));
        // Simula que a habilidade está sendo usada por um usuário
        when(usuarioRepository.existsByHabilidadesPossuidasId(idHabilidade)).thenReturn(true);
        // Simula a mensagem de erro
        when(messageService.get(anyString(), any())).thenReturn("Habilidade em uso");

        // AÇÃO E VERIFICAÇÃO
        assertThrows(ResourceInUseException.class, () -> {
            habilidadeService.deletar(idHabilidade);
        });

        // Garante que o método delete JAMAIS foi chamado
        verify(habilidadeRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando habilidade não for encontrada")
    void deveLancarExcecaoHabilidadeNaoEncontrada() {
        Long idInexistente = 99L;

        when(habilidadeRepository.findById(idInexistente)).thenReturn(Optional.empty());
        when(messageService.get(anyString(), any())).thenReturn("Não encontrado");

        assertThrows(ResourceNotFoundException.class, () -> {
            habilidadeService.buscarPorId(idInexistente);
        });
    }
}