package br.com.assembly.service;

import br.com.assembly.domain.entity.Vote;
import br.com.assembly.domain.entity.VotingSession;
import br.com.assembly.domain.repository.VoteRepository;
import br.com.assembly.domain.repository.VotingSessionRepository;
import br.com.assembly.mock.VoteMock;
import br.com.assembly.mock.VotingSessionMock;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private VotingSessionRepository votingSessionRepository;

    @InjectMocks
    private VoteService voteService;

    @Test
    public void firstRegisterVote() {
        var votingSession = VotingSessionMock.buildFullVotingSession();
        var voteRequest = VoteMock.createdVoteRequest();
        var voteSaved = VoteMock.createdVote();
        var voteResponse = VoteMock.createdVoteResponse();

        when(votingSessionRepository.findByIdAgenda(voteRequest.getIdAgenda())).thenReturn(votingSession);
        when(voteRepository.findFirstByOrderByIdDesc()).thenReturn(null);
        when(voteRepository.save(any(Vote.class))).thenReturn(voteSaved);

        var response = voteService.registerVote(voteRequest);

        assertNotNull(response);
        assertEquals(response.getId(), voteResponse.getId());
        assertEquals(response.getIdAssociate(), voteResponse.getIdAssociate());
        assertEquals(response.getIdAgenda(), voteResponse.getIdAgenda());
        assertEquals(response.isVote(), voteResponse.isVote());

        verify(votingSessionRepository, times(1)).findByIdAgenda(anyLong());
        verify(voteRepository, times(1)).findFirstByOrderByIdDesc();
        verify(voteRepository, times(1)).save(any(Vote.class));
    }

    @Test
    public void othersRegisterVote() {
        var votingSession = VotingSessionMock.buildFullVotingSession();
        var voteRequest = VoteMock.createdVoteRequest();
        var lastVote = VoteMock.createdVote();
        var voteSaved = VoteMock.createdVote();
        voteSaved.setId(lastVote.getId()+1);
        var voteResponse = VoteMock.createdVoteResponse();
        voteResponse.setId(lastVote.getId()+1);

        when(votingSessionRepository.findByIdAgenda(voteRequest.getIdAgenda())).thenReturn(votingSession);
        when(voteRepository.findFirstByOrderByIdDesc()).thenReturn(lastVote);
        when(voteRepository.save(any(Vote.class))).thenReturn(voteSaved);

        var response = voteService.registerVote(voteRequest);

        assertNotNull(response);
        assertEquals(response.getId(), voteResponse.getId());
        assertEquals(response.getIdAssociate(), voteResponse.getIdAssociate());
        assertEquals(response.getIdAgenda(), voteResponse.getIdAgenda());
        assertEquals(response.isVote(), voteResponse.isVote());

        verify(votingSessionRepository, times(1)).findByIdAgenda(anyLong());
        verify(voteRepository, times(1)).findFirstByOrderByIdDesc();
        verify(voteRepository, times(1)).save(any(Vote.class));
    }

    @Test
    public void registerVoteWithAgendaNull() {
        var voteRequest = VoteMock.createdVoteRequest();

        when(votingSessionRepository.findByIdAgenda(voteRequest.getIdAgenda())).thenReturn(null);

        var response = voteService.registerVote(voteRequest);

        assertNull(response);

        verify(votingSessionRepository, times(1)).findByIdAgenda(anyLong());
        verify(voteRepository, never()).findFirstByOrderByIdDesc();
        verify(voteRepository, never()).save(any(Vote.class));
    }



    @Test
    public void registerVoteWithAgendaActiveFalse() {
        var voteRequest = VoteMock.createdVoteRequest();
        var votingSession = VotingSessionMock.buildFullVotingSession();
        votingSession.setActive(false);

        when(votingSessionRepository.findByIdAgenda(voteRequest.getIdAgenda())).thenReturn(votingSession);

        var response = voteService.registerVote(voteRequest);

        assertNotNull(response);
        verify(votingSessionRepository, times(1)).findByIdAgenda(anyLong());
        verify(voteRepository, never()).findFirstByOrderByIdDesc();
        verify(voteRepository, never()).save(any(Vote.class));
    }

    @Test
    public void countVotesSuccess() {
        var agendaId = 1L;
        var agenda = VotingSession.builder().build();
        var votes = VoteMock.createdVotes();

        when(votingSessionRepository.findByIdAgenda(agendaId)).thenReturn(agenda);
        when(voteRepository.findByIdAgenda(agendaId)).thenReturn(votes);

        var response = voteService.countVotes(agendaId);

        assertNotNull(response);
        assertEquals(response.getIdAgenda(), agendaId);
        assertEquals(response.getTotalVotesSim(), 2);
        assertEquals(response.getTotalVotesNao(), 1);
        verify(votingSessionRepository, times(1)).findByIdAgenda(anyLong());
        verify(voteRepository, times(1)).findByIdAgenda(anyLong());
    }

    @Test
    public void countVotesWhenAgendaNull() {
        var agendaId = 1L;
        var votes = VoteMock.createdVotes();

        when(votingSessionRepository.findByIdAgenda(agendaId)).thenReturn(null);
        when(voteRepository.findByIdAgenda(agendaId)).thenReturn(votes);

        var response = voteService.countVotes(agendaId);

        assertNull(response);
        verify(votingSessionRepository, times(1)).findByIdAgenda(anyLong());
        verify(voteRepository, never()).findByIdAgenda(anyLong());
    }

    @Test
    public void countVotesWhenVotesEmpty() {
        var agendaId = 1L;
        var agenda = VotingSession.builder().build();

        when(votingSessionRepository.findByIdAgenda(agendaId)).thenReturn(agenda);
        when(voteRepository.findByIdAgenda(agendaId)).thenReturn(Collections.emptyList());

        var response = voteService.countVotes(agendaId);

        assertNull(response);
        verify(votingSessionRepository, times(1)).findByIdAgenda(anyLong());
        verify(voteRepository, times(1)).findByIdAgenda(anyLong());
    }
}
