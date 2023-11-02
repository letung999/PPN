package com.ppn.ppn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class PpnApplication {
    public static void main(String[] args) {
        SpringApplication.run(PpnApplication.class, args);
    }

}
