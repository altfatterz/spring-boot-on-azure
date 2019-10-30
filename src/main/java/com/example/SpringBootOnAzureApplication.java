package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringBootOnAzureApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootOnAzureApplication.class, args);
    }

    @GetMapping("/")
    public String greet() {
        return "Greetings from Azure Cloud, running as an App Service";
    }
}
