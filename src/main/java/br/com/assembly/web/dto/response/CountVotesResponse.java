package br.com.assembly.web.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountVotesResponse {
    private Long idVotingSession;
    private Long idAgenda;
    private Long totalVotesSim;
    private Long totalVotesNao;

}
