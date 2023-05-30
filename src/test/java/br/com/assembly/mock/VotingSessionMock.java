package br.com.assembly.mock;

import br.com.assembly.domain.entity.VotingSession;
import br.com.assembly.web.dto.request.votingsession.SessionUpdateRequest;
import br.com.assembly.web.dto.request.votingsession.VotingSessionRequest;
import br.com.assembly.web.dto.response.AgendaResponse;
import br.com.assembly.web.dto.response.VotingSessionResponse;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class VotingSessionMock {

    public static VotingSessionRequest buildFullVotingSessionRequest(){
        return VotingSessionRequest.builder()
                .idAgenda(1L)
                .dateStart(LocalDateTime.parse("2023-05-29T20:15"))
                .dateEnd(LocalDateTime.parse("2023-05-30T20:15"))
                .active(true)
                .build();
    }

    public static VotingSessionRequest buildWithoutDateEndVotingSessionRequest(){
        return VotingSessionRequest.builder()
                .idAgenda(1L)
                .dateStart(LocalDateTime.parse("2023-05-29T20:15"))
                .active(true)
                .build();
    }

    public static VotingSessionResponse buildFullVotingSessionResponse(){
        return VotingSessionResponse.builder()
                .id(1L)
                .idAgenda(1L)
                .dateStart(LocalDateTime.parse("2023-05-29T20:15"))
                .dateEnd(LocalDateTime.parse("2023-05-30T20:15"))
                .active(true)
                .build();
    }

    public static SessionUpdateRequest buildFullSessionUpdateRequest(){
        return SessionUpdateRequest.builder()
                .idAgenda(2L)
                .dateStart(LocalDateTime.parse("2023-05-30T20:15"))
                .dateEnd(LocalDateTime.parse("2023-05-31T20:15"))
                .active(false)
                .build();
    }

    public static VotingSessionResponse buildFullVotingSessionResponseUpdated(){
        return VotingSessionResponse.builder()
                .id(1L)
                .idAgenda(2L)
                .dateStart(LocalDateTime.parse("2023-05-30T20:15"))
                .dateEnd(LocalDateTime.parse("2023-05-31T20:15"))
                .active(false)
                .build();
    }

    public static VotingSession buildFullVotingSession(){
        return VotingSession.builder()
                .id(1L)
                .idAgenda(1L)
                .dateStart(LocalDateTime.parse("2023-05-29T20:15"))
                .dateEnd(LocalDateTime.parse("2023-05-30T20:15"))
                .active(true)
                .build();
    }

    public static VotingSession buildFullVotingSessionUpdate(){
        return VotingSession.builder()
                .id(1L)
                .idAgenda(2L)
                .dateStart(LocalDateTime.parse("2023-05-30T20:15"))
                .dateEnd(LocalDateTime.parse("2023-05-31T20:15"))
                .active(false)
                .build();
    }

    public static List<VotingSession> onlyVotingSessionActiveList(){
        return  Arrays.asList(
                VotingSession.builder()
                        .id(1L)
                        .idAgenda(1L)
                        .dateStart(LocalDateTime.parse("2023-05-29T20:15"))
                        .dateEnd(LocalDateTime.now())
                        .active(true)
                        .build(),
                VotingSession.builder()
                        .id(2L)
                        .idAgenda(2L)
                        .dateStart(LocalDateTime.parse("2023-05-30T20:15"))
                        .dateEnd(LocalDateTime.now())
                        .active(true)
                        .build()
        );
    }

    public static List<VotingSession> onlyVotingSessionActiveAndDateEndFutureList(){
        return  Arrays.asList(
                VotingSession.builder()
                        .id(1L)
                        .idAgenda(1L)
                        .dateStart(LocalDateTime.parse("2023-05-29T20:15"))
                        .dateEnd(LocalDateTime.now())
                        .active(true)
                        .build(),
                VotingSession.builder()
                        .id(2L)
                        .idAgenda(2L)
                        .dateStart(LocalDateTime.parse("2023-05-30T20:15"))
                        .dateEnd(LocalDateTime.now())
                        .active(true)
                        .build(),
                VotingSession.builder()
                        .id(3L)
                        .idAgenda(3L)
                        .dateStart(LocalDateTime.parse("2023-05-30T20:15"))
                        .dateEnd(LocalDateTime.now().plusMinutes(30))
                        .active(true)
                        .build()
        );
    }
}
