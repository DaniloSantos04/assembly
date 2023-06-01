package br.com.assembly.domain.repository;

import br.com.assembly.domain.entity.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends MongoRepository<Vote, Long> {
    Vote findFirstByOrderByIdDesc();
    List<Vote> findByIdVotingSession(Long id);
    Vote findByIdAssociateAndIdVotingSession(Long idAssociate, Long idVotingSession);

}
