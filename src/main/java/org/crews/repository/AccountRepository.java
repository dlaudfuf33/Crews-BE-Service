package org.crews.repository;

import org.crews.model.Account;
import org.crews.model.Member;
import org.crews.model.constants.AccountType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByFintecNumber(String fintecNumber);
    Optional<Account> findByIdAndFintecNumber(Long id, String fintecNumber);
    @EntityGraph(attributePaths = {"member"})
    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByMemberAndAccountType(Member member, AccountType accountType);
}
