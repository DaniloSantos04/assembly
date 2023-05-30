package br.com.assembly.service;

import br.com.assembly.domain.entity.Vote;
import br.com.assembly.domain.repository.VoteRepository;
import br.com.assembly.domain.repository.VotingSessionRepository;
import br.com.assembly.web.dto.request.vote.VoteRequest;
import br.com.assembly.web.dto.response.CountVotesResponse;
import br.com.assembly.web.dto.response.VoteResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final VotingSessionRepository votingSessionRepository;

    public VoteResponse registerVote(VoteRequest voteRequest){

        var agenda = votingSessionRepository.findByIdAgenda(voteRequest.getIdAgenda());

        if (agenda == null) return null;

        if(agenda.isActive()){
            var lastVote = voteRepository.findFirstByOrderByIdDesc();
            var maxId = lastVote != null ? lastVote.getId()+1 : 1;
            var vote = Vote.fromEntenty(voteRequest);
            vote.setId(maxId);
            var voteSaved = voteRepository.save(vote);
            return voteSaved.fromResponse();
        }

        return new VoteResponse();
    }

    public CountVotesResponse countVotes(Long id){
        var votingSession = votingSessionRepository.findByIdAgenda(id);

        if (votingSession == null) return null;

        if(votingSession.isActive()) return null;

        var votes = voteRepository.findByIdAgenda(id);

        if(votes.isEmpty()) return null;

        var TotalVotesSim = votes.stream()
                .filter(Vote::isVote)
                .count();

        var TotalVotesNao = votes.stream()
                .filter(vote -> !vote.isVote())
                .count();

        return CountVotesResponse.builder()
                .idAgenda(id)
                .totalVotesSim(TotalVotesSim)
                .totalVotesNao(TotalVotesNao)
                .build();
    }
}
