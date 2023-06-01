package br.com.assembly.web.dto.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgendaResponse  implements Serializable {

    private Long id;
    private String title;
    private String description;
}
