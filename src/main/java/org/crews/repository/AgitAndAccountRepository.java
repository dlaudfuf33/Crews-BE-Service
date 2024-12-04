package org.crews.repository;

import org.crews.model.Account;
import org.crews.model.Agit;
import org.crews.model.AgitAndAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgitAndAccountRepository extends JpaRepository<AgitAndAccount, Long> {
    Optional<AgitAndAccount> findByAgitAndAccount(Agit agit, Account account);

    Optional<AgitAndAccount> findByAgit(Agit agit);

}
