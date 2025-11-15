package br.com.fiap.skillmap.security;

import br.com.fiap.skillmap.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public SecurityFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Tenta recuperar o token da requisição
        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            // Se houver token faz a validação dele
            var emailUsuario = tokenService.validarToken(tokenJWT);

            if (emailUsuario != null) {
                // Se o token for válido busca o usuário no banco
                UserDetails usuario = usuarioRepository.findByEmail(emailUsuario)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado no token."));

                // Informa ao Spring que o usuario está autenticado
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continua a cadeia de filtros (permite a requisição seguir)
        filterChain.doFilter(request, response);
    }

    // Método helper para extrair o token do Header "Authorization"
    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}