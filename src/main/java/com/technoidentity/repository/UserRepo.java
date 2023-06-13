package com.technoidentity.repository;

import com.technoidentity.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
    List<User> findByStatusAndFirstNameContainingIgnoreCaseOrStatusAndMiddleNameContainingIgnoreCaseOrStatusAndLastNameContainingIgnoreCase(int i, String name, int i1, String name1, int i2, String name2);

    Page<User> findByStatus(Integer status, Pageable pageable);

    Page<User> findByStatusAndFirstNameContainingIgnoreCaseOrStatusAndMiddleNameContainingIgnoreCaseOrStatusAndLastNameContainingIgnoreCase(Integer status, String search, Integer status1, String search1, Integer status2, String search2, Pageable pageable);

    Page<User> findByFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String search, String search1, String search2, Pageable pageable);
}
