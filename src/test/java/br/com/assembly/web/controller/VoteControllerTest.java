package br.com.assembly.web.controller;

import br.com.assembly.mock.VoteMock;
import br.com.assembly.service.VoteService;
import br.com.assembly.web.dto.request.vote.VoteRequest;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    private VoteService voteService;

    private static final String URL_PATCH = "/v1/vote";

    @Test
    public void registerVoteSuccess() throws Exception {
        var voteRequest = VoteMock.createdVoteRequest();
        var voteResponse = VoteMock.createdVoteResponse();

        when(voteService.registerVote(any(VoteRequest.class))).thenReturn(voteResponse);

        mockMvc.perform(MockMvcRequestBuilders.post(URL_PATCH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voteRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void registerVoteLocationHeaderSuccess() throws Exception {
        var voteRequest = VoteMock.createdVoteRequest();
        var voteResponse = VoteMock.createdVoteResponse();

        var expectedLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(URL_PATCH.concat("/id/{id}"))
                .buildAndExpand(voteResponse.getId())
                .toUri()
                .toString();

        when(voteService.registerVote(any(VoteRequest.class))).thenReturn(voteResponse);

        var result = mockMvc.perform(MockMvcRequestBuilders.post(URL_PATCH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voteRequest))).andReturn();

        assertEquals(expectedLocation, result.getResponse().getHeaderValue("Location").toString());
    }
}
