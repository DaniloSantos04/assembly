package br.com.assembly.service;

import br.com.assembly.domain.entity.VotingSession;
import br.com.assembly.domain.repository.VotingSessionRepository;
import br.com.assembly.web.dto.request.votingsession.SessionUpdateRequest;
import br.com.assembly.web.dto.request.votingsession.VotingSessionRequest;
import br.com.assembly.web.dto.response.VotingSessionResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VotingSessionService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private final VotingSessionRepository votingSessionRepository;

    public VotingSessionResponse createdVotingSession(VotingSessionRequest votingSessionRequest){
        var lastVotingSession = votingSessionRepository.findFirstByOrderByIdDesc();
        var maxId = lastVotingSession != null ? lastVotingSession.getId()+1 : 1;

        var votingSession = VotingSession.fromEntenty(votingSessionRequest);
        votingSession.setId(maxId);

        if(votingSession.getDateStart() == null) votingSession.setDateStart(LocalDateTime.parse(LocalDateTime.now().format(FORMATTER), FORMATTER));
        if(votingSession.getDateEnd() == null) votingSession.setDateEnd(LocalDateTime.parse(LocalDateTime.now().plusMinutes(1).format(FORMATTER), FORMATTER));

        var createdVotingSession = votingSessionRepository.save(votingSession);
        return createdVotingSession.fromResponse();
    }

    public VotingSessionResponse findVotingSessionById(Long id) {
        var votingSessionOptional  =votingSessionRepository.findById(id);

        if (votingSessionOptional.isEmpty()) {
            return null;
        }

        return votingSessionOptional.get().fromResponse();
    }

    public VotingSessionResponse updateVotingSession(Long id, SessionUpdateRequest sessionUpdateRequest) {
        var previousSession = findVotingSessionById(id);

        if(previousSession == null) return null;


        if(sessionUpdateRequest.getIdAgenda() != null) previousSession.setIdAgenda(sessionUpdateRequest.getIdAgenda());
        if(sessionUpdateRequest.getActive() != null) previousSession.setActive(sessionUpdateRequest.getActive());
        if(sessionUpdateRequest.getDateStart() != null) previousSession.setDateStart(sessionUpdateRequest.getDateStart());
        if(sessionUpdateRequest.getDateEnd() != null) previousSession.setDateEnd(sessionUpdateRequest.getDateEnd());

        var createdAgenda = votingSessionRepository.save(VotingSession.fromEntenty(previousSession));
        return createdAgenda.fromResponse();
    }

    public void closedVotingSession() {
        var dateNow = LocalDateTime.now();

        var sessions = votingSessionRepository.findAllByActive(true);

        var sessionsToClosed = sessions.stream()
                .filter(session -> session.getDateEnd().isBefore(dateNow))
                .collect(Collectors.toList());

        sessionsToClosed.forEach(session -> session.setActive(false));

        votingSessionRepository.saveAll(sessionsToClosed);
    }
}
