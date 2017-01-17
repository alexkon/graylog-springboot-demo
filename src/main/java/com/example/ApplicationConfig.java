package com.example;

import com.example.interceptor.ControllerExecuteTimeInterceptor;
import com.example.interceptor.RestTemplateInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ApplicationConfig extends WebMvcConfigurerAdapter {

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

    @Bean
    public ControllerExecuteTimeInterceptor getControllerInterceptor() {
        return new ControllerExecuteTimeInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getControllerInterceptor()).addPathPatterns("/**");
    }
}
