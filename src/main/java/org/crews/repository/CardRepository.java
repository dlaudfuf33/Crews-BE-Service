package org.crews.repository;

import org.crews.dto.response.AgitCardsResponse;
import org.crews.model.Account;
import org.crews.model.Card;
import org.crews.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByAccountAndMemberAndIsDeletedFalse(Account account, Member member);

    Optional<Card> findByCardNumberAndIsDeletedFalse(String cardNumber);

    @Query("SELECT new org.crews.dto.response.AgitCardsResponse(" +
            "c.id, " +
            "c.cardImage, " +
            "c.cardName, " +
            "SUBSTRING(c.maskedCardNumber, 1, 4), " +
            "a.agitName) " +
            "FROM Card c " +
            "JOIN c.account acc " +
            "JOIN acc.agitAndAccount aa " +
            "JOIN aa.agit a " +
            "WHERE c.member.id = :memberId AND c.isDeleted <> true")
    List<AgitCardsResponse> findAgitCardsByMemberId(@Param("memberId") Long memberId);


    Optional<Card> findByIdAndMemberId(Long id, Long memberId);

    Optional<Card> findByMemberId(Long memberId);

    Optional<Card> findByCardNumberAndMemberId(String cardNumber, Long memberId);
}
