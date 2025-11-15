package br.com.fiap.skillmap.service;

import br.com.fiap.skillmap.dto.*;
import br.com.fiap.skillmap.exception.ResourceNotFoundException;
import br.com.fiap.skillmap.model.Habilidade;
import br.com.fiap.skillmap.model.Usuario;
import br.com.fiap.skillmap.repository.HabilidadeRepository;
import br.com.fiap.skillmap.repository.UsuarioRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final HabilidadeRepository habilidadeRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ChatClient chatClient;
    private final PasswordEncoder passwordEncoder;

    public static final String USUARIOS_NOVOS_QUEUE = "fila.skillmap.usuarios.novos";

    public UsuarioService(UsuarioRepository usuarioRepository,
                          HabilidadeRepository habilidadeRepository,
                          RabbitTemplate rabbitTemplate,
                          ChatClient chatClient,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.habilidadeRepository = habilidadeRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.chatClient = chatClient;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> listarTodos(Pageable pageable) {
        return usuarioRepository.findAll(pageable).map(UsuarioResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = findUsuarioById(id);
        return new UsuarioResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO criar(UsuarioCreateRequestDTO dto) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dto.getNome());
        novoUsuario.setEmail(dto.getEmail());
        // Criptografa a senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());
        novoUsuario.setSenha(senhaCriptografada);

        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);

        // Envia mensagem para a fila
        rabbitTemplate.convertAndSend(USUARIOS_NOVOS_QUEUE, usuarioSalvo.getEmail());

        return new UsuarioResponseDTO(usuarioSalvo);
    }

    @Transactional
    public UsuarioResponseDTO atualizar(Long id, UsuarioUpdateRequestDTO dto) {
        Usuario usuario = findUsuarioById(id);

        // Atualiza apenas os campos que vieram no DTO
        if (dto.getNome() != null) {
            usuario.setNome(dto.getNome());
        }
        if (dto.getEmail() != null) {
            usuario.setEmail(dto.getEmail());
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return new UsuarioResponseDTO(usuarioAtualizado);
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuario = findUsuarioById(id);
        usuarioRepository.delete(usuario);
    }

    // Gerenciamento de Habilidades
    @Transactional
    public UsuarioResponseDTO adicionarHabilidade(Long usuarioId, UsuarioHabilidadeRequestDTO dto) {
        Usuario usuario = findUsuarioById(usuarioId);
        Habilidade habilidade = findHabilidadeById(dto.getHabilidadeId());

        usuario.getHabilidadesPossuidas().add(habilidade);
        usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO removerHabilidade(Long usuarioId, Long habilidadeId) {
        Usuario usuario = findUsuarioById(usuarioId);
        Habilidade habilidade = findHabilidadeById(habilidadeId);

        usuario.getHabilidadesPossuidas().remove(habilidade);
        usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(usuario);
    }

    // Gerenciamento de Metas
    @Transactional
    public UsuarioResponseDTO adicionarMeta(Long usuarioId, UsuarioHabilidadeRequestDTO dto) {
        Usuario usuario = findUsuarioById(usuarioId);
        Habilidade habilidadeMeta = findHabilidadeById(dto.getHabilidadeId());

        usuario.getMetas().add(habilidadeMeta);
        usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO removerMeta(Long usuarioId, Long habilidadeId) {
        Usuario usuario = findUsuarioById(usuarioId);
        Habilidade habilidadeMeta = findHabilidadeById(habilidadeId);

        usuario.getMetas().remove(habilidadeMeta);
        usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(usuario);
    }

    // Spring AI

    public String gerarConselhoCarreira(Long usuarioId) {
        UsuarioResponseDTO dto = buscarPorId(usuarioId); // Reutiliza nosso método

        String prompt = String.format("""
            Atue como um Conselheiro de Carreira de T.I.
            Um profissional me informou seus dados:
            - Habilidades Atuais: %s
            - Habilidades Desejadas (Metas): %s

            Com base nisso, me dê um conselho em um único parágrafo
            sobre qual tecnologia ou soft skill ele deveria focar
            para conectar suas habilidades atuais com suas metas.
            """,
                dto.getHabilidadesPossuidas().stream().map(HabilidadeResponseDTO::getNome).toList(),
                dto.getMetas().stream().map(HabilidadeResponseDTO::getNome).toList()
        );

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }


    private Usuario findUsuarioById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado. ID: " + id));
    }

    private Habilidade findHabilidadeById(Long id) {
        return habilidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Habilidade não encontrada. ID: " + id));
    }
}