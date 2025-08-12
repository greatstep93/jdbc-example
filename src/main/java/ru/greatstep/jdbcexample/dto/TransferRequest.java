package ru.greatstep.jdbcexample.dto;

public record TransferRequest(long fromAccountId, long toAccountId, double amount) {
}
