package com.example.interceptor;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static com.example.util.JsonUtil.jsonToPrettyString;
import static com.example.util.JsonUtil.mapToJsonNode;

@Component
public class RestControllerInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RestControllerInterceptor.class);

    private AtomicLong counter = new AtomicLong();

    private JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    @Value("${json.log.pretty}")
    private boolean prettyJson;

    //before the actual handler will be executed
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        request.setAttribute("request-number", counter.incrementAndGet());

        return true;
    }

    //after the handler is executed
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView modelAndView)
            throws Exception {

        // calculate execution time
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;

        // get request ID
        long requestNumber = (Long) request.getAttribute("request-number");

        traceRequest(request, requestNumber);
        traceResponse(response, requestNumber, executeTime);
    }

    private void traceRequest(HttpServletRequest request, Long counter) throws IOException {

        ObjectNode req = nodeFactory.objectNode();
        req.put("request-name", "server-incoming-request");
        req.put("request-id", counter);
        req.put("uri", request.getRequestURI());
        req.put("method", request.getMethod());
        req.set("headers", mapToJsonNode(getRequestHeadersInfo(request)));

        logger.debug(prettyJson ? jsonToPrettyString(req): req.toString());
    }

    private void traceResponse(HttpServletResponse response, Long counter, long executeTime) throws IOException {

        ObjectNode resp = nodeFactory.objectNode();
        resp.put("request-name", "server-outgoing-response");
        resp.put("request-id", counter);
        resp.put("status-code", response.getStatus());
        resp.set("headers", mapToJsonNode(getResponseHeadersInfo(response)));
        resp.put("execution-time-ms", executeTime);

        logger.debug(prettyJson ? jsonToPrettyString(resp): resp.toString());
    }

    private static Map<String, String> getRequestHeadersInfo(HttpServletRequest request) {

        Map<String, String> map = new HashMap<String, String>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    private static Map<String, String> getResponseHeadersInfo(HttpServletResponse response) {

        Map<String, String> map = new HashMap<String, String>();

        Collection<String> headerNames = response.getHeaderNames();
        for (String name : headerNames) {
            String key = name;
            String value = response.getHeader(key);
            map.put(key, value);
        }
        return map;
    }
}
