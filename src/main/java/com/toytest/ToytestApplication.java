package com.toytest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ToytestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToytestApplication.class, args);
    }

}
