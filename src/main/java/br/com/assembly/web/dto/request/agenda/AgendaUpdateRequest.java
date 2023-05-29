package br.com.assembly.web.dto.request.agenda;

import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgendaUpdateRequest {

    @Size(max=50)
    private String title;

    @Size(max=250)
    private String description;
}
