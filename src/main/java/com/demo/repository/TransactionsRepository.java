package com.demo.repository;

import com.demo.entity.Transactions;

import java.time.LocalDateTime;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, String> {


    Page<Transactions> findByTdateBetween(LocalDateTime dFrom, LocalDateTime dTo,Pageable pageable);

    List<Transactions> findByTransID(String transID );

}