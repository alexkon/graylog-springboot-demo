package com.example.interceptor;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static com.example.util.JsonUtil.jsonToPrettyString;
import static com.example.util.JsonUtil.mapToJsonNode;

@Component
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RestTemplateInterceptor.class);
    private AtomicLong counter = new AtomicLong();
    private JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    @Value("${json.log.pretty}")
    private boolean prettyJson;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        long requestId = counter.incrementAndGet();

        traceRequest(request, body,requestId);

        // execute request
        long start = System.currentTimeMillis();
        ClientHttpResponse response = execution.execute(request, body);
        long end = System.currentTimeMillis();

        traceResponse(response, requestId, request.getURI().toString(), (end - start));

        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body, long counter) throws IOException {

        ObjectNode req = nodeFactory.objectNode();
        req.put("request-name", "client-outgoing-request");
        req.put("request-id", counter);
        req.put("uri", request.getURI().toString());
        req.put("method", request.getMethod().toString());
        req.set("headers", mapToJsonNode(request.getHeaders().toSingleValueMap()));
        req.put("request-body", new String(body, "UTF-8"));

        logger.debug(prettyJson ? jsonToPrettyString(req): req.toString());
    }

    private void traceResponse(ClientHttpResponse response, long counter,  String uri, long execTime) throws IOException {

        String body = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"))
                .lines()
                .collect(Collectors.joining("\n"));

        ObjectNode resp = nodeFactory.objectNode();
        resp.put("response-name", "client-incoming-response");
        resp.put("uri", uri);
        resp.put("request-id", counter);
        resp.put("status-code", response.getStatusCode().toString());
        resp.put("status-text", response.getStatusText());
        resp.set("headers", mapToJsonNode(response.getHeaders().toSingleValueMap()));
        resp.put("response-body", body);
        resp.put("execution-time-ms", execTime);

        logger.debug(prettyJson ? jsonToPrettyString(resp): resp.toString());
    }

}
