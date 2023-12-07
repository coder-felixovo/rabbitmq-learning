package com.example.rabbitmqlearning.demo5;

import com.example.rabbitmqlearning.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerSeven {
    private static final String EXCHANGE_NAME = "topic_logs";
    private static final String QUEUE_NAME = "Q7";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建channel
        Channel channel = RabbitMqUtils.getChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        // 创建Q7队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 绑定队列和交换机
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "*.orange.*");

        // 消费消息回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("消费者7，获得消息：" + new String(message.getBody()));
        };

        // 消费取消回调
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消费者7，取消消费");
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
