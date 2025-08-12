package ru.greatstep.jdbcexample.dto;

public record TransferRequest(int fromAccountId, int toAccountId, double amount) {
}
