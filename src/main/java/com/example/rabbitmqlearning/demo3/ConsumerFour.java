package com.example.rabbitmqlearning.demo3;

import com.example.rabbitmqlearning.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerFour {
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        // 声明交换机
        channel.exchangeDeclare("logs", BuiltinExchangeType.FANOUT);

        // 声明临时队列
        String queueName = channel.queueDeclare().getQueue();

        // 将队列和交换机绑定
        channel.queueBind(queueName, "logs", "");

        // 消费消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("消费者4，消费消息：" + new String(message.getBody()));
        };

        // 取消消费消息
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消费者4， 消费消息中断");
        };

        channel.basicConsume(queueName, true, deliverCallback, cancelCallback);
    }
}
