package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.IntStream;

@SpringBootApplication
public class GraylogDemoApplication {

    private static final int REQUEST_NUMBER = 1000;

	private static final Logger logger = LoggerFactory.getLogger(GraylogDemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(GraylogDemoApplication.class, args);
	}

	@Bean
	CommandLineRunner runner() {
		return (args -> {
			logger.info("Application Started");

            long start = System.currentTimeMillis();
            IntStream.range(0,REQUEST_NUMBER).forEach(i -> logger.info("Log number {}",i));

            System.out.println(String.format("%d requests in %f seconds",REQUEST_NUMBER, (System.currentTimeMillis() - start)/1000.));
        });
	}
}
