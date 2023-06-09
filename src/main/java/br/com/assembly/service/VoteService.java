package br.com.assembly.service;

import br.com.assembly.domain.entity.Vote;
import br.com.assembly.domain.repository.VoteRepository;
import br.com.assembly.domain.repository.VotingSessionRepository;
import br.com.assembly.exception.CustomException;
import br.com.assembly.web.dto.request.vote.VoteRequest;
import br.com.assembly.web.dto.response.CountVotesResponse;
import br.com.assembly.web.dto.response.VoteResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class VoteService {

    private final VoteRepository voteRepository;
    private final VotingSessionRepository votingSessionRepository;

    public VoteResponse registerVote(VoteRequest voteRequest) throws CustomException {
        try {
            log.info("Starting the registerVote method. Parameters: voteRequest={}", voteRequest);

            log.info("Querying the voting session in the database for idVotingSession={}", voteRequest.getIdVotingSession());
            var votingSessionOptional = votingSessionRepository.findById(voteRequest.getIdVotingSession());

            if (votingSessionOptional.isEmpty()){
                log.warn("Voting session does not exist for the id: {}", voteRequest.getIdVotingSession());
                throw new CustomException("Voting session does not exist for the id: ".concat(voteRequest.getIdVotingSession().toString()), 404);
            }

            var votingSession = votingSessionOptional.get();

            log.info("Querying vote in the database for the parameters: idAssociate={} and idVotingSession={}", voteRequest.getIdAssociate(), voteRequest.getIdVotingSession());
            var associateAlreadyVoted = voteRepository.findByIdAssociateAndIdVotingSession(voteRequest.getIdAssociate(), voteRequest.getIdVotingSession());

            if (associateAlreadyVoted != null){
                log.warn("Associate has already voted.");
                throw new CustomException("Associate has already voted.", 400);
            }

            log.debug("Checking if voting session is active for idVotingSession={}", voteRequest.getIdVotingSession());
            if(votingSession.isActive()){
                var lastVote = voteRepository.findFirstByOrderByIdDesc();
                log.info("Database query returned lastVote={}", lastVote);
                var maxId = lastVote != null ? lastVote.getId()+1 : 1;
                var vote = Vote.fromEntenty(voteRequest);
                vote.setId(maxId);
                log.info("Saving vote in database with parameters={}", vote);
                var voteSaved = voteRepository.save(vote);
                return voteSaved.fromResponse();
            }
            log.warn("The voting session is not active for the provided id: {}", voteRequest.getIdVotingSession());
            throw new CustomException("The voting session is not active for the provided id: ".concat(voteRequest.getIdVotingSession().toString()), 403);

        } catch (CustomException e) {
            log.error("A CustomException occurred: {}", e.getMessage());
            throw new CustomException(e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}, cause: {}", e.getMessage(), e.getCause());
            throw new CustomException("We encountered an unexpected error.", 500);
        }
    }

    public CountVotesResponse countVotes(Long id) throws CustomException {
        try {
            log.info("Starting the countVotes method. Parameters: id={}", id);
            log.info("Querying the voting session in the database for idAgenda={}", id);
            var votingSessionOptional= votingSessionRepository.findById(id);

            if (votingSessionOptional.isEmpty())
                throw new CustomException("Voting session does not exist for the id: ".concat(id.toString()), 404);

            var votingSession = votingSessionOptional.get();

            if (votingSession.isActive())
                throw new CustomException("Voting session is active for the id: " + id.toString(), 409);

            var votes = voteRepository.findByIdVotingSession(id);

            log.info("Database query returned {} votes for id={}", votes.size(), id);

            var TotalVotesSim = votes.stream()
                    .filter(Vote::isVote)
                    .count();

            var TotalVotesNao = votes.stream()
                    .filter(vote -> !vote.isVote())
                    .count();

            return CountVotesResponse.builder()
                    .idVotingSession(id)
                    .idAgenda(votingSession.getIdAgenda())
                    .totalVotesSim(TotalVotesSim)
                    .totalVotesNao(TotalVotesNao)
                    .build();

        } catch (CustomException e) {
            throw new CustomException(e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            throw new CustomException("We encountered an unexpected error.", 500);
        }
    }
}
