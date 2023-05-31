package br.com.assembly.service;

import br.com.assembly.domain.entity.VotingSession;
import br.com.assembly.domain.repository.VotingSessionRepository;
import br.com.assembly.exception.CustomException;
import br.com.assembly.mock.VotingSessionMock;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
public class VotingSessionServiceTest {

    @Mock
    private VotingSessionRepository votingSessionRepository;

    @InjectMocks
    private VotingSessionService votingSessionService;

    @Test
    public void createdVotingSessionWithoutDateStartAndEndSuccess() throws CustomException {
        var votingSessionRequest = VotingSessionMock.buildFullVotingSessionRequest();
        votingSessionRequest.setDateStart(null);
        votingSessionRequest.setDateEnd(null);

        var votingSessionResponse = VotingSessionMock.buildFullVotingSessionResponse();
        votingSessionResponse.setDateStart(LocalDateTime.now());
        votingSessionResponse.setDateEnd(LocalDateTime.now().plusMinutes(1));

        var votingSession = VotingSessionMock.buildFullVotingSession();

        when(votingSessionRepository.findFirstByOrderByIdDesc()).thenReturn(null);

        when(votingSessionRepository.save(any(VotingSession.class))).thenReturn(votingSession);

        var response  = votingSessionService.createdVotingSession(votingSessionRequest);

        assertNotNull(response);
        assertEquals(response.getId(), votingSessionResponse.getId());
        assertEquals(response.getIdAgenda(), votingSessionResponse.getIdAgenda());
        assertEquals(response.isActive(), votingSessionResponse.isActive());

        verify(votingSessionRepository, times(1)).findFirstByOrderByIdDesc();
        verify(votingSessionRepository, times(1)).save(any(VotingSession.class));
    }

    @Test
    public void createdVotingSessionCompleteSuccess() throws CustomException {
        var votingSessionRequest = VotingSessionMock.buildFullVotingSessionRequest();

        var votingSessionResponse = VotingSessionMock.buildFullVotingSessionResponse();

        var votingSession = VotingSessionMock.buildFullVotingSession();

        when(votingSessionRepository.findFirstByOrderByIdDesc()).thenReturn(null);

        when(votingSessionRepository.save(any(VotingSession.class))).thenReturn(votingSession);

        var response  = votingSessionService.createdVotingSession(votingSessionRequest);

        assertNotNull(response);
        assertEquals(response.getId(), votingSessionResponse.getId());
        assertEquals(response.getIdAgenda(), votingSessionResponse.getIdAgenda());
        assertEquals(response.isActive(), votingSessionResponse.isActive());
        assertEquals(response.getDateStart(), votingSessionResponse.getDateStart());
        assertEquals(response.getDateEnd(), votingSessionResponse.getDateEnd());

        verify(votingSessionRepository, times(1)).findFirstByOrderByIdDesc();
        verify(votingSessionRepository, times(1)).save(any(VotingSession.class));
    }

    @Test
    public void findVotingSessionByIdSuccess() throws CustomException {
        var id = 1L;
        var votingSession = VotingSessionMock.buildFullVotingSession();
        var votingSessionResponse = VotingSessionMock.buildFullVotingSessionResponse();

        when(votingSessionRepository.findById(eq(id))).thenReturn(Optional.of(votingSession));

        var response = votingSessionService.findVotingSessionById(id);

        assertNotNull(response);
        assertEquals(response.getId(), votingSessionResponse.getId());
        assertEquals(response.getIdAgenda(), votingSessionResponse.getIdAgenda());
        assertEquals(response.isActive(), votingSessionResponse.isActive());
        assertEquals(response.getDateStart(), votingSessionResponse.getDateStart());
        assertEquals(response.getDateEnd(), votingSessionResponse.getDateEnd());

        verify(votingSessionRepository, times(1)).findById(eq(id));
    }

    @Test
    public void findVotingSessionByIdEmpty() throws CustomException {
        var id = 1L;

        try{

            when(votingSessionRepository.findById(eq(id))).thenReturn(Optional.empty());

            var response = votingSessionService.findVotingSessionById(id);

        } catch (CustomException e) {
            assertEquals("Voting session does not exist for the id: 1", e.getMessage());
            assertEquals(404, e.getHttpStatus());
        }

        verify(votingSessionRepository, times(1)).findById(eq(id));
    }

    @Test
    public void updateVotingSessionNotExecuted() throws CustomException {
        Long id = 1L;
        try{
            var sessionUpdateRequest = VotingSessionMock.buildFullSessionUpdateRequest();

            when(votingSessionRepository.findById(eq(id))).thenReturn(Optional.empty());

            var updatedResponse = votingSessionService.updateVotingSession(id, sessionUpdateRequest);

        } catch (CustomException e) {
            assertEquals("Voting session does not exist for the id: 1", e.getMessage());
            assertEquals(404, e.getHttpStatus());
        }

        verify(votingSessionRepository, times(1)).findById(eq(id));
        verify(votingSessionRepository, never()).save(any(VotingSession.class));
    }

   @Test
    public void updateVotingSessionCompleteSuccess() throws CustomException {
        var id = 1L;
        var sessionUpdateRequest = VotingSessionMock.buildFullSessionUpdateRequest();
        var votingSession = VotingSessionMock.buildFullVotingSession();
        var updatedVotingSession= VotingSessionMock.buildFullVotingSessionUpdate();
        var votingSessionResponse = VotingSessionMock.buildFullVotingSessionResponseUpdated();


        when(votingSessionRepository.findById(eq(id))).thenReturn(Optional.of(votingSession));
        when(votingSessionRepository.save(any(VotingSession.class))).thenReturn(updatedVotingSession);

        var response = votingSessionService.updateVotingSession(id, sessionUpdateRequest);

        assertNotNull(response);
       assertEquals(response.getId(), votingSessionResponse.getId());
       assertEquals(response.getIdAgenda(), votingSessionResponse.getIdAgenda());
       assertEquals(response.isActive(), votingSessionResponse.isActive());
       assertEquals(response.getDateStart(), votingSessionResponse.getDateStart());
       assertEquals(response.getDateEnd(), votingSessionResponse.getDateEnd());

        verify(votingSessionRepository, times(1)).findById(eq(id));
        verify(votingSessionRepository, times(1)).save(any(VotingSession.class));
    }

    @Test
    public void closedVotingSessionOnlyVotingSessionActive() throws CustomException {
        var sessions = VotingSessionMock.onlyVotingSessionActiveList();
        var sessionClosed= VotingSessionMock.onlyVotingSessionActiveList();
        sessionClosed.get(0).setActive(false);
        sessionClosed.get(1).setActive(false);

        ArgumentCaptor<List<VotingSession>> captor = ArgumentCaptor.forClass(List.class);

        when(votingSessionRepository.findAllByActive(true)).thenReturn(sessions);
        when(votingSessionRepository.saveAll(captor.capture())).thenReturn(sessionClosed);

        votingSessionService.closedVotingSession();

        assertEquals(sessions.size(), captor.getValue().size());
    }

    @Test
    public void closedVotingSessionSuccess() throws CustomException {
        var sessions = VotingSessionMock.onlyVotingSessionActiveAndDateEndFutureList();
        var sizeOnlyActive = sessions.size()-1;

        var sessionClosed= VotingSessionMock.onlyVotingSessionActiveList();
        sessionClosed.get(0).setActive(false);
        sessionClosed.get(1).setActive(false);

        ArgumentCaptor<List<VotingSession>> captor = ArgumentCaptor.forClass(List.class);

        when(votingSessionRepository.findAllByActive(true)).thenReturn(sessions);
        when(votingSessionRepository.saveAll(captor.capture())).thenReturn(sessionClosed);

        votingSessionService.closedVotingSession();

        assertEquals(sizeOnlyActive, captor.getValue().size());
    }

    @Test
    public void closedVotingSessionNeverCall() throws CustomException {
        var sessions = VotingSessionMock.onlyVotingSessionActiveList();
        sessions.get(0).setDateEnd(LocalDateTime.now().plusMinutes(10));
        sessions.get(1).setDateEnd(LocalDateTime.now().plusMinutes(20));

        var sessionClosed= VotingSessionMock.onlyVotingSessionActiveList();
        sessionClosed.get(0).setActive(false);
        sessionClosed.get(1).setActive(false);

        ArgumentCaptor<List<VotingSession>> captor = ArgumentCaptor.forClass(List.class);

        when(votingSessionRepository.findAllByActive(true)).thenReturn(sessions);
        when(votingSessionRepository.saveAll(captor.capture())).thenReturn(sessionClosed);

        votingSessionService.closedVotingSession();

        assertEquals(0, captor.getValue().size());
    }
}