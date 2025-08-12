package ru.greatstep.jdbcexample.dto;

import java.time.LocalDateTime;

public record TransactionLogDto(
        int id,
        Integer fromAccount,
        Integer toAccount,
        double amount,
        String status,
        String errorMessage,
        LocalDateTime createdAt
) {
}
