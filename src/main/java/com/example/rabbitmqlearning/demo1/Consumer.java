package com.example.rabbitmqlearning.demo1;

import com.example.rabbitmqlearning.utils.RabbitMqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者示例
 */
public class Consumer {
    public static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
//        // 创建连接工厂
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("127.0.0.1");
//        factory.setUsername("guest");
//        factory.setUsername("guest");
//
//        // 创建连接
//        Connection connection = factory.newConnection();
//
//        // 创建通道
//        Channel channel = connection.createChannel();

        Channel channel = RabbitMqUtils.getChannel();

        /*
            消费者消费消息
            1. 消费的队列名称
            2. 消费成功后是否自动应答，true表示自动应答，false表示手动应答
            3. 消费者消费消息的回调，函数式接口
            4. 消费者取消消费的回调，函数式接口
         */

        // 消费消息回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("成功消费消息，内容为：" + new String(message.getBody()));
        };

        // 取消消费消息
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消费消息被中断");
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
