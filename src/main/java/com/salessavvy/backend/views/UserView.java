package com.salessavvy.backend.views;

import com.salessavvy.backend.enums.Role;

import java.time.LocalDateTime;

public interface UserView {
    Integer getId();

    String getUsername();

    String getEmail();

    Role getRole();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();
}
