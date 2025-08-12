package ru.greatstep.jdbcexample.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.greatstep.jdbcexample.entity.Account;
import ru.greatstep.jdbcexample.entity.TransactionLog;
import ru.greatstep.jdbcexample.repository.AccountRepository;
import ru.greatstep.jdbcexample.repository.TransactionLogRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Profile("jpa")
public class JpaTransactionService {

    private final AccountRepository accountRepository;
    private final TransactionLogRepository transactionLogRepository;

    @Transactional
    public void transferMoney(Long fromAccountId, Long toAccountId, Double amount) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Account not found: " + fromAccountId));

        if (fromAccount.getBalance() < amount) {
            logTransaction(fromAccountId, toAccountId, amount, "FAILED", "Insufficient funds");
            throw new RuntimeException("Insufficient funds in account " + fromAccountId);
        }

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Account not found: " + toAccountId));

        // Имитация сбоя при специальной сумме
        if (amount == 666.0) {
            throw new RuntimeException("Simulated system failure");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        logTransaction(fromAccountId, toAccountId, amount, "COMPLETED", null);
    }


    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public List<TransactionLog> getTransactionLogs() {
        return transactionLogRepository.findAll();
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

