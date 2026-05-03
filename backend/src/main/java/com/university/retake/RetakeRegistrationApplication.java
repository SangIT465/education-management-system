package com.university.retake;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RetakeRegistrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(RetakeRegistrationApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("  GROUP 6 - RETAKE REGISTRATION API");
        System.out.println("  Server: http://localhost:8080/api");
        System.out.println("========================================\n");
    }
}
