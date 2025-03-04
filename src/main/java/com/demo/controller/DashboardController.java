package com.demo.controller;


import com.demo.common.DateUtil;
import com.demo.config.JwtUtil;
import com.demo.dto.DepositDetailDto;
import com.demo.entity.Deposits;
import com.demo.entity.Transactions;
import com.demo.repository.DepositsRepository;
import com.demo.repository.TransactionsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties.Transaction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {


    @Autowired
    private DepositsRepository depositsRepository;


    @Autowired
    private TransactionsRepository transactionsRepository;

  
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        Date dFrom = DateUtil.getFromCurrenDate();
        Date dTo = DateUtil.getToCurrenDate();

        String fromDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String toDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);

        List<Transactions> listTransactions = transactionsRepository.findByTdateBetween(dFrom, dTo);
        List<Deposits> deposits = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for (Transactions transactions : listTransactions) {
            Deposits deposit = new Deposits();
            deposit.setAmount(transactions.getBillAmt());
            deposit.setCurrency(transactions.getBillCurrency());
            deposit.setUsername(transactions.getDescriptor());
            deposit.setStatus(transactions.getStatus());
            deposit.setCreatedTime(transactions.getTdate());
            String jsonInfo = transactions.getInfo();
            try {
                Map<String, Object> map = objectMapper.readValue(jsonInfo, Map.class);
                String fullName = (String) map.get("fullName");
                deposit.setUsername(fullName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            deposits.add(deposit);
        }
         
        model.addAttribute("deposits", deposits);
        return "dashboard";
    }



    @PostMapping("/dashboard/searchDeposit")
    public List<Deposits> searchDeposit(@RequestBody Map<String, String> body) {
        
        Date startDate = new Date();
        Date endDate = new Date();

        List<Deposits> listDeposits = depositsRepository.findByCreatedTimeBetween(startDate,endDate );
        return listDeposits;
        
    }

    @GetMapping("/dashboard/getDepositDetails/{id}")
    public DepositDetailDto getDepositDetails(@PathVariable Long id) {
        Optional<Deposits> depositOptional = depositsRepository.findById(id);
        if (!depositOptional.isPresent()) {
            return null;
        }
        Deposits deposit = depositOptional.get();
        DepositDetailDto depositDetailDto = new DepositDetailDto();
        depositDetailDto.setAmount(deposit.getAmount() );
        depositDetailDto.setCurrency(deposit .getCurrency());
        depositDetailDto.setUsername(deposit.getUsername());
        depositDetailDto.setStatus(deposit.getStatus());
        depositDetailDto.setCreatedTime(deposit.getCreatedTime());
        depositDetailDto.setNote("This is a note");
        return depositDetailDto;
    }
    
    
    


    
}
