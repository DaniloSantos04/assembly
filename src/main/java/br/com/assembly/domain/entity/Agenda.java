package br.com.assembly.domain.entity;

import br.com.assembly.web.dto.request.agenda.AgendaRequest;
import br.com.assembly.web.dto.response.AgendaResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("agenda")
@Getter
@Setter
@Builder
public class Agenda {

    @Id
    private Long id;
    private String title;
    private String description;

    public static Agenda fromEntenty(AgendaRequest dto){
        return Agenda.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
    }

    public static Agenda fromEntenty(AgendaResponse dto){
        return Agenda.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
    }

    public AgendaResponse fromResponse(){
        return AgendaResponse.builder()
                .id(this.id)
                .title(this.title)
                .description(this.getDescription())
                .build();
    }
}
