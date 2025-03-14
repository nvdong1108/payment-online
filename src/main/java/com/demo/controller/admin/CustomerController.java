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
import com.demo.entity.User;
import com.demo.repository.TransactionsRepository;
import com.demo.repository.UserRepository;


@RequestMapping("/admin")
@Controller
public class CustomerController {


    @Autowired
    private UserRepository userRepository;

    private final int PAGE_SIZE = 6;

    @GetMapping("/customers")
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

        Page<User> pageUsers = userRepository.findByCreatedTimeBetween(fromDateTime, toDateTime,
                PageRequest.of(page, PAGE_SIZE));
        if (pageUsers != null && !pageUsers.isEmpty()) {
            model.addAttribute("listUsers", pageUsers.getContent());
            model.addAttribute("currentPageUser", page);
            model.addAttribute("totalPagesUser", pageUsers.getTotalPages());
        }
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);

        return "admin/customers";
    }
}
