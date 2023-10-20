package com.AppServer.Chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
public class ChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }
}

@RestController
@RequestMapping("/api")
public class ApiController {

    @PostMapping("/risposta")
    public String riceviParola(@RequestBody String parola) {
        String risposta = "Hai scritto: " + parola;
        return risposta;
    }
}