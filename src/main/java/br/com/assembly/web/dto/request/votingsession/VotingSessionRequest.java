package br.com.assembly.web.dto.request.votingsession;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ApiModel(description = "Voting Session Request")
public class VotingSessionRequest {

    @NotNull
    private Long idAgenda;

    @ApiModelProperty(value = "Start date and time in the format yyyy-MM-ddTHH:mm", example = "2023-05-29T19:42")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm")
    private LocalDateTime dateStart;

    @ApiModelProperty(value = "End date and time in the format yyyy-MM-ddTHH:mm", example = "2023-05-29T19:45")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm")
    private LocalDateTime dateEnd;

    @Builder.Default
    private boolean active = true;
}
