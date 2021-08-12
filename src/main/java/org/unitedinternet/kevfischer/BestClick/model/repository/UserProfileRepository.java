package org.unitedinternet.kevfischer.BestClick.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.unitedinternet.kevfischer.BestClick.model.database.UserProfile;

import java.util.UUID;

public interface UserProfileRepository extends CrudRepository<UserProfile, UUID> {
}
