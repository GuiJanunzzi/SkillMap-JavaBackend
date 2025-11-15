package br.com.fiap.skillmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SkillmapApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkillmapApplication.class, args);
    }

}
