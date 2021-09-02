package org.unitedinternet.kevfischer.BestClick.model.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.unitedinternet.kevfischer.BestClick.model.database.Session;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends CrudRepository<Session, UUID> {

    Optional<Session> findBySession(UUID session);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO user_sessions (expires, session_user_id, session) values (?, ?, ?)")
    void saveSession(Date expires, UUID userId, UUID session);

}
