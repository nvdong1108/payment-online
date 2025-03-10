package com.demo.controller.api;

import com.demo.config.JwtUtil;
import com.demo.dto.DepositDetailDto;
import com.demo.entity.Deposits;
import com.demo.entity.Transactions;
import com.demo.repository.DepositsRepository;
import com.demo.repository.TransactionsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiDashboardController {


    @Autowired
    private DepositsRepository depositsRepository;

    @Autowired
    private TransactionsRepository transactionsRepository;

    @GetMapping("/dashboard/getDepositDetails/{id}")
    public DepositDetailDto getDepositDetails(@PathVariable Long id) {
         String transId = String.valueOf(id);
        List<Transactions> listTransactions = transactionsRepository.findByTransID(transId);
        DepositDetailDto depositDetailDto = new DepositDetailDto();
        if (listTransactions!=null && !listTransactions.isEmpty()) {
        Transactions transaction = listTransactions.get(0);  
        ObjectMapper objectMapper = new ObjectMapper();
        double amount = transaction.getBillAmt();
        String currency = transaction.getBillCurrency();  
        String status = transaction.getStatus();
        Date createdTime = new Date();// transaction.getTdate(); 
        String jsonInfo = transaction.getInfo();
            try {
                Map<String, Object> map = objectMapper.readValue(jsonInfo, Map.class);
                String fullName = (String) map.get("fullname");
                depositDetailDto.setUsername(fullName==null?"":fullName);
            } catch (Exception e) {
                e.printStackTrace();
            }

        depositDetailDto.setAmount(amount);
        depositDetailDto.setCurrency(currency);
        
        depositDetailDto.setStatus(status);
        depositDetailDto.setCreatedTime(createdTime);
        depositDetailDto.setNote("This is a note");
        }

        
        
        return depositDetailDto;
    }
    

}