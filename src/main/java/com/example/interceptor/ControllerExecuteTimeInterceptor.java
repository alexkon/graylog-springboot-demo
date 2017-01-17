package com.example.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class ControllerExecuteTimeInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ControllerExecuteTimeInterceptor.class);

    private AtomicLong counter = new AtomicLong();

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

        logger.debug(counter + " - ===========================request begin================================================");
        logger.debug(counter + " - URI         : {}", request.getRequestURI());
        logger.debug(counter + " - Method      : {}", request.getMethod());
        logger.debug(counter + " - Headers     : {}", getRequestHeadersInfo(request));
        logger.debug(counter + " -==========================request end================================================");
    }

    private void traceResponse(HttpServletResponse response, Long counter, Long executeTime) throws IOException {
        logger.debug(counter + " -============================response begin==========================================");
        logger.debug(counter + " - Status code    : {}", response.getStatus());
        logger.debug(counter + " - Headers        : {}", getResponseHeadersInfo(response));
        logger.debug(counter + " - Execution time : {} ms", executeTime);
        logger.debug(counter + " -=======================response end=================================================\n\n");
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
