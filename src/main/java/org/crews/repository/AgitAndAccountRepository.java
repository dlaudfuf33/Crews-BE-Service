package org.crews.repository;

import org.crews.model.Account;
import org.crews.model.Agit;
import org.crews.model.AgitAndAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgitAndAccountRepository extends JpaRepository<AgitAndAccount, Long> {
    Optional<AgitAndAccount> findByAgitAndAccount(Agit agit, Account account);

    @Query("""
        SELECT aa FROM AgitAndAccount aa
        JOIN FETCH aa.account acc
        JOIN FETCH aa.agit agit
        WHERE acc.id = :accountId
    """)
    Optional<AgitAndAccount> findByAccountIdWithAgit(@Param("accountId") Long accountId);

    Optional<AgitAndAccount> findByAgit(Agit agit);

}
