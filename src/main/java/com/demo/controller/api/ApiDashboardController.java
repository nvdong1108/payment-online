package com.demo.controller.api;

import com.demo.config.JwtUtil;
import com.demo.dto.DepositDetailDto;
import com.demo.entity.Deposits;
import com.demo.entity.Transactions;
import com.demo.repository.DepositsRepository;
import com.demo.repository.TransactionsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api")
public class ApiDashboardController {

    @Autowired
    private TransactionsRepository transactionsRepository;

    private final int PAGE_SIZE = 6;

    @GetMapping("/dashboard/getDepositDetails/{id}")
    public DepositDetailDto getDepositDetails(@PathVariable Long id) {
        String transId = String.valueOf(id);
        List<Transactions> listTransactions = transactionsRepository.findByTransID(transId);
        DepositDetailDto depositDetailDto = new DepositDetailDto();
        if (listTransactions != null && !listTransactions.isEmpty()) {
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
                depositDetailDto.setUsername(fullName == null ? "" : fullName);
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

    @PostMapping("dashboard/search")
    public ResponseEntity<?> getMethodName(@RequestBody Map<String, Object> body) {
        Map<String, Object> response = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Double totalBillAmt = 0.0;
        int countSize = 0;
        int totalPages = 0 ;
        List<Transactions> transactions = null;
        int currentPage = 0;

        LocalDate fromDate = LocalDate.parse((String) body.get("fromDate"), formatter);
        LocalDate toDate = LocalDate.parse((String) body.get("toDate"), formatter);

        LocalDateTime fromDateTime = fromDate.atStartOfDay(); 
        LocalDateTime toDateTime = toDate.atTime(23, 59, 59);

        String tab = (String) body.get("tab");
        if ("deposits".equals(tab)   || "transactions".equals(tab)) {
            List<Transactions> listTransactions = transactionsRepository.findByTdateBetween(fromDateTime, toDateTime);
            response.put("transactions", listTransactions);
            if(listTransactions!=null && !listTransactions.isEmpty()){
                totalBillAmt = listTransactions.stream().mapToDouble(Transactions::getBillAmt).sum();
                countSize= listTransactions.size();
            }

            Page<Transactions> pageTransactions = transactionsRepository.findByTdateBetween(fromDateTime, toDateTime , PageRequest.of(currentPage, PAGE_SIZE));
            totalPages =pageTransactions.getTotalPages();
            transactions = pageTransactions.getContent();

        } 
        response.put("tab", tab);
        response.put("tototalBillAmt",totalBillAmt);
        response.put("countSize",countSize);


        response.put("transactions", transactions);
        response.put("currentPage", currentPage);
        response.put("totalPages", totalPages);

        response.put("fromDate", fromDate);
        response.put("toDate", toDate);

        return ResponseEntity.ok(response);
    }

}