package com.example.rabbitmqlearning.demo1;

import com.example.rabbitmqlearning.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者示例
 */
public class Producer {
    public static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
//        // 创建连接工厂
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("127.0.0.1");
//        factory.setUsername("guest");
//        factory.setPassword("guest");
//
//        // 创建连接
//        Connection connection = factory.newConnection();
//
//        // 创建通道
//        Channel channel = connection.createChannel();

        Channel channel = RabbitMqUtils.getChannel();

        /*
            创建队列 参数释义
            1. 队列名称
            2. 队列中的消息是否持久化，默认false，表示消息存储在内存中
            3. 该队列是否仅供一个消费者消费，true表示可以多个消费者消费
            4. 表示最后一个消费者断开连接后，该队列是否自动删除，true表示自动删除
            5. 其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        /*
            发送消息 参数释义
            1. 交换机，空串代表默认交换机
            2. 路由
            3. 其他参数
            4. 发送消息的消息体
         */
        String message = "Hello, rabbitmq";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("消息发送完毕");
    }
}
