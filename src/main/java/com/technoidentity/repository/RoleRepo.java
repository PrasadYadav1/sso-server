package com.technoidentity.repository;

import com.technoidentity.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRepo extends JpaRepository<Role, UUID> {

    Role findByNameIgnoreCase(String name);

    List<Role> findByNameContainingIgnoreCase(String name);

    Page<Role> findByNameContainingIgnoreCase(String name,  Pageable pageable);

    Page<Role> findByStatus(Integer status, Pageable pageable);

    Page<Role> findByStatusAndNameContainingIgnoreCase(Integer status, String search,  Pageable pageable);

    }
