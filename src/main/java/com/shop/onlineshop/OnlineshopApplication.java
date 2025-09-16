package com.shop.onlineshop;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

@SpringBootApplication
public class OnlineshopApplication {

	public static void main(String[] args) {
        SpringApplication app = new SpringApplication(OnlineshopApplication.class);
        app.run(args);
	}

}
