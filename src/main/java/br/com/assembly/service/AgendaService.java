package br.com.assembly.service;

import br.com.assembly.domain.entity.Agenda;
import br.com.assembly.domain.repository.AgendaRepository;
import br.com.assembly.web.dto.request.agenda.AgendaRequest;
import br.com.assembly.web.dto.request.agenda.AgendaUpdateRequest;
import br.com.assembly.web.dto.response.AgendaResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AgendaService {

    private final AgendaRepository agendaRepository;

    public AgendaResponse createAgenda(AgendaRequest agendaRequest) {
        var lastAgenda = agendaRepository.findFirstByOrderByIdDesc();
        var maxId = lastAgenda != null ? lastAgenda.getId()+1 : 1;
        var agenda = Agenda.fromEntenty(agendaRequest);
        agenda.setId(maxId);
        var createdAgenda = agendaRepository.save(agenda);
        return createdAgenda.fromResponse();
    }

    public List<AgendaResponse> findAll() {
        var agendas = agendaRepository.findAll();
        return agendas.stream().map(Agenda::fromResponse).collect(Collectors.toList());
    }

    public AgendaResponse updateAgendaId(Long id, AgendaUpdateRequest agendaUpdateRequest) {
        var previousAgenda = findAgendaId(id);

        if(previousAgenda == null) return null;

        if(agendaUpdateRequest.getTitle() != null && !agendaUpdateRequest.getTitle().isEmpty()){
            previousAgenda.setTitle(agendaUpdateRequest.getTitle());
        }

        if(agendaUpdateRequest.getDescription() != null && !agendaUpdateRequest.getDescription().isEmpty()){
            previousAgenda.setDescription(agendaUpdateRequest.getDescription());
        }
        var createdAgenda = agendaRepository.save(Agenda.fromEntenty(previousAgenda));
        return createdAgenda.fromResponse();
    }

    public Boolean deleteByAgendaId(Long id) {
        var previousAgenda = findAgendaId(id);
        if(previousAgenda == null) return null;


        if(findAgendaId(id) != null){
            agendaRepository.deleteById(id);

        }
        return !agendaRepository.findById(id).isPresent();
    }

    public AgendaResponse findAgendaId(Long id) {
        var agendaOptional  =agendaRepository.findById(id);

        if (agendaOptional.isEmpty()) {
            return null;
        }

        return agendaOptional.get().fromResponse();
    }
}
