package br.com.assembly.web.controller;

import br.com.assembly.exception.CustomException;
import br.com.assembly.service.AgendaService;
import br.com.assembly.service.VoteService;
import br.com.assembly.web.dto.request.agenda.AgendaRequest;
import br.com.assembly.web.dto.request.agenda.AgendaUpdateRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/v1/agenda")
@AllArgsConstructor
@Slf4j
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    @PostMapping()
    public ResponseEntity<?> createAgenda(@RequestBody @NotNull @Valid AgendaRequest agendaRequest) throws CustomException {
        var createdAgenda = agendaService.createAgenda(agendaRequest);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/id/{id}")
                .buildAndExpand(createdAgenda.getId())
                .toUri();
        log.info("Agenda created successfully. Location: {}", location);
        return ResponseEntity.created(location).body(createdAgenda);
    }

    @GetMapping()
    public ResponseEntity<?> findAll() throws CustomException {
        var response = agendaService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> findAgendaId(@PathVariable("id") Long id) throws CustomException {
        var response = agendaService.findAgendaId(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/id/{id}")
    public ResponseEntity<?> updateAgendaId(@PathVariable("id") Long id, @RequestBody @NotNull AgendaUpdateRequest agendaUpdateRequest) throws CustomException {
        var response = agendaService.updateAgendaId(id, agendaUpdateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteByAgendaId(@PathVariable("id") Long id) throws CustomException {
        var response = agendaService.deleteByAgendaId(id);

        if(response) {
            log.info("Agenda deleted successfully");
            return ResponseEntity.noContent().build();
        }
        log.info("Agenda not found.");
        return ResponseEntity.badRequest().build();
    }
}
