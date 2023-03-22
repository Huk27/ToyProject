package com.Jinhyy;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import reactor.tools.agent.ReactorDebugAgent;

@SpringBootApplication
public class ApiMain {
    public static void main(String[] args) {
        ReactorDebugAgent.init();
        ConfigurableApplicationContext context = SpringApplication.run(ApiMain.class, args);
    }
}