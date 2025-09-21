package com.salessavvy.backend.repository;

import com.salessavvy.backend.entity.User;
import com.salessavvy.backend.enums.Role;
import com.salessavvy.backend.views.UserView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<UserView> findAllBy();
}