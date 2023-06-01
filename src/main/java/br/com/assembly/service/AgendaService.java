package br.com.assembly.service;

import br.com.assembly.domain.entity.Agenda;
import br.com.assembly.domain.repository.AgendaRepository;
import br.com.assembly.exception.CustomException;
import br.com.assembly.web.dto.request.agenda.AgendaRequest;
import br.com.assembly.web.dto.request.agenda.AgendaUpdateRequest;
import br.com.assembly.web.dto.response.AgendaResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AgendaService {

    private final AgendaRepository agendaRepository;

    @CachePut(value = "agendas", key = "#agendaRequest.description")
    public AgendaResponse createAgenda(AgendaRequest agendaRequest) throws CustomException {
        log.info("Starting the createAgenda method. Parameters: agendaRequest={}", agendaRequest);
        try{
            var lastAgenda = agendaRepository.findFirstByOrderByIdDesc();
            log.info("Database query returned lastAgenda={}", lastAgenda);

            var maxId = lastAgenda != null ? lastAgenda.getId()+1 : 1;
            var agenda = Agenda.fromEntenty(agendaRequest);
            agenda.setId(maxId);
            log.info("Saving vote in the database with parameters={}", agenda);
            var createdAgenda = agendaRepository.save(agenda);
            return createdAgenda.fromResponse();

        } catch (Exception e) {
            log.error("An unexpected error occurred: {}, cause: {}", e.getMessage(), e.getCause());
            throw new CustomException("We encountered an unexpected error.", 500);
        }
    }

    @Cacheable("agendas")
    public List<AgendaResponse> findAll() throws CustomException {
        try{
            log.info("Starting the findAll method.");
            log.info("Querying the list of agenda in the database");
            var agendas = agendaRepository.findAll();
            return agendas.stream().map(Agenda::fromResponse).collect(Collectors.toList());

        } catch (Exception e) {
            log.error("An unexpected error occurred: {}, cause: {}", e.getMessage(), e.getCause());
            throw new CustomException("We encountered an unexpected error.", 500);
        }
    }

    @CachePut(value = "agenda", key = "#id")
    public AgendaResponse updateAgendaId(Long id, AgendaUpdateRequest agendaUpdateRequest) throws CustomException {
        try{
            log.info("Starting the updateAgendaId method. Parameters: id={} and agendaUpdateRequest={}", id, agendaUpdateRequest);
            var previousAgenda = findAgendaId(id);

            if(agendaUpdateRequest.getTitle() != null && !agendaUpdateRequest.getTitle().isEmpty()){
                log.info("Updating title to={}", agendaUpdateRequest.getTitle());
                previousAgenda.setTitle(agendaUpdateRequest.getTitle());
            }

            if(agendaUpdateRequest.getDescription() != null && !agendaUpdateRequest.getDescription().isEmpty()){
                log.info("Updating description to={}", agendaUpdateRequest.getDescription());
                previousAgenda.setDescription(agendaUpdateRequest.getDescription());
            }
            log.info("Saving agenda in database with parameters={}", previousAgenda);
            var createdAgenda = agendaRepository.save(Agenda.fromEntenty(previousAgenda));
            return createdAgenda.fromResponse();

        } catch (CustomException e) {
            log.error("A CustomException occurred: {}", e.getMessage());
            throw new CustomException(e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}, cause: {}", e.getMessage(), e.getCause());
            throw new CustomException("We encountered an unexpected error.", 500);
        }
    }

    @CacheEvict(value = "agenda", key = "#id")
    public Boolean deleteByAgendaId(Long id) throws CustomException {
        try{
            log.info("Starting the deleteByAgendaId method. Parameters: id={}", id);
            findAgendaId(id);

            log.info("Deleting agenda in the database with parameters={}", id);
            agendaRepository.deleteById(id);

            log.info("Querying the agenda in the database with the parameters={}", id);
            return !agendaRepository.findById(id).isPresent();

        } catch (CustomException e) {
            log.error("A CustomException occurred: {}", e.getMessage());
            throw new CustomException(e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}, cause: {}", e.getMessage(), e.getCause());
            throw new CustomException("We encountered an unexpected error.", 500);
        }
    }

    @Cacheable("agenda")
    public AgendaResponse findAgendaId(Long id) throws CustomException {
        try{

            log.info("Starting the findAgendaId method. Parameters: idAgenda={}", id);
            log.info("Querying agenda in the database with parameters={}", id);
            var agendaOptional  =agendaRepository.findById(id);

            if (agendaOptional.isEmpty()) {
                throw new CustomException("Agenda does not exist for the id: ".concat(id.toString()), 404);
            }

            return agendaOptional.get().fromResponse();

        } catch (CustomException e) {
            log.error("A CustomException occurred: {}", e.getMessage());
            throw new CustomException(e.getMessage(), e.getHttpStatus());
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}, cause: {}", e.getMessage(), e.getCause());
            throw new CustomException("We encountered an unexpected error.", 500);
        }
    }
}
