package org.crews.repository;

import org.crews.dto.request.FindMemberRequest;
import org.crews.dto.response.FindMemberIdResponse;
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

    // 내 프로필 조회
    @Query("SELECT m FROM Member m " +
            "JOIN FETCH m.memberAndInterestings mai " +
            "JOIN FETCH mai.interesting interests " +
            "JOIN FETCH interests.subject " +
            "WHERE m.id = :id")
    Optional<Member> findByIdWithInterestings(@Param("id") Long id);

    // 내정보 조회(주소,관심사,주제)
    @Query("SELECT m FROM Member m " +
            "JOIN FETCH m.address " +
            "JOIN FETCH m.memberAndInterestings mai " +
            "JOIN FETCH mai.interesting " +
            "JOIN FETCH mai.interesting.subject " +
            "WHERE m.id = :id")
    Optional<Member> findByIdWithAddresses(@Param("id") Long id);

    //회원&주소 조회
    @Query("SELECT m FROM Member m " +
            "JOIN FETCH m.address " +
            "WHERE m.id = :id")
    Optional<Member> findByWithAddress(@Param("id") Long id);

    Optional<Member> findByCi(String ci);

    @Query("SELECT m FROM Member m WHERE m.name = :name AND m.phoneNumber = :phoneNumber")
    Optional<Member> findByNameAndPhoneNumber(@Param("name") String name, @Param("phoneNumber") String phoneNumber);

    @Query("SELECT m FROM Member m WHERE m.email = :email AND m.name = :name AND m.phoneNumber = :phoneNumber")
    Optional<Member> findByEmailAndNameAndPhoneNumber(@Param("email") String email, @Param("name") String name, @Param("phoneNumber") String phoneNumber);
}
