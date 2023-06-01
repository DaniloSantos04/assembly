package br.com.assembly.web.dto.request.vote;

import br.com.assembly.enums.VoteEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Vote")
public class VoteRequest {

    @ApiModelProperty(value = "Associate id", example = "1")
    @NotNull
    private Long idAssociate;

    @ApiModelProperty(value = "Voting session id", example = "1")
    @NotNull
    private Long idVotingSession;

    @ApiModelProperty(value = "Vote", example = "SIM/NAO")
    private VoteEnum vote;
}
