package ru.greatstep.jdbcexample.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.greatstep.jdbcexample.dto.TransferRequest;
import ru.greatstep.jdbcexample.entity.Account;
import ru.greatstep.jdbcexample.entity.TransactionLog;
import ru.greatstep.jdbcexample.service.JpaTransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Profile("jpa")
public class JpaTransactionController {

    private final JpaTransactionService transactionalService;


    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody TransferRequest request) {

        try {
            transactionalService.transferMoney(request.fromAccountId(), request.toAccountId(), request.amount());
            return ResponseEntity.ok("Transfer completed successfully with transaction");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Transfer failed: " + e.getMessage());
        }
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts() {
        return ResponseEntity.ok(transactionalService.getAccounts());
    }

    @GetMapping("/logs")
    public ResponseEntity<List<TransactionLog>> getLogs() {
        return ResponseEntity.ok(transactionalService.getTransactionLogs());
    }
}
