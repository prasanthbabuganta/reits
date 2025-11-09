package com.reitsplatform.domain.repositories;

import com.reitsplatform.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.kycVerified = false")
    java.util.List<User> findPendingKycVerification();

    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :startDate")
    Long countNewUsers(java.time.LocalDateTime startDate);
}
