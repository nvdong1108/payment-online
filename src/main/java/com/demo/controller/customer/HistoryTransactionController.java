package com.demo.controller.customer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
import com.demo.common.DateUtil;
import com.demo.entity.Transactions;
import com.demo.repository.TransactionsRepository;

@RequestMapping("/customer")
@Controller
public class HistoryTransactionController {

    @Autowired
    private TransactionsRepository transactionsRepository;

    private final int PAGE_SIZE = 6;

    @GetMapping("/transactions")
    public String getMethodName(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        if (fromDate == null) {
            fromDate = LocalDate.now().minusDays(Constant.WEEK); 
        }
        if (toDate == null) {
            toDate = LocalDate.now(); 
        }
        LocalDateTime fromDateTime = fromDate.atStartOfDay(); 
        LocalDateTime toDateTime = toDate.atTime(23, 59, 59);


        Page<Transactions> listTransactions = transactionsRepository.findByTdateBetween(fromDateTime, toDateTime,
                PageRequest.of(page, PAGE_SIZE));

        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("transactions", listTransactions.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", listTransactions.getTotalPages());

        return "/customer/transactions";
    }

}
