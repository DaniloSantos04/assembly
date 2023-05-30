package br.com.assembly.web.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VotingSessionResponse {

    private Long id;
    private Long idAgenda;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
    private boolean active;
}
