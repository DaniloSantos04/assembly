package br.com.assembly.task;

import br.com.assembly.service.VotingSessionService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class MinuteTask {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private VotingSessionService votingSessionService;


    @Scheduled(cron = "0 * * * * *")
    public void executeMinuteTask() {
        try {
            votingSessionService.closedVotingSession();
            var mensagem = "Publicada mensagem: ".concat(LocalDateTime.now().toString()); // Defina a mensagem que deseja enviar
            rabbitTemplate.convertAndSend(mensagem);
        }catch (Exception e){
            System.out.println("Erro execução Scheduled");
        }
    }
}
