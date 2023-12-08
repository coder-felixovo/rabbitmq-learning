package com.example.rabbitmqlearning.demo7;

import com.example.rabbitmqlearning.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 演示批量确认发布
 */
public class BatchProducer {
    public static String QUEUE_NAME = "confirm";
    public static int MESSAGE_COUNT = 10;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.getChannel();

        // 开启发布确认
        channel.confirmSelect();

        // 确定批量大小
        int batchSize = 5;

        // 未确认消息个数
        int noConfirmMesNum = 0;

        long begin = System.currentTimeMillis();

        // 发送消息
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        for (int i = 0; i < MESSAGE_COUNT;i ++) {
            String message = i + "";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            noConfirmMesNum++;
            // 每5个一批发布确认
            if (noConfirmMesNum == batchSize) {
                boolean flag = channel.waitForConfirms();
                if (flag) System.out.println("消息" + i + "与之前的" + batchSize + "发送成功");
                noConfirmMesNum = 0;
            }
        }
        if (noConfirmMesNum > 0) {
            channel.waitForConfirms();
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "条批量确认消息耗时：" + (end - begin) + "ms");
    }
}
