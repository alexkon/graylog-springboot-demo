package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
@Component
public class TaskSheduller implements SchedulingConfigurer {

    private final RestTemplate restTemplate;

    @Autowired
    public TaskSheduller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final Logger logger = LoggerFactory.getLogger(TaskSheduller.class);

    @Scheduled(initialDelay=1000, fixedDelay=5*1000) // каждые 5 сек
    public void heartBeatService1() {
        logger.debug("Started heartBeatService1 task");
        try {
            String result = restTemplate.getForObject("http://localhost:8080/service1/status", String.class);
            logger.info("Heart Beat Service1 result: {}", result);
        } catch (Exception exception) {
            logger.error("Heart Beat Service1 result: {}", exception.getMessage());
        }
        logger.debug("Finished heartBeatService1 task");
    }

    @Scheduled(initialDelay=1000, fixedDelay=5*1000) // каждые 5 сек
    public void heartBeatService2() {
        logger.debug("Started heartBeatService2 task");
        try {
            String result = restTemplate.getForObject("http://localhost:8080/service2/status", String.class);
            logger.info("Heart Beat Service2 result: {}", result);
        } catch (Exception exception) {
            logger.error("Heart Beat Service2 result: {}", exception.getMessage());
        }
        logger.debug("Finished heartBeatService2 task");
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegister) {
        taskRegister.setScheduler(taskExecutor());
    }

    @Bean(destroyMethod="shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(2);
    }
}
