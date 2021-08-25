package org.unitedinternet.kevfischer.BestClick.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.unitedinternet.kevfischer.BestClick.model.database.UserProfile;

import java.util.UUID;

@Repository
public interface UserProfileRepository extends CrudRepository<UserProfile, UUID> {
}
