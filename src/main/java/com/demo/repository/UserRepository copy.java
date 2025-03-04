package com.demo.repository;


import com.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LogPaymentRepository extends JpaRepository<LogPaymentEntity, String> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}