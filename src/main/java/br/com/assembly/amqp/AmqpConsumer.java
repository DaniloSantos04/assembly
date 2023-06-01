package br.com.assembly.amqp;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Component
public class AmqpConsumer {

    @RabbitListener(queues = "${spring.rabbitmq.consumer.queue-name}")
    public void receiveMessage(Message message, Channel channel) throws IOException {
        try {
            var messageString = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println("Message received: " + messageString);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            System.out.println("Error receiving messageÇ " + e.getMessage());
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}