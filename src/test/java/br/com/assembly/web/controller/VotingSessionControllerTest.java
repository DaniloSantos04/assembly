package br.com.assembly.web.controller;

import br.com.assembly.mock.VoteMock;
import br.com.assembly.mock.VotingSessionMock;
import br.com.assembly.service.VoteService;
import br.com.assembly.service.VotingSessionService;
import br.com.assembly.web.dto.request.votingsession.SessionUpdateRequest;
import br.com.assembly.web.dto.request.votingsession.VotingSessionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class VotingSessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    private VoteService voteService;

    @MockBean
    private VotingSessionService votingSessionService;

    private static final String URL_PATCH = "/v1/voting-session";
    private static final String URL_PATCH_ID = "/v1/voting-session/id/{id}";

    private static final String MESSAGE_NOT_FOUND = "Voting session not found for the id: ";

    @Test
    public void createdVotingSessionSuccess() throws Exception {
        var votingSessionRequest = VotingSessionMock.buildFullVotingSessionRequest();
        var votingSessionResponse = VotingSessionMock.buildFullVotingSessionResponse();

        when(votingSessionService.createdVotingSession(any(VotingSessionRequest.class))).thenReturn(votingSessionResponse);

        mockMvc.perform(MockMvcRequestBuilders.post(URL_PATCH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(votingSessionRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void createdVotingSessionWithoutDateEndSuccess() throws Exception {
        var dateEnd = LocalDateTime.parse("2023-05-30T20:15");
        var votingSessionRequest = VotingSessionMock.buildWithoutDateEndVotingSessionRequest();
        votingSessionRequest.setDateEnd(dateEnd);
        var votingSessionResponse = VotingSessionMock.buildFullVotingSessionResponse();

        when(votingSessionService.createdVotingSession(any(VotingSessionRequest.class))).thenReturn(votingSessionResponse);

        mockMvc.perform(MockMvcRequestBuilders.post(URL_PATCH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(votingSessionRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateEnd").value(dateEnd.withNano(0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
    }

    @Test
    public void findVotingSessionByIdSucess() throws Exception {
        var id = 1L;
        var votingSessionResponse = VotingSessionMock.buildFullVotingSessionResponse();

        when(votingSessionService.findVotingSessionById(id)).thenReturn(votingSessionResponse);

        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATCH_ID,id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(votingSessionResponse.getId()));
    }

    @Test
    public void updateAgendaIdSuccess() throws Exception {
        var id = 1L;
        var sessionUpdateRequest = VotingSessionMock.buildFullSessionUpdateRequest();
        var votingSessionResponse = VotingSessionMock.buildFullVotingSessionResponseUpdated();

        when(votingSessionService.updateVotingSession(any(Long.class), any(SessionUpdateRequest.class))).thenReturn(votingSessionResponse);
        
        mockMvc.perform(MockMvcRequestBuilders.patch(URL_PATCH_ID,id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionUpdateRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(votingSessionResponse.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.idAgenda").value(sessionUpdateRequest.getIdAgenda()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.active").value(sessionUpdateRequest.getActive()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateStart").value(sessionUpdateRequest.getDateStart().withNano(0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateEnd").value(sessionUpdateRequest.getDateEnd().withNano(0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
    }

    @Test
    public void findResultAgendaSuccess() throws Exception {
        var id = 1L;
        var countVotesResponse = VoteMock.createdCountVotesResponse();

        when(voteService.countVotes(id)).thenReturn(countVotesResponse);

        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATCH.concat("/{id}/result"),id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idVotingSession").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalVotesSim").value(countVotesResponse.getTotalVotesSim()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalVotesNao").value(countVotesResponse.getTotalVotesNao()));
    }

}
