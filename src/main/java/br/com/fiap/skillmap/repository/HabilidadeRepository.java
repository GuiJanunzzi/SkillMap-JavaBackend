package br.com.fiap.skillmap.repository;

import br.com.fiap.skillmap.model.Habilidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HabilidadeRepository extends JpaRepository<Habilidade, Long> {

    // Verifica se existe alguma Habilidade ligada a esta Categoria
    boolean existsByCategoriaId(Long categoriaId);

}