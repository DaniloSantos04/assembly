package br.com.assembly.service;

import br.com.assembly.domain.entity.Vote;
import br.com.assembly.domain.entity.VotingSession;
import br.com.assembly.domain.repository.VoteRepository;
import br.com.assembly.domain.repository.VotingSessionRepository;
import br.com.assembly.exception.CustomException;
import br.com.assembly.mock.VoteMock;
import br.com.assembly.mock.VotingSessionMock;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Optional;

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
    public void firstRegisterVote() throws CustomException {
        var votingSession = VotingSessionMock.buildFullVotingSession();
        var voteRequest = VoteMock.createdVoteRequest();
        var voteSaved = VoteMock.createdVote();
        var voteResponse = VoteMock.createdVoteResponse();

        when(votingSessionRepository.findById(voteRequest.getIdVotingSession())).thenReturn(Optional.of(votingSession));

        when(voteRepository.findFirstByOrderByIdDesc()).thenReturn(null);
        when(voteRepository.save(any(Vote.class))).thenReturn(voteSaved);

        var response = voteService.registerVote(voteRequest);

        assertNotNull(response);
        assertEquals(response.getId(), voteResponse.getId());
        assertEquals(response.getIdAssociate(), voteResponse.getIdAssociate());
        assertEquals(response.getIdVotingSession(), voteResponse.getIdVotingSession());
        assertEquals(response.getVote(), voteResponse.getVote());

        verify(votingSessionRepository, times(1)).findById(eq(voteRequest.getIdVotingSession()));
        verify(voteRepository, times(1)).findFirstByOrderByIdDesc();
        verify(voteRepository, times(1)).save(any(Vote.class));
    }

    @Test
    public void othersRegisterVote() throws CustomException {
        var votingSession = VotingSessionMock.buildFullVotingSession();
        var voteRequest = VoteMock.createdVoteRequest();
        var lastVote = VoteMock.createdVote();
        var voteSaved = VoteMock.createdVote();
        voteSaved.setId(lastVote.getId()+1);
        var voteResponse = VoteMock.createdVoteResponse();
        voteResponse.setId(lastVote.getId()+1);

        when(votingSessionRepository.findById(voteRequest.getIdVotingSession())).thenReturn(Optional.of(votingSession));
        when(voteRepository.findFirstByOrderByIdDesc()).thenReturn(lastVote);
        when(voteRepository.save(any(Vote.class))).thenReturn(voteSaved);

        var response = voteService.registerVote(voteRequest);

        assertNotNull(response);
        assertEquals(response.getId(), voteResponse.getId());
        assertEquals(response.getIdAssociate(), voteResponse.getIdAssociate());
        assertEquals(response.getIdVotingSession(), voteResponse.getIdVotingSession());
        assertEquals(response.getVote(), voteResponse.getVote());

        verify(votingSessionRepository, times(1)).findById(eq(voteRequest.getIdVotingSession()));
        verify(voteRepository, times(1)).findFirstByOrderByIdDesc();
        verify(voteRepository, times(1)).save(any(Vote.class));
    }

    @Test
    public void registerVoteWithAgendaNull() throws CustomException {
        var voteRequest = VoteMock.createdVoteRequest();

        try{

            when(votingSessionRepository.findById(voteRequest.getIdVotingSession())).thenReturn(Optional.empty());

            var response = voteService.registerVote(voteRequest);

        } catch (CustomException e) {
            assertEquals("Voting session does not exist for the id: 1", e.getMessage());
            assertEquals(404, e.getHttpStatus());
        }

        verify(votingSessionRepository, times(1)).findById(eq(voteRequest.getIdVotingSession()));
        verify(voteRepository, never()).findFirstByOrderByIdDesc();
        verify(voteRepository, never()).save(any(Vote.class));
    }



    @Test
    public void registerVoteWithAgendaActiveFalse() throws CustomException {
        var voteRequest = VoteMock.createdVoteRequest();
        try{

            var votingSession = VotingSessionMock.buildFullVotingSession();
            votingSession.setActive(false);

            when(votingSessionRepository.findById(voteRequest.getIdVotingSession())).thenReturn(Optional.of(votingSession));

            var response = voteService.registerVote(voteRequest);

        } catch (CustomException e) {
            assertEquals("The voting session is not active for the provided id: 1", e.getMessage());
            assertEquals(403, e.getHttpStatus());
        }
        verify(votingSessionRepository, times(1)).findById(eq(voteRequest.getIdVotingSession()));
        verify(voteRepository, never()).findFirstByOrderByIdDesc();
        verify(voteRepository, never()).save(any(Vote.class));
    }

    @Test
    public void countVotesSuccess() throws CustomException {
        var id = 1L;
        var votingSession = VotingSession.builder().build();
        var votes = VoteMock.createdVotes();

        when(votingSessionRepository.findById(id)).thenReturn(Optional.of(votingSession));
        when(voteRepository.findByIdVotingSession(id)).thenReturn(votes);

        var response = voteService.countVotes(id);

        assertNotNull(response);
        assertEquals(response.getIdVotingSession(), id);
        assertEquals(response.getTotalVotesSim(), 2);
        assertEquals(response.getTotalVotesNao(), 1);

        verify(votingSessionRepository, times(1)).findById(eq(id));
        verify(voteRepository, times(1)).findByIdVotingSession(anyLong());
    }

    @Test
    public void countVotesWhenVotingSessionIsNull() throws CustomException {
        var id = 1L;
        try{

            var votes = VoteMock.createdVotes();

            when(votingSessionRepository.findById(id)).thenReturn(Optional.empty());
            when(voteRepository.findByIdVotingSession(id)).thenReturn(votes);

            var response = voteService.countVotes(id);

        } catch (CustomException e) {
            assertEquals("Voting session does not exist for the id: 1", e.getMessage());
            assertEquals(404, e.getHttpStatus());
        }

        verify(votingSessionRepository, times(1)).findById(eq(id));
        verify(voteRepository, never()).findByIdVotingSession(anyLong());
    }

    @Test
    public void countVotesWhenVotesEmpty() throws CustomException {
        var id = 1L;
        var votingSession = VotingSession.builder().build();

        when(votingSessionRepository.findById(id)).thenReturn(Optional.of(votingSession));
        when(voteRepository.findByIdVotingSession(id)).thenReturn(Collections.emptyList());

        var response = voteService.countVotes(id);

        assertNotNull(response);
        assertEquals(response.getIdVotingSession(), id);
        assertEquals(response.getTotalVotesSim(), 0);
        assertEquals(response.getTotalVotesNao(), 0);

        verify(votingSessionRepository, times(1)).findById(eq(id));
        verify(voteRepository, times(1)).findByIdVotingSession(anyLong());
    }
}
