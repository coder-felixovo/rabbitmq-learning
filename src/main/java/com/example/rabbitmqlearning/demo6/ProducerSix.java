package com.example.rabbitmqlearning.demo6;

import com.example.rabbitmqlearning.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProducerSix {
    public static String QUEUE_NAME = "prefetch";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        for (int i = 0; i < 10; i++) {
            String message = i + "";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("生产者6，发送消息：" + message);
        }
    }
}
