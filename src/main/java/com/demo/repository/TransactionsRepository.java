package com.demo.repository;

import com.demo.entity.Transactions;

import java.time.LocalDateTime;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, String> {

    List<Transactions> findByTdateBetween(LocalDateTime dFrom, LocalDateTime dTo);

    Page<Transactions> findByTdateBetween(LocalDateTime dFrom, LocalDateTime dTo,Pageable pageable);

    List<Transactions> findByTransID(String transID );

    @Query("SELECT COALESCE(SUM(t.billAmt), 0) FROM Transactions t WHERE t.tdate BETWEEN :dFrom AND :dTo")
    Double getTotalBillAmt(@Param("dFrom") LocalDateTime dFrom, @Param("dTo") LocalDateTime dTo);


}