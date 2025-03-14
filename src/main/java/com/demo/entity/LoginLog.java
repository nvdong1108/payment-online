package com.demo.entity;

import java.util.Date;

import org.hibernate.annotations.Collate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "login_log")
public class LoginLog {

    @Id
    @GeneratedValue(strategy  = GenerationType.IDENTITY)
    Long id; 

    String username;

    @Column(name= "ip_address")
    String ipAddress;

    @Column(name= "login_time")
    Date loginTime ; 

    @Column(name= "user_agent")
    String userAgent;


    public LoginLog(String username, String ipAddress, Date loginTime, String userAgent) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.loginTime = loginTime;
        this.userAgent = userAgent;
    }

}
