package org.crews.repository;

import jakarta.transaction.Transactional;
import org.crews.model.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {
    @Transactional
    void deleteByRefresh(String refresh);

    @Transactional
    void deleteByUsername(String username);

    Boolean existsByRefresh(String refresh);
}
