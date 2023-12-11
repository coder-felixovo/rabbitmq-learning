package com.example.rabbitmqlearning.demo10;

import com.example.rabbitmqlearning.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 代码与demo9中的ProducerNine保持一致
 * 演示消息被拒
 * 先启动ConsumerE，然后启动ConsumerF，最后启动ProducerTen
 * 结果：消费者E拒收了消息5，消息5进入死信队列，被消费者F消费
 */
public class ProducerTen {
    private static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);

        for (int i = 0; i < 10; i++) {
            String message = i + "";
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null, message.getBytes());
            System.out.println("生产者10，发送消息：" + message);
        }
    }
}
