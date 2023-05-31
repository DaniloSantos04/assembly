package br.com.assembly.service;

import br.com.assembly.domain.entity.VotingSession;
import br.com.assembly.domain.repository.VotingSessionRepository;
import br.com.assembly.exception.CustomException;
import br.com.assembly.web.dto.request.votingsession.SessionUpdateRequest;
import br.com.assembly.web.dto.request.votingsession.VotingSessionRequest;
import br.com.assembly.web.dto.response.VotingSessionResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class VotingSessionService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private final VotingSessionRepository votingSessionRepository;

    public VotingSessionResponse createdVotingSession(VotingSessionRequest votingSessionRequest) throws CustomException {
        try{
            log.info("Starting the createdVotingSession method. Parameters: votingSessionRequest={}", votingSessionRequest);
            var lastVotingSession = votingSessionRepository.findFirstByOrderByIdDesc();
            log.info("Database query returned lastVotingSession={}", lastVotingSession);
            var maxId = lastVotingSession != null ? lastVotingSession.getId()+1 : 1;

            var votingSession = VotingSession.fromEntenty(votingSessionRequest);
            votingSession.setId(maxId);

            if(votingSession.getDateStart() == null) votingSession.setDateStart(LocalDateTime.parse(LocalDateTime.now().format(FORMATTER), FORMATTER));
            if(votingSession.getDateEnd() == null) votingSession.setDateEnd(LocalDateTime.parse(LocalDateTime.now().plusMinutes(1).format(FORMATTER), FORMATTER));

            log.info("Saving voting session in database with parameters={}", votingSession);
            var createdVotingSession = votingSessionRepository.save(votingSession);
            return createdVotingSession.fromResponse();

        } catch (Exception e) {
            log.error("An unexpected error occurred: {}, cause: {}", e.getMessage(), e.getCause());
            throw new CustomException("We encountered an unexpected error.", 500);
        }
    }

    public VotingSessionResponse findVotingSessionById(Long id) throws CustomException {
        try{
            log.info("Starting the findVotingSessionById method. Parameters: id={}", id);
            log.info("Querying voting session in the database with parameters={}", id);
            var votingSessionOptional  =votingSessionRepository.findById(id);

            if (votingSessionOptional.isEmpty()) {
                throw new CustomException("Voting session does not exist for the id: ".concat(id.toString()), 404);
            }

            return votingSessionOptional.get().fromResponse();

        } catch (CustomException e) {
            log.error("A CustomException occurred: {}", e.getMessage());
            throw new CustomException(e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}, cause: {}", e.getMessage(), e.getCause());
            throw new CustomException("We encountered an unexpected error.", 500);
        }
    }

    public VotingSessionResponse updateVotingSession(Long id, SessionUpdateRequest sessionUpdateRequest) throws CustomException {
        try{
            log.info("Starting the updateAgendaId method. Parameters: id={} and sessionUpdateRequest={}", id, sessionUpdateRequest);
            var previousSession = findVotingSessionById(id);

            if(sessionUpdateRequest.getIdAgenda() != null) previousSession.setIdAgenda(sessionUpdateRequest.getIdAgenda());
            if(sessionUpdateRequest.getActive() != null) previousSession.setActive(sessionUpdateRequest.getActive());
            if(sessionUpdateRequest.getDateStart() != null) previousSession.setDateStart(sessionUpdateRequest.getDateStart());
            if(sessionUpdateRequest.getDateEnd() != null) previousSession.setDateEnd(sessionUpdateRequest.getDateEnd());

            log.info("Saving voting session in database with parameters={}", previousSession);
            var createdVotingSession = votingSessionRepository.save(VotingSession.fromEntenty(previousSession));
            return createdVotingSession.fromResponse();
        } catch (CustomException e) {
            log.error("A CustomException occurred: {}", e.getMessage());
            throw new CustomException(e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}, cause: {}", e.getMessage(), e.getCause());
            throw new CustomException("We encountered an unexpected error.", 500);
        }
    }

    public void closedVotingSession() throws CustomException {
        try{
            log.info("Starting the closedVotingSession method.");
            var dateNow = LocalDateTime.now();

            var sessions = votingSessionRepository.findAllByActive(true);
            log.info("Database query returned {} sessions for active={}", sessions.size(), true);

            var sessionsToClosed = sessions.stream()
                    .filter(session -> session.getDateEnd().isBefore(dateNow))
                    .collect(Collectors.toList());

            log.info("{} sessions to be closed.", sessionsToClosed.size());

            sessionsToClosed.forEach(session -> session.setActive(false));

            log.info("Saving {} session to database", sessionsToClosed.size());
            votingSessionRepository.saveAll(sessionsToClosed);
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}, cause: {}", e.getMessage(), e.getCause());
            throw new CustomException("We encountered an unexpected error.", 500);
        }
    }
}
