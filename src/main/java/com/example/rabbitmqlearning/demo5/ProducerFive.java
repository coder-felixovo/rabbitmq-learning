package com.example.rabbitmqlearning.demo5;

import com.example.rabbitmqlearning.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 生产者，演示Topic模式
 */
public class ProducerFive {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建channel
        Channel channel = RabbitMqUtils.getChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        // 发送消息
        Map<String, String> messageHashMap = new HashMap<>();

        messageHashMap.put("quick.orange.rabbit", "被队列7和8接收到");
        messageHashMap.put("lazy.orange.elephant", "被队列7和8接收到");
        messageHashMap.put("quick.orange.fox", "被队列7接收到");
        messageHashMap.put("lazy.brown.fox", "被队列8接收到");
        messageHashMap.put("lazy.pink.rabbit", "虽然满足两个绑定，但只被队列8接收一次");
        messageHashMap.put("quick.brown.fox", "不匹配任何绑定，不会被任何队列接收，消息被丢弃");
        messageHashMap.put("quick.orange.male.rabbit", "不匹配任何绑定，消息被丢弃");
        messageHashMap.put("lazy.orange.male.rabbit", "被队列8接收");

        for (Map.Entry<String, String> mes : messageHashMap.entrySet()) {
            String message = mes.getValue();
            String routingKey = mes.getKey();
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
            System.out.println("消息已发送：" + message);
        }
    }
}
