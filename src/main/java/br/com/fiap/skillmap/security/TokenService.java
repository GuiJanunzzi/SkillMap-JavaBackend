package br.com.fiap.skillmap.security;

import br.com.fiap.skillmap.model.Usuario;
import br.com.fiap.skillmap.service.MessageService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class TokenService {

    // Injeta a chave secreta do application.properties
    @Value("${api.security.token.secret}")
    private String secret;

    private final String ISSUER = "API SkillMap";
    private final MessageService messageService;

    public TokenService(MessageService messageService) {
        this.messageService = messageService;
    }


    // Gera um novo Token JWT para um usuário. (Sintaxe JJWT correta)
    public String gerarToken(Usuario usuario) {
        // Converte a String da chave secreta em uma chave criptográfica
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        // Define a data de expiração
        Date expirationDate = Date.from(gerarDataDeExpiracao());
        Date issuedAtDate = Date.from(Instant.now());

        try {
            String token = Jwts.builder()
                    .issuer(ISSUER) // Define o emissor
                    .subject(usuario.getEmail()) // Define o "dono" (email)
                    .issuedAt(issuedAtDate) // Data de emissão
                    .expiration(expirationDate) // Data de validade
                    .signWith(secretKey) // Assina o token com a chave
                    .compact();

            return token;

        } catch (Exception e) {
            throw new RuntimeException(messageService.get("token.error.generation"), e);
        }
    }


    // Valida um token JWT e retorna o "dono" (subject/email) se for válido.
    public String validarToken(String tokenJWT) {
        // Converte a chave
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        try {
            // Cria o parser (validador)
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey) // Define a chave para verificar a assinatura
                    .requireIssuer(ISSUER) // Verifica se o emissor bate
                    .build()
                    .parseSignedClaims(tokenJWT) // Tenta validar o token
                    .getPayload();

            // Retorna o "dono" (email)
            return claims.getSubject();

        } catch (Exception e) {
            // Se o token for inválido (expirado, assinatura errada), retorna nulo
            return null;
        }
    }

    // Metodo helper para definir a validade do token (ex: 2 horas).
    private Instant gerarDataDeExpiracao() {
        // Pega o tempo atual em UTC e soma 2 horas
        return Instant.now().plus(2, ChronoUnit.HOURS);
    }
}