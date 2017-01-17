package com.example.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicLong;

@PropertySource("classpath:application.properties")

public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RestTemplateInterceptor.class);
    private AtomicLong counter = new AtomicLong();

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        long countGet = counter.incrementAndGet();
        traceRequest(request, body,countGet);
        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(response, countGet);
        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body, Long counter) throws IOException {

        logger.debug(counter + " - ===========================request begin================================================");
        logger.debug(counter + " - URI         : {}", request.getURI());
        logger.debug(counter + " - Method      : {}", request.getMethod());
        logger.debug(counter + " - Headers     : {}", request.getHeaders());
        logger.debug(counter + " - Request body: {}", new String(body, "UTF-8"));
        logger.debug(counter + " -==========================request end================================================");
    }

    private void traceResponse(ClientHttpResponse response, Long counter) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
        String line = bufferedReader.readLine();
        while (line != null) {
            inputStringBuilder.append(line);
            line = bufferedReader.readLine();
            if (line != null) inputStringBuilder.append('\n');
        }
        logger.debug(counter + " -============================response begin==========================================");
        logger.debug(counter + " - Status code  : {}", response.getStatusCode());
        logger.debug(counter + " - Status text  : {}", response.getStatusText());
        logger.debug(counter + " - Headers      : {}", response.getHeaders());
        logger.debug(counter + " - Response body: {}", inputStringBuilder.toString());
        logger.debug(counter + " -=======================response end=================================================\n\n");
    }

}
