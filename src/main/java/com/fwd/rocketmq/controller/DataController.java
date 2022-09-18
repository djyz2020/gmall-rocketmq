package com.fwd.rocketmq.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

    @GetMapping("/getData")
    public String getData() {
        return "devtools热部署启动.";
    }

}
