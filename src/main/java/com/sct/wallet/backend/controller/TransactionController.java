package com.sct.wallet.backend.controller;

import com.sct.wallet.backend.service.TransactionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionServiceImpl service;

    @PostMapping
    public String transaction(){
        return "This is transaction endpoint";
    }




}
