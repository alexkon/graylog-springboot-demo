package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Service2 {

    @RequestMapping("/service2/status")
    public String heartBeat() {
        return "Service2 is alive";
    }
}
