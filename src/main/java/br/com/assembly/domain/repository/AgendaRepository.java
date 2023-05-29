package br.com.assembly.domain.repository;

import br.com.assembly.domain.entity.Agenda;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepository extends MongoRepository<Agenda, Long> {
    Agenda findFirstByOrderByIdDesc();
}
