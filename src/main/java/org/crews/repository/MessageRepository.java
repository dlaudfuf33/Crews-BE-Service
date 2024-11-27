package org.crews.repository;

import org.crews.model.Member;
import org.crews.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE m.phoneNumber = :phoneNumber")
    Optional<Message> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Modifying
    @Query("DELETE FROM Message m WHERE m.id = :messageId")
    void deleteMessage(@Param("messageId") Long messageId);
}
