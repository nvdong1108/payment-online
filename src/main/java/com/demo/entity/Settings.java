package com.demo.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;




@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "email_send")
    String emailSend;

    @Column(name = "password_send")
    String passwordSend;

    @Column(name = "email_receive")
    String emailReceive;

    @Column(name = "public_key")
    String publicKey;

    @Column(name = "ter_no")
    String terNo;

    @Column(name = "update_time")
    Date upDateTime;
}
