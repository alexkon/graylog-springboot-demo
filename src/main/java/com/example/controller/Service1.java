package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Service1 {

    @RequestMapping("/service1/status")
    public String heartBeat() {
        return "Service1 is alive";
    }
}
