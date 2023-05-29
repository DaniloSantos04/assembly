package br.com.assembly.mock;

import br.com.assembly.domain.entity.Agenda;
import br.com.assembly.web.dto.request.agenda.AgendaRequest;
import br.com.assembly.web.dto.request.agenda.AgendaUpdateRequest;
import br.com.assembly.web.dto.response.AgendaResponse;

import java.util.Arrays;
import java.util.List;

public class AgendaMock {

    public static AgendaRequest allAgendaRequestRequestedFieldsComplete(){
        return AgendaRequest.builder()
                .title("Title Agenda")
                .description("Description Agenda")
                .build();
    }

    public static AgendaResponse allAgendaResponseRequestedFieldsComplete(){
        return AgendaResponse.builder()
                .id(1L)
                .title("Title Agenda")
                .description("Description Agenda")
                .build();
    }

    public static AgendaRequest agendaRequestBigTitle(){
        return AgendaRequest.builder()
                .title("Lorem ipsum dolor sit amet, consectetuer adipiscing elit.")
                .description("Description Agenda")
                .build();
    }

    public static AgendaRequest agendaRequestEmptyTitle(){
        return AgendaRequest.builder()
                .title(" ")
                .description("Description Agenda")
                .build();
    }

    public static AgendaRequest agendaRequestBigDescription(){
        return AgendaRequest.builder()
                .title("Title Agenda")
                .description("Praesent in mauris eu tortor porttitor accumsan. Mauris suscipit, ligula sit amet pharetra semper, nibh ante cursus purus, vel sagittis velit mauris vel metus. Aenean fermentum risus id tortor. Integer imperdiet lectus quis justo. Integer tempor. Vivamus ac urna")
                .build();
    }

    public static AgendaRequest agendaRequestEmptyDescription(){
        return AgendaRequest.builder()
                .title("Title Agenda")
                .description(" ")
                .build();
    }

    public static List<AgendaResponse> agendaResponseCompleteList(){
        return  Arrays.asList(
                AgendaResponse.builder()
                        .id(1L)
                        .title("Title Agenda1")
                        .description("Description Agenda1")
                        .build(),
                AgendaResponse.builder()
                        .id(2L)
                        .title("Title Agenda2")
                        .description("Description Agenda2")
                        .build()
        );
    }

    public static AgendaUpdateRequest allAgendaUpdateRequestFieldsComplete(){
        return AgendaUpdateRequest.builder()
                .title("Title Agenda2")
                .description("Description Agenda2")
                .build();
    }

    public static AgendaUpdateRequest allAgendaUpdateRequestFieldTitle(){
        return AgendaUpdateRequest.builder()
                .title("Title Agenda2")
                .build();
    }

    public static AgendaUpdateRequest allAgendaUpdateRequestFieldDescription(){
        return AgendaUpdateRequest.builder()
                .description("Description Agenda2")
                .build();
    }

    public static Agenda allAgendaFieldsComplete(){
        return Agenda.builder()
                .id(1L)
                .title("Title Agenda")
                .description("Description Agenda")
                .build();
    }

    public static List<Agenda> agendaCompleteList(){
        return  Arrays.asList(
                Agenda.builder()
                        .id(1L)
                        .title("Title Agenda1")
                        .description("Description Agenda1")
                        .build(),
                Agenda.builder()
                        .id(2L)
                        .title("Title Agenda2")
                        .description("Description Agenda2")
                        .build()
        );
    }

    public static Agenda allAgendaUpdateFieldsComplete(){
        return Agenda.builder()
                .title("Title Agenda2")
                .description("Description Agenda2")
                .build();
    }


}
