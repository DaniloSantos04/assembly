package br.com.assembly.domain.entity;

import br.com.assembly.web.dto.request.votingsession.VotingSessionRequest;
import br.com.assembly.web.dto.response.VotingSessionResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("voting_session")
@Getter
@Setter
@Builder
public class VotingSession {

    @Id
    private Long id;
    private Long idAgenda;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
    private boolean active;

    public static VotingSession fromEntenty(VotingSessionRequest dto){
        return VotingSession.builder()
                .idAgenda(dto.getIdAgenda())
                .dateStart(dto.getDateStart())
                .dateEnd(dto.getDateEnd())
                .active(dto.isActive())
                .build();
    }

    public static VotingSession fromEntenty(VotingSessionResponse dto){
        return VotingSession.builder()
                .id(dto.getId())
                .idAgenda(dto.getIdAgenda())
                .dateStart(dto.getDateStart())
                .dateEnd(dto.getDateEnd())
                .active(dto.isActive())
                .build();
    }

    public VotingSessionResponse fromResponse() {
        return VotingSessionResponse.builder()
                .id(this.id)
                .idAgenda(this.idAgenda)
                .dateStart(this.dateStart)
                .dateEnd(this.dateEnd)
                .active(this.active)
                .build();
    }
}
