package org.unitedinternet.kevfischer.BestClick.model.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.unitedinternet.kevfischer.BestClick.model.database.UserAppData;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserAppRepository extends CrudRepository<UserAppData, UUID> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_app SET counter = counter + ? WHERE app_user_id = ?", nativeQuery = true)
    int incrCounter(int value, String uuid);

    List<UserAppData> findAll(Pageable pageable);

}
