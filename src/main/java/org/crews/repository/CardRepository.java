package org.crews.repository;

import org.crews.model.Account;
import org.crews.model.Card;
import org.crews.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByAccountAndMemberAndIsDeletedFalse(Account account, Member member);
}
