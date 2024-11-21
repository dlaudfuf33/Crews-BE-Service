package org.crews.repository;

import org.crews.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Boolean existsByEmail(String email);

    // MyProfile용
    @Query("SELECT m FROM Member m " +
            "JOIN FETCH m.memberAndInterestings mai " +
            "JOIN FETCH mai.interesting interests " +
            "JOIN FETCH interests.subject " +
            "WHERE m.id = :id")
    Optional<Member> findByIdWithInterestings(@Param("id") Long id);

    // MyInfo용
    @Query("SELECT DISTINCT m FROM Member m " +
            "JOIN FETCH m.memberAndInterestings mai " +
            "JOIN FETCH mai.interesting " +
            "JOIN FETCH mai.interesting.subject " +
            "JOIN FETCH m.addresses " +
            "WHERE m.id = :id")
    Optional<Member> findByIdWithAddresses(@Param("id") Long id);
}
