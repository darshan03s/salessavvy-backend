package com.salessavvy.backend.repository;

import com.salessavvy.backend.entity.JWTToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface JWTTokenRepository extends JpaRepository<JWTToken, Integer> {
    @Query("SELECT t FROM JWTToken t WHERE t.user.id = :userId")
    JWTToken findByUserId(@Param("userId") int userId);

    @Modifying
    @Transactional
    void deleteByUserId(Integer userId);

    Optional<JWTToken> findByToken(String token);
}
