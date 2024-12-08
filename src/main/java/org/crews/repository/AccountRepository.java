package org.crews.repository;

import org.crews.dto.response.AccountsResponse;
import org.crews.model.Account;
import org.crews.model.Member;
import org.crews.model.constants.AccountType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByFintecNumber(String fintecNumber);

    Optional<Account> findByIdAndFintecNumber(Long id, String fintecNumber);

    @EntityGraph(attributePaths = {"member", "member.membership"})
    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByMemberAndAccountType(Member member, AccountType accountType);

    @Query("SELECT new org.crews.dto.response.AccountsResponse( " +
            "a.id, " +
            "ab.bankImage, " +
            "ab.bankCode, " +
            "a.productName, " +
            "a.maskedAccountNumber, " +
            "a.balance) " +
            "FROM Account a " +
            "JOIN a.bank ab " +
            "WHERE a.member.id = :memberId AND " +
            "a.accountType = 'PERSONAL' " +
            "ORDER BY a.createdAt DESC")
    List<AccountsResponse> findPersonalAccounts(@Param("memberId") Long memberId);

    @Query("SELECT a FROM Account a WHERE a.member.id = :memberId ORDER BY a.createdAt DESC")
    List<Account> findAccountsByMemberId(@Param("memberId") Long memberId);

    @Query(" SELECT a FROM Account a JOIN a.member m WHERE a.id = :accountId AND a.member.id = :memberId")
    Optional<Account> findByIdAndMemberId(@Param("accountId") Long accountId, @Param("memberId") Long memberId);

    boolean existsByMemberIdAndAccountNumber(Long memberId, String accountNumber);
}
