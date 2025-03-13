package com.demo.controller;

import com.demo.common.Constant;
import com.demo.dto.DepositDetailDto;
import com.demo.entity.Deposits;
import com.demo.entity.Transactions;
import com.demo.entity.User;
import com.demo.repository.DepositsRepository;
import com.demo.repository.TransactionsRepository;
import com.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    @Autowired
    private DepositsRepository depositsRepository;

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private UserRepository userRepository;

    private final int PAGE_SIZE = 6;

    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(required = false) String tab,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        if (tab == null || tab.isEmpty()) {
            tab = "deposits";
        }
        if (fromDate == null) {
            fromDate = LocalDate.now().minusDays(Constant.TODAY);
        }
        if (toDate == null) {
            toDate = LocalDate.now();
        }
        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime = toDate.atTime(23, 59, 59);

        int totalPages = 0;
        List<Transactions> transactions = null;
        Double totalBillAmt = 0.0;
        int countSize = 0;

        Page<User> pageUsers = userRepository.findByCreatedTimeBetween(fromDateTime, toDateTime,
                PageRequest.of(page, PAGE_SIZE));
        if (pageUsers != null && !pageUsers.isEmpty()) {
            model.addAttribute("listUsers", pageUsers.getContent());
            model.addAttribute("currentPageUser", page);
            model.addAttribute("totalPagesUser", pageUsers.getTotalPages());
        }

        Page<Transactions> pageTransactions = transactionsRepository.findByTdateBetween(fromDateTime, toDateTime,
                PageRequest.of(page, PAGE_SIZE));
        totalPages = pageTransactions.getTotalPages();
        transactions = pageTransactions.getContent();

        List<Transactions> listTransactions = transactionsRepository.findByTdateBetween(fromDateTime, toDateTime);
        if (listTransactions != null && !listTransactions.isEmpty()) {
            totalBillAmt = listTransactions.stream().mapToDouble(Transactions::getBillAmt).sum();
            countSize = listTransactions.size();
        }

        model.addAttribute("transactions", transactions);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        model.addAttribute("totalBillAmt", totalBillAmt);
        model.addAttribute("transactionCount", countSize);

        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("tab", tab);

        return "dashboard";
    }

    @PostMapping("/dashboard/searchDeposit")
    public List<Deposits> searchDeposit(@RequestBody Map<String, String> body) {

        Date startDate = new Date();
        Date endDate = new Date();

        List<Deposits> listDeposits = depositsRepository.findByCreatedTimeBetween(startDate, endDate);
        return listDeposits;

    }

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

}
