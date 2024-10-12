package com.ttn.jobapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ttn.jobapp", "com.ttn.jobapp.Controllers"})
public class SeekingHiringAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeekingHiringAppApplication.class, args);
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
