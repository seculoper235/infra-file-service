package com.example.infrafileservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class InfraFileServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfraFileServiceApplication.class, args);
    }

}
