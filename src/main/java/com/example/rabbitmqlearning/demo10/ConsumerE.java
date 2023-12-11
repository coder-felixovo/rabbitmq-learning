package com.example.rabbitmqlearning.demo10;

import com.example.rabbitmqlearning.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class ConsumerE {
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

        // 声明队列
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        // 绑定队列和交换机，设置路由
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        // 消费消息
        System.out.println("消费者E等待接收普通队列的消息......");
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String mes = new String(message.getBody(), "UTF-8");
            if (mes.equals("5")) {
                System.out.println("消费者E接收到普通队列的消息：" + mes + "，并拒收该消息");
                // 第二个参数requeue设置为false，表示拒绝重新入队，如果该队列配置了死信交换机，则发送到死信队列中。
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("消费者E接收到普通队列的消息：" + mes);
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        };
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消费者E消费消息中断");
        };

        channel.basicConsume(NORMAL_QUEUE, deliverCallback, cancelCallback);
    }
}
