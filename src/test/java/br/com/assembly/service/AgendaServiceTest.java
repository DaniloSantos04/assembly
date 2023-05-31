package br.com.assembly.service;

import br.com.assembly.domain.entity.Agenda;
import br.com.assembly.domain.repository.AgendaRepository;
import br.com.assembly.exception.CustomException;
import br.com.assembly.mock.AgendaMock;
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
public class AgendaServiceTest {

    @Mock
    private AgendaRepository agendaRepository;

    @InjectMocks
    private AgendaService agendaService;

    @Test
    public void successfullyCreatedFirstAgenda() throws CustomException {
        var agendaRequest = AgendaMock.allAgendaRequestRequestedFieldsComplete();
        var savedAgenda =  AgendaMock.allAgendaFieldsComplete();
        var rsponse = AgendaMock.allAgendaResponseRequestedFieldsComplete();

        when(agendaRepository.findFirstByOrderByIdDesc()).thenReturn(null);
        when(agendaRepository.save(any(Agenda.class))).thenReturn(savedAgenda);

        var createdAgenda = agendaService.createAgenda(agendaRequest);

        assertEquals(rsponse.getId(), createdAgenda.getId());
        assertEquals(rsponse.getTitle(), createdAgenda.getTitle());
        assertEquals(rsponse.getDescription(), createdAgenda.getDescription());

        verify(agendaRepository, times(1)).findFirstByOrderByIdDesc();
        verify(agendaRepository, times(1)).save(any(Agenda.class));
    }

    @Test
    public void successfullyCreatedAnotherAgenda() throws CustomException {
        var agendaRequest = AgendaMock.allAgendaRequestRequestedFieldsComplete();
        var firstAgenda =  AgendaMock.allAgendaFieldsComplete();
        var savedAgenda =  AgendaMock.allAgendaFieldsComplete();
        savedAgenda.setId(2L);
        var response = AgendaMock.allAgendaResponseRequestedFieldsComplete();
        response.setId(2L);

        when(agendaRepository.findFirstByOrderByIdDesc()).thenReturn(firstAgenda);
        when(agendaRepository.save(any(Agenda.class))).thenReturn(savedAgenda);

        var createdAgenda = agendaService.createAgenda(agendaRequest);

        assertEquals(response.getId(), createdAgenda.getId());
        assertEquals(response.getTitle(), createdAgenda.getTitle());
        assertEquals(response.getDescription(), createdAgenda.getDescription());

        verify(agendaRepository, times(1)).findFirstByOrderByIdDesc();
        verify(agendaRepository, times(1)).save(any(Agenda.class));
    }

    @Test
    public void findAllWithListContainingValues() throws CustomException {
        var agendaList = AgendaMock.agendaCompleteList();
        var responseList = AgendaMock.agendaResponseCompleteList();

        when(agendaRepository.findAll()).thenReturn(agendaList);

        var response = agendaService.findAll();

        assertEquals(responseList.size(), response.size());
        assertEquals(responseList.get(0).getId(), response.get(0).getId());
        assertEquals(responseList.get(0).getTitle(), response.get(0).getTitle());
        assertEquals(responseList.get(0).getDescription(), response.get(0).getDescription());
        assertEquals(responseList.get(1).getId(), response.get(1).getId());
        assertEquals(responseList.get(1).getTitle(), response.get(1).getTitle());
        assertEquals(responseList.get(1).getDescription(), response.get(1).getDescription());
        verify(agendaRepository, times(1)).findAll();
    }

    @Test
    public void findAllWithEmptyList() throws CustomException {
        when(agendaRepository.findAll()).thenReturn(Collections.emptyList());

        var response = agendaService.findAll();

        assertEquals(0, response.size());
        verify(agendaRepository, times(1)).findAll();
    }

    @Test
    public void findAgendaIdSuccess() throws CustomException {
        var id = 1L;
        var agenda = AgendaMock.allAgendaFieldsComplete();
        var agendaResponse = AgendaMock.allAgendaResponseRequestedFieldsComplete();

        when(agendaRepository.findById(eq(id))).thenReturn(Optional.of(agenda));

        var response = agendaService.findAgendaId(id);

        assertNotNull(response);
        assertEquals(agendaResponse.getId(), response.getId());
        assertEquals(agendaResponse.getTitle(), response.getTitle());
        assertEquals(agendaResponse.getDescription(), response.getDescription());

        verify(agendaRepository, times(1)).findById(eq(id));
    }

    @Test
    public void findAgendaIdEmpty() {
        var id = 1L;
        try {
            when(agendaRepository.findById(eq(id))).thenReturn(Optional.empty());

            agendaService.findAgendaId(id);
        } catch (CustomException e) {
            assertEquals("Agenda does not exist for the id: 1", e.getMessage());
            assertEquals(404, e.getHttpStatus());
        }

        verify(agendaRepository, times(1)).findById(eq(id));
    }

    @Test
    public void deleteByAgendaIdSuccess() throws CustomException {
        var id = 1L;
        var agenda = AgendaMock.allAgendaFieldsComplete();

        when(agendaRepository.findById(eq(id))).thenReturn(Optional.of(agenda));
        doNothing().when(agendaRepository).deleteById(eq(id));

        agendaService.deleteByAgendaId(id);

        verify(agendaRepository, times(1)).deleteById(eq(id));
    }

    @Test
    public void deleteByAgendaIdNotExecuted() throws CustomException {
        var id = 1L;
        try {

            when(agendaRepository.findById(eq(id))).thenReturn(Optional.empty());

            var result = agendaService.deleteByAgendaId(id);

        } catch (CustomException e) {
            assertEquals("Agenda does not exist for the id: 1", e.getMessage());
            assertEquals(404, e.getHttpStatus());
        }

        verify(agendaRepository, times(1)).findById(eq(id));
        verify(agendaRepository, never()).deleteById(eq(id));
    }

    @Test
    public void updateAgendaIdNotExecuted() throws CustomException {
        Long id = 1L;
        try {
            var agendaUpdateRequest = AgendaMock.allAgendaUpdateRequestFieldsComplete();

            when(agendaRepository.findById(eq(id))).thenReturn(Optional.empty());

            agendaService.updateAgendaId(id, agendaUpdateRequest);

        } catch (CustomException e) {
            assertEquals("Agenda does not exist for the id: 1", e.getMessage());
            assertEquals(404, e.getHttpStatus());
        }

        verify(agendaRepository, times(1)).findById(eq(id));
        verify(agendaRepository, never()).save(any(Agenda.class));
    }

    @Test
    public void updateAgendaIdCompleteSuccess() throws CustomException {
        var id = 1L;
        var agendaUpdateRequest = AgendaMock.allAgendaUpdateRequestFieldsComplete();
        var agenda = AgendaMock.allAgendaFieldsComplete();
        var updatedAgenda= AgendaMock.allAgendaUpdateFieldsComplete();
        updatedAgenda.setId(id);
        var agendaResponse = AgendaMock.allAgendaResponseRequestedFieldsComplete();
        agendaResponse.setTitle(updatedAgenda.getTitle());
        agendaResponse.setDescription(updatedAgenda.getDescription());

        when(agendaRepository.findById(eq(id))).thenReturn(Optional.of(agenda));
        when(agendaRepository.save(any(Agenda.class))).thenReturn(updatedAgenda);

        var response = agendaService.updateAgendaId(id, agendaUpdateRequest);

        assertNotNull(response);
        assertEquals(agendaResponse.getId(), response.getId());
        assertEquals(agendaResponse.getTitle(), response.getTitle());
        assertEquals(agendaResponse.getDescription(), response.getDescription());
        verify(agendaRepository, times(1)).findById(eq(id));
        verify(agendaRepository, times(1)).save(any(Agenda.class));
    }

    @Test
    public void updateAgendaIdTitleSuccess() throws CustomException {
        var id = 1L;
        var agendaUpdateRequest = AgendaMock.allAgendaUpdateRequestFieldsComplete();
        agendaUpdateRequest.setDescription("");
        var agenda = AgendaMock.allAgendaFieldsComplete();
        var updatedAgenda= AgendaMock.allAgendaUpdateFieldsComplete();
        updatedAgenda.setId(id);
        updatedAgenda.setDescription(agenda.getDescription());
        var agendaResponse = AgendaMock.allAgendaResponseRequestedFieldsComplete();
        agendaResponse.setTitle(updatedAgenda.getTitle());
        agendaResponse.setDescription(updatedAgenda.getDescription());

        when(agendaRepository.findById(eq(id))).thenReturn(Optional.of(agenda));
        when(agendaRepository.save(any(Agenda.class))).thenReturn(updatedAgenda);

        var response = agendaService.updateAgendaId(id, agendaUpdateRequest);

        assertNotNull(response);
        assertEquals(agendaResponse.getId(), response.getId());
        assertEquals(agendaResponse.getTitle(), response.getTitle());
        assertEquals(agendaResponse.getDescription(), response.getDescription());
        verify(agendaRepository, times(1)).findById(eq(id));
        verify(agendaRepository, times(1)).save(any(Agenda.class));
    }

    @Test
    public void updateAgendaIdDescriptionSuccess() throws CustomException {
        var id = 1L;
        var agendaUpdateRequest = AgendaMock.allAgendaUpdateRequestFieldsComplete();
        agendaUpdateRequest.setTitle("");
        var agenda = AgendaMock.allAgendaFieldsComplete();
        var updatedAgenda= AgendaMock.allAgendaUpdateFieldsComplete();
        updatedAgenda.setId(id);
        updatedAgenda.setTitle(agenda.getTitle());
        var agendaResponse = AgendaMock.allAgendaResponseRequestedFieldsComplete();
        agendaResponse.setTitle(updatedAgenda.getTitle());
        agendaResponse.setDescription(updatedAgenda.getDescription());

        when(agendaRepository.findById(eq(id))).thenReturn(Optional.of(agenda));
        when(agendaRepository.save(any(Agenda.class))).thenReturn(updatedAgenda);

        var response = agendaService.updateAgendaId(id, agendaUpdateRequest);

        assertNotNull(response);
        assertEquals(agendaResponse.getId(), response.getId());
        assertEquals(agendaResponse.getTitle(), response.getTitle());
        assertEquals(agendaResponse.getDescription(), response.getDescription());
        verify(agendaRepository, times(1)).findById(eq(id));
        verify(agendaRepository, times(1)).save(any(Agenda.class));
    }
}
