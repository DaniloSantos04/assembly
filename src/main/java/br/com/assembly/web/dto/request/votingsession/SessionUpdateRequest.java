package br.com.assembly.web.dto.request.votingsession;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Voting Session Update")
public class SessionUpdateRequest {

    private Long idAgenda;

    @ApiModelProperty(value = "Start date and time in the format yyyy-MM-ddTHH:mm", example = "2023-05-29T19:42")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm")
    private LocalDateTime dateStart;

    @ApiModelProperty(value = "End date and time in the format yyyy-MM-ddTHH:mm", example = "2023-05-29T19:45")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm")
    private LocalDateTime dateEnd;

    private Boolean active;
}
