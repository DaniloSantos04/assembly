package br.com.assembly.web.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgendaResponse {

    private Long id;
    private String title;
    private String description;
}
