package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

@RestController
public class Service2 {

    private final Random random = new Random();

    @ResponseBody
    @RequestMapping(value = "/service2/status", produces = "application/json")
    public String heartBeat(HttpServletRequest request, HttpServletResponse response) {
        if (random.nextInt(10) % 4 == 0) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
        return "Service2 is alive";
    }
}
