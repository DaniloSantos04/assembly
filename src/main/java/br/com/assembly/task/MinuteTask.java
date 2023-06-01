package br.com.assembly.task;

import br.com.assembly.exception.CustomException;
import br.com.assembly.service.VoteService;
import br.com.assembly.service.VotingSessionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MinuteTask {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private VotingSessionService votingSessionService;

    @Autowired
    private VoteService voteService;

    @Autowired
    protected ObjectMapper objectMapper;


    @Scheduled(cron = "0 * * * * *")
    public void executeMinuteTask() {
        try {
            var VotingSessionResponses = votingSessionService.closedVotingSession();

            VotingSessionResponses.forEach(session ->{
                try {
                    var resultVote = voteService.countVotes(session.getIdAgenda());
                    rabbitTemplate.convertAndSend(objectMapper.writeValueAsString(resultVote));
                } catch (JsonProcessingException e) {
                    System.out.println("Error converting object to JSON: " + e.getMessage());
                } catch (CustomException e) {
                    System.out.println("Count votes error: " + e.getMessage());
                }
            });
        }catch (Exception e){
            System.out.println("Scheduled execution error: " + e.getMessage());
        }
    }
}
