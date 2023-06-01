package br.com.assembly.web.controller;

import br.com.assembly.exception.CustomException;
import br.com.assembly.service.VoteService;
import br.com.assembly.service.VotingSessionService;
import br.com.assembly.web.dto.request.votingsession.SessionUpdateRequest;
import br.com.assembly.web.dto.request.votingsession.VotingSessionRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class VotingSessionController {

    private static final String MESSAGE_NOT_FOUND = "Voting session not found for the id: ";

    @Autowired
    private VotingSessionService votingSessionService;

    @Autowired
    private VoteService voteService;

    @PostMapping()
    public ResponseEntity<?> createdVotingSession(@RequestBody @NotNull @Valid VotingSessionRequest votingSessionRequest) throws CustomException {
        var createdVotingSession = votingSessionService.createdVotingSession(votingSessionRequest);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/id/{id}")
                .buildAndExpand(createdVotingSession.getId())
                .toUri();
        log.info("Voting session created successfully. Location: {}", location);
        return ResponseEntity.created(location).body(createdVotingSession);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> findVotingSessionById(@PathVariable("id") Long id) throws CustomException {
        var response = votingSessionService.findVotingSessionById(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/id/{id}")
    public ResponseEntity<?> updateVotingSession(@PathVariable("id") Long id, @RequestBody @NotNull SessionUpdateRequest sessionUpdateRequest) throws CustomException {
        var response = votingSessionService.updateVotingSession(id, sessionUpdateRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/result")
    public ResponseEntity<?> findResultAgenda(@PathVariable("id") Long id) throws CustomException {
        var response = voteService.countVotes(id);
        return ResponseEntity.ok(response);
    }
}
