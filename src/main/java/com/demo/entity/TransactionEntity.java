package com.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    String transID;

    String order_status;

    String status;

    String bill_amt;

    String descriptor;

    String tdate;

    String bill_currency;

    String response;

    String reference;

    String mop;

    String ccno;

    String rrn ; 

    String upa;

    String authstatus;

    String authurl;

    String info;
}
