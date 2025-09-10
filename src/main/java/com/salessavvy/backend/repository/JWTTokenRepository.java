package com.salessavvy.backend.repository;

import com.salessavvy.backend.entity.JWTToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JWTTokenRepository extends JpaRepository<JWTToken, Integer> {
    @Query("SELECT t FROM JWTToken t WHERE t.user.id = :userId")
    JWTToken findByUserId(@Param("userId") int userId);

}