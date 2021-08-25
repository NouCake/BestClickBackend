package org.unitedinternet.kevfischer.BestClick.model.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.unitedinternet.kevfischer.BestClick.model.database.UserAppData;
import org.unitedinternet.kevfischer.BestClick.model.database.UserProfile;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserAppRepository extends CrudRepository<UserAppData, UUID> {

    List<UserAppData> findAll(Pageable pageable);

}
