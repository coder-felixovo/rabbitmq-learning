package com.example.rabbitmqlearning.demo7;

import com.example.rabbitmqlearning.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 演示单个确认发布
 */
public class SingleProducer {
   public static String QUEUE_NAME = "confirm";
   public static int MESSAGE_COUNT = 10;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.getChannel();

        // 开启发布确认
        channel.confirmSelect();

        long begin = System.currentTimeMillis();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

            // 单条消息发送完马上确认
            boolean flag = channel.waitForConfirms();

            // 服务端返回false或超时，生产者可以重新发送消息
            if (flag) System.out.println("消息" + i + "发送成功");
        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "条单独确认消息耗时：" + (end - begin) + "ms");
    }
}
