package br.com.assembly.web.controller;

import br.com.assembly.service.VoteService;
import br.com.assembly.web.dto.request.vote.VoteRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/v1/vote")
@AllArgsConstructor
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping()
    public ResponseEntity<?> registerVote(@RequestBody @NotNull @Valid VoteRequest voteRequest) {
        var voteResponse = voteService.registerVote(voteRequest);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/id/{id}")
                .buildAndExpand(voteResponse.getId())
                .toUri();
        return ResponseEntity.created(location).body(voteResponse);
    }
}
