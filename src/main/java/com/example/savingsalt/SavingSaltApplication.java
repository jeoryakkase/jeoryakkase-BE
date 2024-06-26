package com.example.savingsalt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SavingSaltApplication {

    public static void main(String[] args) {
        SpringApplication.run(SavingSaltApplication.class, args);
    }
}
