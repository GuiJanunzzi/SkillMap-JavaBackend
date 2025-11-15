package br.com.fiap.skillmap.repository;

import br.com.fiap.skillmap.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    // Verifica se algum usuario tem uma habilidade na sua lista de 'habilidadesPossuidas'
    boolean existsByHabilidadesPossuidasId(Long habilidadeId);

    // Verifica se algum usuario tem esta habilidade na sua lista de 'metas'
    boolean existsByMetasId(Long habilidadeId);

}