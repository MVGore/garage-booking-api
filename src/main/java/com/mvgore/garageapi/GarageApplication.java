package com.mvgore.garageapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GarageApplication {

    public static void main(String[] args) {
        SpringApplication.run(GarageApplication.class, args);
        System.out.println("Garage Booking API is running...");
    }
}

