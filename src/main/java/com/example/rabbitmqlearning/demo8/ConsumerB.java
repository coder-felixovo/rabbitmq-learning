package com.example.rabbitmqlearning.demo8;

import com.example.rabbitmqlearning.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerB {
    private static final String DEAD_EXCHANGE = "dead_exchange";
    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        System.out.println("消费者B等待接受死信队列的消息......");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("消费者B接收到死信队列的消息：" + message);
        };

        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消费者B消费消息中断");
        };

        channel.basicConsume(DEAD_QUEUE, deliverCallback, cancelCallback);
    }
}
