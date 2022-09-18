package com.fwd.rocketmq.controller;

import com.fwd.rocketmq.mq.producer.TransactionProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DataController {

    private TransactionProducer producer;

    private DataController(TransactionProducer producer) {
        this.producer = producer;
    }

    @GetMapping("/getData")
    public String getData() {
        return "devtools热部署启动 (2022-09-18).";
    }

    @GetMapping("/sendMessage/{message}")
    public Object sendMessage(@PathVariable String message) {
        String status = producer.sendMessage(message + ":" + new Date());
        Map<String, String> map = new HashMap<>();
        map.put("status", status);
        map.put("message", message);
        return map;
    }

}
