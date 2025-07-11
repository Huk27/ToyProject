package com.Jinhyy;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class ApiMain {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ApiMain.class, args);
    }
}