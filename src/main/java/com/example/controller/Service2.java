package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Random;

@RestController
public class Service2 {

    private final Random random = new Random();

    @RequestMapping("/service2/status")
    public String heartBeat(HttpServletResponse response) {
        if (random.nextInt(10) >= 9) {  // 10% errors
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
        return "Service2 is alive";
    }
}
