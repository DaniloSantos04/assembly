package br.com.assembly.web.dto.request.agenda;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AgendaRequest {

    @NotBlank
    @Size(min=1,max=50)
    private String title;

    @Size(min=1,max=250)
    @NotBlank
    private String description;
}
