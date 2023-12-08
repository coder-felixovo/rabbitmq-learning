package com.example.rabbitmqlearning.demo7;

import com.example.rabbitmqlearning.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

public class AsyncProducer {
    public static String QUEUE_NAME = "confirm";
    public static int MESSAGE_COUNT = 10;

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        channel.confirmSelect();

        long begin = System.currentTimeMillis();

        // 创建一个线程安全有序的哈希表，用于存放消息序号与内容
        ConcurrentSkipListMap<Long, String> concurrentSkipListMap = new ConcurrentSkipListMap<>();

        // 消息确认成功回调函数，第一个参数表示消息标志，第二个参数表示是否为批量确认
        ConfirmCallback confirmCallback = (long deliveryTag, boolean multiple) -> {
            if (multiple) {
                ConcurrentNavigableMap<Long, String> confirmed = concurrentSkipListMap.headMap(deliveryTag);
                confirmed.clear();
            } else {
                concurrentSkipListMap.remove(deliveryTag);
            }
            System.out.println("消息：" + deliveryTag + "已确认发布");
        };

        // 消息确认失败回调函数，第一个参数表示消息标志，第二个参数表示是否为批量确认
        ConfirmCallback nackCallback = (long deliveryTag, boolean multiple) -> {
            String message = concurrentSkipListMap.get(deliveryTag);
            System.out.println("未确认的消息为：" + message);
        };

        // 异步消息监听器
        channel.addConfirmListener(confirmCallback, nackCallback);

        // 发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "消息" + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            // 记录所有要发送的消息
            concurrentSkipListMap.put(channel.getNextPublishSeqNo(), message);
        }

        long end = System.currentTimeMillis();

        System.out.println("发布" + MESSAGE_COUNT + "条异步确认发布消息耗时：" + (end - begin) + "ms");
    }
}
