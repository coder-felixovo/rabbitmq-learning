package com.example.rabbitmqlearning.init;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 设置消息TTL+死信实现消息延迟
 */
@Configuration
public class MessageTTLDelay {
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    public static final String QUEUE_C = "QC";

    // 声明队列C，绑定死信交换机
    @Bean("queueC")
    public Queue queueC() {
        Map<String, Object> args = new HashMap<>();
        // 声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        // 声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", "YD");
        // 队列不声明TTL属性
        return QueueBuilder.durable(QUEUE_C).withArguments(args).build();
    }

    @Bean
    public Binding queueCBindingX(@Qualifier("queueC") Queue queueC, @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }
}
