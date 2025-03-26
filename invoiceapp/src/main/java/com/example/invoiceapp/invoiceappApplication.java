package com.example.invoiceapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class invoiceappApplication {

	public static void main(String[] args) {
		SpringApplication.run(invoiceappApplication.class, args);
		System.out.println("Server running on port 8081");
	}

}


