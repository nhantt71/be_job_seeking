package com.ttn.jobapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ttn.jobapp", "com.ttn.jobapp.Controllers"})
public class SeekingHiringAppApplication{

    public static void main(String[] args) {
        SpringApplication.run(SeekingHiringAppApplication.class, args);
    }
    

}
