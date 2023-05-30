package br.com.assembly.web.controller;

import br.com.assembly.service.VotingSessionService;
import br.com.assembly.web.dto.request.votingsession.SessionUpdateRequest;
import br.com.assembly.web.dto.request.votingsession.VotingSessionRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/v1/voting-session")
@AllArgsConstructor
public class VotingSessionController {

    private static final String MESSAGE_NOT_FOUND = "Voting session not found for the id: ";

    @Autowired
    private VotingSessionService votingSessionService;

    @PostMapping()
    public ResponseEntity<?> createdVotingSession(@RequestBody @NotNull @Valid VotingSessionRequest votingSessionRequest) {
        var createdVotingSession = votingSessionService.createdVotingSession(votingSessionRequest);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/id/{id}")
                .buildAndExpand(createdVotingSession.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdVotingSession);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> findVotingSessionById(@PathVariable("id") Long id) {
        var response = votingSessionService.findVotingSessionById(id);
        if(response == null){
            return new ResponseEntity<>(MESSAGE_NOT_FOUND.concat(id.toString()), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/id/{id}")
    public ResponseEntity<?> updateVotingSession(@PathVariable("id") Long id, @RequestBody @NotNull SessionUpdateRequest sessionUpdateRequest){
        var response = votingSessionService.updateVotingSession(id, sessionUpdateRequest);
        if(response == null){
            return new ResponseEntity<>(MESSAGE_NOT_FOUND.concat(id.toString()), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(response);
    }
}
