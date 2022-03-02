package com.sip.ams.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sip.ams.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	Role findByRole(String role);

}
