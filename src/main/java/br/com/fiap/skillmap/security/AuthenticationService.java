package br.com.fiap.skillmap.security;

import br.com.fiap.skillmap.repository.UsuarioRepository;
import br.com.fiap.skillmap.service.MessageService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final MessageService messageService;

    public AuthenticationService(UsuarioRepository usuarioRepository, MessageService messageService) {
        this.usuarioRepository = usuarioRepository;
        this.messageService = messageService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // O "username" para é o email
        // Busca o usuário no banco pelo email
        return usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        messageService.get("security.error.user.notfound", username)
                ));
    }
}