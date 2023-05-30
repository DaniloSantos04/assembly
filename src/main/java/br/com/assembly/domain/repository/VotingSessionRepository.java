package br.com.assembly.domain.repository;

import br.com.assembly.domain.entity.VotingSession;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VotingSessionRepository extends MongoRepository<VotingSession, Long> {
    VotingSession findFirstByOrderByIdDesc();
    List<VotingSession> findAllByActive(boolean active);
}
