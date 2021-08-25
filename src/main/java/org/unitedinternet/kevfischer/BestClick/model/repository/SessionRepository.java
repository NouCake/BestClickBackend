package org.unitedinternet.kevfischer.BestClick.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.unitedinternet.kevfischer.BestClick.model.database.Session;

import java.util.Optional;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {

    Optional<Session> findBySession(String session);

}
