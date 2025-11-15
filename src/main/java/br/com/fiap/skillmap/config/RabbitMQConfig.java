package br.com.fiap.skillmap.config;

import br.com.fiap.skillmap.service.UsuarioService;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue usuariosNovosQueue() {
        return new Queue(UsuarioService.USUARIOS_NOVOS_QUEUE, true);
    }
}