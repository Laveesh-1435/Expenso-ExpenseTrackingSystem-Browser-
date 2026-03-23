package com.example.expense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class app {
    public static void main(String[] args) {
        // This line starts the web server and your entire application
        SpringApplication.run(app.class, args);
        System.out.println("🌐 Web Server Started! Go to http://localhost:9000 in your browser.");
    }
}