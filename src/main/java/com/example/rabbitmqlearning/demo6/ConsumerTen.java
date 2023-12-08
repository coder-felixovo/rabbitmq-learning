package com.example.rabbitmqlearning.demo6;

import com.example.rabbitmqlearning.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerTen {
    public static String QUEUE_NAME = "prefetch";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("消费者10收到消息时间较长");

        // 设置预取值
        channel.basicQos(2);

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("消费者10，消费消息：" + new String(message.getBody()));
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消费者10，消费消息中断");
        };

        channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);
    }
}
