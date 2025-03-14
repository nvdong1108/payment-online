package com.demo.entity;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "transactions")
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    
    String transID;

    @Column(name = "order_status")
    int orderStatus;

    String status;

    @Column(name = "bill_amt")
    double billAmt;

    String descriptor;

    @Column(name = "tdate", columnDefinition = "TIMESTAMP")
    LocalDateTime tdate;
    

    @Column(name = "bill_currency")
    String billCurrency;

    String response;

    String reference;

    String mop;

    String ccno;

    String rrn;

    String upa;

    String authstatus;

    String authurl;

    @Column(columnDefinition = "LONGTEXT" )
    String info;

    String username;

    @Column(length = 1000)
    String note;

    @Column(name = "work_flow")
    String workFlow;

    @PrePersist
    public void prePersist() {
        if (this.workFlow == null) {
            this.workFlow = "pending";
        }
    }




}