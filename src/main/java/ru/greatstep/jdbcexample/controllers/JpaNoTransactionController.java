package ru.greatstep.jdbcexample.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.greatstep.jdbcexample.dto.TransferRequest;
import ru.greatstep.jdbcexample.service.JpaNoTransactionService;

@RestController
@RequestMapping("/api/no-transactions")
@RequiredArgsConstructor
@Profile("jpa")
public class JpaNoTransactionController {

    private final JpaNoTransactionService noTransactionService;

    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody TransferRequest request) {

        try {
            noTransactionService.transferMoney(request.fromAccountId(), request.toAccountId(), request.amount());
            return ResponseEntity.ok("Transfer completed without transaction");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Transfer failed: " + e.getMessage());
        }
    }
}
