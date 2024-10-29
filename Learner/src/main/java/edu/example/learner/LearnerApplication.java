package edu.example.learner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
public class LearnerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnerApplication.class, args);
    }
}
