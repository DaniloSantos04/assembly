package br.com.assembly.web.controller;

import br.com.assembly.service.AgendaService;
import br.com.assembly.web.dto.request.agenda.AgendaRequest;
import br.com.assembly.web.dto.request.agenda.AgendaUpdateRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/v1/agenda")
@AllArgsConstructor
public class AgendaController {

    private static final String MESSAGE_AGENDA_NOT_FOUND = "Agenda not found for the id: ";
    @Autowired
    private AgendaService agendaService;

    @PostMapping()
    public ResponseEntity<?> createAgenda(@RequestBody @NotNull @Valid AgendaRequest agendaRequest) {
        var createdAgenda = agendaService.createAgenda(agendaRequest);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/id/{id}")
                .buildAndExpand(createdAgenda.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdAgenda);
    }

    @GetMapping()
    public ResponseEntity<?> findAll(){
        var response = agendaService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> findAgendaId(@PathVariable("id") Long id) {
        var response = agendaService.findAgendaId(id);
        if(response == null){
            return new ResponseEntity<>(MESSAGE_AGENDA_NOT_FOUND.concat(id.toString()), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/id/{id}")
    public ResponseEntity<?> updateAgendaId(@PathVariable("id") Long id, @RequestBody @NotNull AgendaUpdateRequest agendaUpdateRequest){
        var response = agendaService.updateAgendaId(id, agendaUpdateRequest);
        if(response == null){
            return new ResponseEntity<>(MESSAGE_AGENDA_NOT_FOUND.concat(id.toString()), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteByAgendaId(@PathVariable("id") Long id) {
        var response = agendaService.deleteByAgendaId(id);

        if(response == null) {
            return new ResponseEntity<>(MESSAGE_AGENDA_NOT_FOUND.concat(id.toString()), HttpStatus.NOT_FOUND);
        }

        if(response) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
