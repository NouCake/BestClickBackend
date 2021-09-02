package org.unitedinternet.kevfischer.BestClick.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.unitedinternet.kevfischer.BestClick.model.redis.Session;

import java.util.UUID;

public interface RSessionRepository extends CrudRepository<Session, UUID> {



}
