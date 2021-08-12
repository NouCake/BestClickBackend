package org.unitedinternet.kevfischer.BestClick.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.unitedinternet.kevfischer.BestClick.model.database.User;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {


}
