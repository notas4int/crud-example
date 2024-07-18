package org.artem.projects.proteincrudexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class ProteinCrudExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProteinCrudExampleApplication.class, args);
    }
}
