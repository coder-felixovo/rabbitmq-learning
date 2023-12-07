package com.example.rabbitmqlearning.demo4;

import com.example.rabbitmqlearning.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 生产者，演示Direct模式
 */
public class ProducerFour {
    private static  final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        Map<String, String> messageHashMap = new HashMap<>();

        messageHashMap.put("info", "info message");
        messageHashMap.put("warn", "warn message");
        messageHashMap.put("error", "error message");
        messageHashMap.put("debug", "debug message");

        for (Map.Entry<String, String> mes : messageHashMap.entrySet()) {
            String routingKey = mes.getKey();
            String message = mes.getValue();
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
            System.out.println("生产者4，消息发送完毕：" + message);
        }
    }
}
