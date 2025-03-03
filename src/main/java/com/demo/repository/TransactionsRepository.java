package com.demo.repository;

import com.demo.entity.Transactions;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, String> {


    List<Transactions> findByTdateBetween(Date dFrom, Date dTo);

}