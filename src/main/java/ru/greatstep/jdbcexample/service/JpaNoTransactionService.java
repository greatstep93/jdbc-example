package ru.greatstep.jdbcexample.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.greatstep.jdbcexample.entity.Account;
import ru.greatstep.jdbcexample.entity.TransactionLog;
import ru.greatstep.jdbcexample.repository.AccountRepository;
import ru.greatstep.jdbcexample.repository.TransactionLogRepository;

@Service
@RequiredArgsConstructor
public class JpaNoTransactionService {

    private final AccountRepository accountRepository;
    private final TransactionLogRepository transactionLogRepository;


    public void transferMoney(Long fromAccountId, Long toAccountId, Double amount) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Account not found: " + fromAccountId));

        if (fromAccount.getBalance() < amount) {
            logTransaction(fromAccountId, toAccountId, amount, "FAILED", "Insufficient funds");
            throw new RuntimeException("Insufficient funds in account " + fromAccountId);
        }

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Account not found: " + toAccountId));

        // Списание средств
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        accountRepository.save(fromAccount);

        // Имитация сбоя при специальной сумме
        if (amount == 666.0) {
            throw new RuntimeException("Simulated system failure");
        }

        // Зачисление средств
        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepository.save(toAccount);

        logTransaction(fromAccountId, toAccountId, amount, "COMPLETED", null);
    }

    private void logTransaction(
            Long fromAccountId, Long toAccountId,
            Double amount, String status, String errorMessage
    ) {
        TransactionLog log = new TransactionLog();
        log.setFromAccount(fromAccountId);
        log.setToAccount(toAccountId);
        log.setAmount(amount);
        log.setStatus(status);
        log.setErrorMessage(errorMessage);
        transactionLogRepository.save(log);
    }
}
