package org.crews.repository;

import org.crews.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByFintecNumber(String fintecNumber);
    Optional<Account> findByIdAndFintecNumber(Long id, String fintecNumber);
}
