package br.com.assembly.task;

import br.com.assembly.service.VotingSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MinuteTask {

    @Autowired
    private VotingSessionService votingSessionService;

    @Scheduled(cron = "0 * * * * *")
    public void executeMinuteTask() {
        try {
            votingSessionService.closedVotingSession();
        }catch (Exception e){
            System.out.println("Erro execução Scheduled");
        }
    }
}
