package br.com.assembly.domain.entity;

import br.com.assembly.web.dto.request.vote.VoteRequest;
import br.com.assembly.web.dto.response.VoteResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("vote")
@Getter
@Setter
@Builder
public class Vote {

    @Id
    private Long id;
    private Long idAssociate;
    private Long idAgenda;
    private boolean vote;

    public static Vote fromEntenty(VoteRequest dto){
        return Vote.builder()
                .idAssociate(dto.getIdAssociate())
                .idAgenda(dto.getIdAgenda())
                .vote(dto.isVote())
                .build();
    }

    public VoteResponse fromResponse(){
        return VoteResponse.builder()
                .id(this.id)
                .idAssociate(this.idAssociate)
                .idAgenda(this.idAgenda)
                .vote(this.vote)
                .build();
    }
}
