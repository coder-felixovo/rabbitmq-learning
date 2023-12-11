package com.example.rabbitmqlearning.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Slf4j
public class SendMessageController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("当前时间：{}，发送一条信息给两个TTL队列：{}", new Date(), message);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自ttl=10s的队列：" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自ttl=40s的队列：" + message);
    }

    @GetMapping("sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message, @PathVariable String ttlTime) {
        rabbitTemplate.convertAndSend("X", "XC", message, correlationData -> {
            correlationData.getMessageProperties().setExpiration(ttlTime);
            return correlationData;
        });
        log.info("当前时间：{}，发送一条时长{}毫秒TTL信息给队列C：{}", new Date(), ttlTime, message);
    }
}
