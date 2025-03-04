package com.demo.controller.api;

import com.demo.config.JwtUtil;
import com.demo.dto.DepositDetailDto;
import com.demo.entity.Deposits;
import com.demo.repository.DepositsRepository;

import jakarta.servlet.http.HttpServletRequest;

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