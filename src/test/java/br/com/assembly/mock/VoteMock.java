package br.com.assembly.mock;

import br.com.assembly.domain.entity.Vote;
import br.com.assembly.enums.VoteEnum;
import br.com.assembly.web.dto.request.vote.VoteRequest;
import br.com.assembly.web.dto.response.CountVotesResponse;
import br.com.assembly.web.dto.response.VoteResponse;

import java.util.Arrays;
import java.util.List;

public class VoteMock {

    public static VoteRequest createdVoteRequest(){
        return VoteRequest.builder()
                .idAssociate(1L)
                .idVotingSession(1L)
                .vote(VoteEnum.SIM)
                .build();
    }

    public static VoteResponse createdVoteResponse() {
        return VoteResponse.builder()
                .id(1L)
                .idAssociate(1L)
                .idVotingSession(1L)
                .vote(VoteEnum.SIM)
                .build();
    }

    public static Vote createdVote(){
        return Vote.builder()
                .id(1L)
                .idAssociate(1L)
                .idVotingSession(1L)
                .vote(true)
                .build();
    }

    public static List<Vote> createdVotes(){
        return  Arrays.asList(
                Vote.builder()
                        .id(1L)
                        .idAssociate(1L)
                        .idVotingSession(1L)
                        .vote(true)
                        .build(),
                Vote.builder()
                        .id(2L)
                        .idAssociate(2L)
                        .idVotingSession(1L)
                        .vote(false)
                        .build(),
                Vote.builder()
                        .id(3L)
                        .idAssociate(3L)
                        .idVotingSession(1L)
                        .vote(true)
                        .build()
        );
    }

    public static CountVotesResponse createdCountVotesResponse(){
        return CountVotesResponse.builder()
                .idVotingSession(1L)
                .totalVotesSim(2L)
                .totalVotesNao(1L)
                .build();
    }
}
