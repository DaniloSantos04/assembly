package br.com.assembly.web.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VoteResponse {

    private Long id;
    private Long idAssociate;
    private Long idAgenda;
    private boolean vote;
}
