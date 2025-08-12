package ru.greatstep.jdbcexample.controllers;

import ru.greatstep.jdbcexample.dto.AccountDto;
import ru.greatstep.jdbcexample.dto.TransferRequest;
import ru.greatstep.jdbcexample.dto.TransactionLogDto;
import ru.greatstep.jdbcexample.service.NoTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/no-transactions")
public class NoTransactionController {

    private final NoTransactionService noTransactionService;

    public NoTransactionController(NoTransactionService noTransactionService) {
        this.noTransactionService = noTransactionService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody TransferRequest request) {
        try {
            noTransactionService.transferMoney(request.fromAccountId(), request.toAccountId(), request.amount());
            return ResponseEntity.ok("Transfer completed (no transactions)");
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body("Transfer failed (no transactions): " + e.getMessage());
        }
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        try {
            return ResponseEntity.ok(noTransactionService.getAllAccounts());
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/logs")
    public ResponseEntity<List<TransactionLogDto>> getTransactionLog() {
        try {
            return ResponseEntity.ok(noTransactionService.getTransactionLog());
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
