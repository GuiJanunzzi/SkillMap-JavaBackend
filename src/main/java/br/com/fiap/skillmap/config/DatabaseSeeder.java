package br.com.fiap.skillmap.config;

import br.com.fiap.skillmap.model.Categoria;
import br.com.fiap.skillmap.repository.CategoriaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
public class DatabaseSeeder implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;

    public DatabaseSeeder(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Verifica se já existem categorias para não duplicar
        if (categoriaRepository.count() == 0) {
            seedCategorias();
        }
    }

    private void seedCategorias() {
        List<Categoria> categoriasIniciais = List.of(
                criarCategoria("Energia Sustentável"),
                criarCategoria("Tecnologia da Informação"),
                criarCategoria("Soft Skills"),
                criarCategoria("Gestão de Projetos"),
                criarCategoria("Dados e Analytics"),
                criarCategoria("Engenharia Elétrica"),
                criarCategoria("Sustentabilidade Corporativa")
        );

        categoriaRepository.saveAll(categoriasIniciais);
        System.out.println("✅ DatabaseSeeder: Categorias iniciais carregadas com sucesso!");
    }

    private Categoria criarCategoria(String nome) {
        Categoria categoria = new Categoria();
        categoria.setNome(nome);
        return categoria;
    }
}