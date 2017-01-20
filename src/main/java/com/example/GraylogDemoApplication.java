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
		System.out.println("http://localhost:8080/service1/status");
        System.out.println("http://localhost:8080/service2/status");
	}

    @Bean
    CommandLineRunner runner() {
        return (args -> {
//            logger.trace("GraylogDemoApplication: test TRACE message");
//            logger.debug("GraylogDemoApplication: test DEBUG message");
//            logger.info("GraylogDemoApplication: test INFO message");
//            logger.warn("GraylogDemoApplication: test WARN message");
//            logger.error("GraylogDemoApplication: test ERROR message");
//            performanceTest();
        });
    }

    private static void performanceTest() {
        logger.info("Application Started");

        long start = System.currentTimeMillis();
        IntStream.range(0,REQUEST_NUMBER).forEach(i -> logger.info("Log number {}",i));

        System.out.println(String.format("%d requests in %f seconds",REQUEST_NUMBER, (System.currentTimeMillis() - start)/1000.));
    }
}
