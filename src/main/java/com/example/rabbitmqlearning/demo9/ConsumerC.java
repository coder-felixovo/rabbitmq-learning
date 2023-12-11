package com.example.rabbitmqlearning.demo9;

import com.example.rabbitmqlearning.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class ConsumerC {
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    private static final String DEAD_EXCHANGE = "dead_exchange";
    private static final String NORMAL_QUEUE = "normal_queue";
    private static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 普通队列需要传递参数，设置死信交换机及其路由
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE); // 设置死信交换机
        arguments.put("x-dead-letter-routing-key", "lisi"); // 设置与死信交换机间的routingKey
        arguments.put("x-max-length", 6); // 设置正常队列长度为6

        // 声明队列
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        // 绑定队列和交换机，设置路由
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        // 消费消息
        System.out.println("消费者C等待接收普通队列的消息......");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("消费者C接收到普通队列的消息：" + new String(message.getBody()));
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消费者C消费消息中断");
        };

        channel.basicConsume(NORMAL_QUEUE, deliverCallback, cancelCallback);
    }
}
