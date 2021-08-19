package org.unitedinternet.kevfischer.BestClick.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.unitedinternet.kevfischer.BestClick.model.database.UserAuthData;

import java.util.Optional;
import java.util.UUID;

public interface UserAuthRepository extends CrudRepository<UserAuthData, UUID> {

    Optional<UserAuthData> findByUsername(String username);

}
