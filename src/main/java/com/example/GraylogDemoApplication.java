package com.example;

import com.example.interceptor.RestTemplateInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
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
			logger.info("Application Started");

            long start = System.currentTimeMillis();
            IntStream.range(0,REQUEST_NUMBER).forEach(i -> logger.info("Log number {}",i));

            System.out.println(String.format("%d requests in %f seconds",REQUEST_NUMBER, (System.currentTimeMillis() - start)/1000.));
        });
	}

	@Bean
	RestTemplate restTemplate() {
        // create factory with timeouts
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setReadTimeout(30000);
		requestFactory.setConnectTimeout(30000);

        // create interceptors
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new RestTemplateInterceptor());

        // configure rest template
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(requestFactory));
        restTemplate.setInterceptors(interceptors);
		return restTemplate;
	}
}
