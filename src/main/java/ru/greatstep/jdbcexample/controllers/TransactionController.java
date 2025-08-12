package ru.greatstep.jdbcexample.controllers;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.greatstep.jdbcexample.dto.AccountDto;
import ru.greatstep.jdbcexample.dto.TransactionLogDto;
import ru.greatstep.jdbcexample.dto.TransferRequest;
import ru.greatstep.jdbcexample.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Profile("jdbc")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody TransferRequest request) {
        try {
            transactionService.transferMoney(request.fromAccountId(), request.toAccountId(), request.amount());
            return ResponseEntity.ok("Transfer completed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Transfer failed: " + e.getMessage());
        }
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        try {
            return ResponseEntity.ok(transactionService.getAllAccounts());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/logs")
    public ResponseEntity<List<TransactionLogDto>> getTransactionLog() {
        try {
            return ResponseEntity.ok(transactionService.getTransactionLog());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
