package com.demo.repository;


import com.demo.entity.Deposits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DepositsRepository extends JpaRepository<Deposits,Long> {
    
    List<Deposits> findByCreatedTimeBetween(Date startDate, Date endDate);

}