package com.demo.dto;


import java.util.Date;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepositDetailDto {

    String username;

    String currency;

    Double amount;

    String status;

    Date createdTime;

    String note;

    
}