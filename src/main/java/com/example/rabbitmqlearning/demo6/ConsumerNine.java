package com.example.rabbitmqlearning.demo6;

import com.example.rabbitmqlearning.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerNine {
    public static String QUEUE_NAME = "prefetch";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("消费者9收到消息时间较短");

        // 设置预取值为5
        channel.basicQos(5);

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            try {
                Thread.sleep(1000 * 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("消费者9，消费消息：" + new String(message.getBody()));
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消费者9，消费消息中断");
        };

        channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);
    }
}
