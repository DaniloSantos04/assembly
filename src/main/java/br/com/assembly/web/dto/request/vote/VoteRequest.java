package br.com.assembly.web.dto.request.vote;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Vote")
public class VoteRequest {

    @NotNull
    private Long idAssociate;

    @NotNull
    private Long idAgenda;

    private boolean vote;
}
