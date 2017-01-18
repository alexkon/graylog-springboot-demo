package com.example.interceptor;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import java.util.stream.Collectors;

import static com.example.util.Util.jsonToPrettyString;

@PropertySource("classpath:application.properties")

public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RestTemplateInterceptor.class);
    private AtomicLong counter = new AtomicLong();
    private JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        long requestId = counter.incrementAndGet();

        traceRequest(request, body,requestId);

        // execute request
        long start = System.currentTimeMillis();
        ClientHttpResponse response = execution.execute(request, body);
        long end = System.currentTimeMillis();

        traceResponse(response, requestId, (end - start));

        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body, Long counter) throws IOException {

        ObjectNode reqWrapper = nodeFactory.objectNode();
        ObjectNode req = nodeFactory.objectNode();
        req.put("request_id", counter);
        req.put("uri", request.getURI().toString());
        req.put("method", request.getMethod().toString());
        req.put("headers", request.getHeaders().toString());
        req.put("request-body", new String(body, "UTF-8"));
        reqWrapper.set("client-outgoing-request", req);

        logger.debug(jsonToPrettyString(reqWrapper));
    }

    private void traceResponse(ClientHttpResponse response, Long counter, long execTime) throws IOException {

        String body = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"))
                .lines()
                .collect(Collectors.joining("\n"));

        ObjectNode respWrapper = nodeFactory.objectNode();
        ObjectNode resp = nodeFactory.objectNode();
        resp.put("request_id", counter);
        resp.put("status-code", response.getStatusCode().toString());
        resp.put("status-text", response.getStatusText());
        resp.put("headers", response.getHeaders().toString());
        resp.put("response-body", body);
        resp.put("execution-time-ms", execTime);
        respWrapper.set("client-incoming-response", resp);

        logger.debug(jsonToPrettyString(respWrapper));
    }

}
