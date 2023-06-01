package br.com.assembly.web.dto.response;

import br.com.assembly.enums.VoteEnum;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteResponse {

    private Long id;
    private Long idAssociate;
    private Long idVotingSession;
    private VoteEnum vote;
}
