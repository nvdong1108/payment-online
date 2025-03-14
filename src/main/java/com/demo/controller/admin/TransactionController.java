package com.demo.controller.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.common.Constant;
import com.demo.entity.Transactions;
import com.demo.repository.TransactionsRepository;


@RequestMapping("/admin")
@Controller
public class TransactionController {


    @Autowired
    private TransactionsRepository transactionsRepository;

    private final int PAGE_SIZE = 6;

    @GetMapping("/transactions")
    public String transactions(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        if (fromDate == null) {
            fromDate = LocalDate.now().minusDays(Constant.MONTH);
        }
        if (toDate == null) {
            toDate = LocalDate.now();
        }
        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime = toDate.atTime(23, 59, 59);

        int totalPages = 0;
        List<Transactions> transactions = null;

        Page<Transactions> pageTransactions = transactionsRepository.findByTdateBetween(fromDateTime, toDateTime,
                PageRequest.of(page, PAGE_SIZE));
        totalPages = pageTransactions.getTotalPages();
        transactions = pageTransactions.getContent();


        model.addAttribute("transactions", transactions);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        // model.addAttribute("totalBillAmt", totalBillAmt);
        // model.addAttribute("transactionCount", countSize);

        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        // model.addAttribute("tab", tab);

        return "admin/transactions";
    }
}
