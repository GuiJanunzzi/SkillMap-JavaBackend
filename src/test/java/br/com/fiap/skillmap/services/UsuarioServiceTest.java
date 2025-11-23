package br.com.fiap.skillmap.service;

import br.com.fiap.skillmap.dto.UsuarioCreateRequestDTO;
import br.com.fiap.skillmap.dto.UsuarioHabilidadeRequestDTO;
import br.com.fiap.skillmap.dto.UsuarioResponseDTO;
import br.com.fiap.skillmap.model.Categoria; // Importante
import br.com.fiap.skillmap.model.Habilidade;
import br.com.fiap.skillmap.model.Usuario;
import br.com.fiap.skillmap.repository.HabilidadeRepository;
import br.com.fiap.skillmap.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private HabilidadeRepository habilidadeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private org.springframework.ai.chat.client.ChatClient chatClient;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Deve criar usuário criptografando a senha")
    void deveCriarUsuarioComSenhaCriptografada() {
        // CENÁRIO
        UsuarioCreateRequestDTO dto = new UsuarioCreateRequestDTO();
        dto.setNome("Alice");
        dto.setEmail("alice@test.com");
        dto.setSenha("123456");

        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setId(1L);
        usuarioSalvo.setNome("Alice");
        usuarioSalvo.setEmail("alice@test.com");
        usuarioSalvo.setSenha("SENHA_CRIPTOGRAFADA");

        when(passwordEncoder.encode("123456")).thenReturn("SENHA_CRIPTOGRAFADA");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        // AÇÃO
        UsuarioResponseDTO resultado = usuarioService.criar(dto);

        // VERIFICAÇÃO
        assertEquals("SENHA_CRIPTOGRAFADA", usuarioSalvo.getSenha());
        verify(passwordEncoder, times(1)).encode("123456");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve adicionar habilidade ao usuário com sucesso")
    void deveAdicionarHabilidadeAoUsuario() {
        // CENÁRIO
        Long usuarioId = 1L;
        UsuarioHabilidadeRequestDTO dto = new UsuarioHabilidadeRequestDTO();
        dto.setHabilidadeId(10L);

        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        usuario.setHabilidadesPossuidas(new HashSet<>()); // Lista vazia

        // --- CORREÇÃO AQUI: Criando a Categoria Mockada ---
        Categoria categoriaMock = new Categoria();
        categoriaMock.setId(99L);
        categoriaMock.setNome("Tecnologia");

        Habilidade habilidade = new Habilidade();
        habilidade.setId(10L);
        habilidade.setNome("Java");
        habilidade.setCategoria(categoriaMock); // Associando a categoria para não dar NullPointer
        // --------------------------------------------------

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(habilidadeRepository.findById(10L)).thenReturn(Optional.of(habilidade));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        // AÇÃO
        UsuarioResponseDTO resultado = usuarioService.adicionarHabilidade(usuarioId, dto);

        // VERIFICAÇÃO
        assertEquals(1, usuario.getHabilidadesPossuidas().size());
        assertTrue(usuario.getHabilidadesPossuidas().contains(habilidade));
        verify(usuarioRepository, times(1)).save(usuario);
    }
}