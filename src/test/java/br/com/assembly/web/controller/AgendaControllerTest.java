package br.com.assembly.web.controller;

import br.com.assembly.exception.CustomException;
import br.com.assembly.mock.AgendaMock;
import br.com.assembly.mock.VoteMock;
import br.com.assembly.service.AgendaService;
import br.com.assembly.service.VoteService;
import br.com.assembly.web.dto.request.agenda.AgendaRequest;
import br.com.assembly.web.dto.request.agenda.AgendaUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class AgendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    private AgendaService agendaService;
    @MockBean
    private VoteService voteService;

    private static final String URL_PATCH = "/v1/agenda";
    private static final String URL_PATCH_ID = "/v1/agenda/id/{id}";

    private static final String MESSAGE_AGENDA_NOT_FOUND = "Agenda not found for the id: ";
    private static final String MESSAGE_VOTE_NOT_FOUND = "The voting result was not found for the id: ";

    @Test
    public void createdAgendaSucess() throws Exception {
        var agendaRequest = AgendaMock.allAgendaRequestRequestedFieldsComplete();
        var createdAgenda = AgendaMock.allAgendaResponseRequestedFieldsComplete();

        when(agendaService.createAgenda(any(AgendaRequest.class))).thenReturn(createdAgenda);

        mockMvc.perform(MockMvcRequestBuilders.post(URL_PATCH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendaRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void createAgendaLocationHeaderSucess() throws Exception {
        var agendaRequest = AgendaMock.allAgendaRequestRequestedFieldsComplete();
        var createdAgenda = AgendaMock.allAgendaResponseRequestedFieldsComplete();

        var expectedLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(URL_PATCH.concat("/id/{id}"))
                .buildAndExpand(createdAgenda.getId())
                .toUri()
                .toString();

        when(agendaService.createAgenda(any(AgendaRequest.class))).thenReturn(createdAgenda);

        var result = mockMvc.perform(MockMvcRequestBuilders.post(URL_PATCH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(agendaRequest))).andReturn();

        assertEquals(expectedLocation, result.getResponse().getHeaderValue("Location").toString());
    }

    @Test
    public void createdAgendaTitleExceedsSize() throws Exception {
        var agendaRequest = AgendaMock.agendaRequestBigTitle();

        mockMvc.perform(MockMvcRequestBuilders.post(URL_PATCH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendaRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verifyNoInteractions(agendaService);
    }

    @Test
    public void createdAgendaTitleEmpty() throws Exception {
        var agendaRequest = AgendaMock.agendaRequestEmptyTitle();

        mockMvc.perform(MockMvcRequestBuilders.post(URL_PATCH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendaRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verifyNoInteractions(agendaService);
    }

    @Test
    public void createdAgendaDescriptionExceedsSize() throws Exception {
        var agendaRequest = AgendaMock.agendaRequestBigDescription();

        mockMvc.perform(MockMvcRequestBuilders.post(URL_PATCH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendaRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verifyNoInteractions(agendaService);
    }

    @Test
    public void createdAgendaDescriptionEmpty() throws Exception {
        var agendaRequest = AgendaMock.agendaRequestEmptyDescription();

        mockMvc.perform(MockMvcRequestBuilders.post(URL_PATCH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendaRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verifyNoInteractions(agendaService);
    }

    @Test
    public void findAllSucess() throws Exception {
        var createdAgendas = AgendaMock.agendaResponseCompleteList();

        when(agendaService.findAll()).thenReturn(createdAgendas);

        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATCH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(createdAgendas.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(createdAgendas.get(0).getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(createdAgendas.get(1).getId().intValue())));
    }

    @Test
    public void findAllEmptyList() throws Exception {
        when(agendaService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATCH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void findAgendaIdSucess() throws Exception {
        var id = 1L;
        var createdAgenda = AgendaMock.allAgendaResponseRequestedFieldsComplete();

        when(agendaService.findAgendaId(id)).thenReturn(createdAgenda);

        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATCH_ID,id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdAgenda.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(createdAgenda.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(createdAgenda.getDescription()));
    }

    @Test
    public void updateAgendaIdSuccess() throws Exception {
        var id = 1L;
        var agendaUpdateRequest = AgendaMock.allAgendaUpdateRequestFieldsComplete();
        var createdAgenda = AgendaMock.allAgendaResponseRequestedFieldsComplete();
        createdAgenda.setTitle(agendaUpdateRequest.getTitle());
        createdAgenda.setDescription(agendaUpdateRequest.getDescription());

        when(agendaService.updateAgendaId(any(Long.class), any(AgendaUpdateRequest.class))).thenReturn(createdAgenda);


        mockMvc.perform(MockMvcRequestBuilders.patch(URL_PATCH_ID,id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendaUpdateRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdAgenda.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(agendaUpdateRequest.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(agendaUpdateRequest.getDescription()));
    }

    @Test
    public void updateAgendaIdOnlyTitleSuccess() throws Exception {
        var id = 1L;
        var agendaUpdateRequest = AgendaMock.allAgendaUpdateRequestFieldTitle();
        var createdAgenda = AgendaMock.allAgendaResponseRequestedFieldsComplete();
        createdAgenda.setTitle(agendaUpdateRequest.getTitle());

        when(agendaService.updateAgendaId(any(Long.class), any(AgendaUpdateRequest.class))).thenReturn(createdAgenda);


        mockMvc.perform(MockMvcRequestBuilders.patch(URL_PATCH_ID,id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendaUpdateRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdAgenda.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(agendaUpdateRequest.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(createdAgenda.getDescription()));
    }

    @Test
    public void updateAgendaIdOnlyDescriptionSuccess() throws Exception {
        var id = 1L;
        var agendaUpdateRequest = AgendaMock.allAgendaUpdateRequestFieldDescription();
        var createdAgenda = AgendaMock.allAgendaResponseRequestedFieldsComplete();
        createdAgenda.setDescription(agendaUpdateRequest.getDescription());

        when(agendaService.updateAgendaId(any(Long.class), any(AgendaUpdateRequest.class))).thenReturn(createdAgenda);


        mockMvc.perform(MockMvcRequestBuilders.patch(URL_PATCH_ID,id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendaUpdateRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdAgenda.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(createdAgenda.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(agendaUpdateRequest.getDescription()));
    }

    @Test
    public void deleteByAgendaIdSuccess() throws Exception {
        var id = 1L;

        when(agendaService.deleteByAgendaId(eq(id))).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATCH_ID,id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void deleteByAgendaIdBadRequest() throws Exception {
        var id = 1L;;

        when(agendaService.deleteByAgendaId(eq(id))).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATCH_ID,id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void findResultAgendaSuccess() throws Exception {
        var id = 1L;
        var countVotesResponse = VoteMock.createdCountVotesResponse();

        when(voteService.countVotes(id)).thenReturn(countVotesResponse);

        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATCH.concat("/{id}/result"),id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idAgenda").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalVotesSim").value(countVotesResponse.getTotalVotesSim()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalVotesNao").value(countVotesResponse.getTotalVotesNao()));
    }
}
