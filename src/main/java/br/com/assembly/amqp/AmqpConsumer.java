package br.com.assembly.amqp;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AmqpConsumer {

    @RabbitListener(queues = "${spring.rabbitmq.consumer.queue-name}")
    public void receiveMessage(String mensagem) {
        System.out.println("Mensagem recebida: " + mensagem);
    }
}
