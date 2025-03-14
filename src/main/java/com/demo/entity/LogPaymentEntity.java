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
@Table(name = "log_payment")
public class LogPaymentEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "log_payment_seq")
    @SequenceGenerator(name = "log_payment_seq", sequenceName = "log_payment_seq", allocationSize = 1)
    Long id;

    @Column(name = "username")
    String username;

    @Column(name = "amount")
    String amount;  
    
    @Column(name = "status")
    String status;

    @Column(name = "transaction_id")
    String transactionId;

    @Column(name = "created_time")
    String createdTime;


}
