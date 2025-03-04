package com.demo.entity;





import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

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
public class Deposits {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "deposit_seq")
    @SequenceGenerator(name = "deposit_seq", sequenceName = "deposit_seq", allocationSize = 1)
    Long id;

    double amount;

    String currency;

    String username;

    String status;


    // @Temporal(TemporalType.TIMESTAMP)
    // @Column(nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    Date createdTime ;

    // @PrePersist
    // protected void onCreate() {
    //     if (createdTime == null) {
    //         createdTime = new Date(); 
    //     }
    // }
     

}
