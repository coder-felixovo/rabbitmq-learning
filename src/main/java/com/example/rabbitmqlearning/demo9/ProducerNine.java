package com.example.rabbitmqlearning.demo9;

import com.example.rabbitmqlearning.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 演示队列达到最大长度
 * 先启动消费者C创建相应队列，然后关闭。再启动生产者9发送消息，最后启动消费者D。
 * 结果：有4条消息进入死信队列被消费者D消费
 */
public class ProducerNine {
    private static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);

        for (int i = 0; i < 10; i++) {
            String message = i + "";
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null, message.getBytes());
            System.out.println("生产者9，发送消息：" + message);
        }
    }
}
