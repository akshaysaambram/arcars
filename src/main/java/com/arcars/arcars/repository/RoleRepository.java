package com.arcars.arcars.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arcars.arcars.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
