package com.example.javaproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.example.javaproject.entity")
@EnableJpaRepositories("com.example.javaproject.repository")
public class JavaProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaProjectApplication.class, args);
    }

}
