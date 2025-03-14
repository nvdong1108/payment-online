package com.demo.repository;


import com.demo.entity.Transactions;
import com.demo.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);

    Page<User> findByCreatedTimeBetween(LocalDateTime from, LocalDateTime to,Pageable pageable);
}