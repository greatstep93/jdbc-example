package ru.greatstep.jdbcexample.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.greatstep.jdbcexample.dto.AccountDto;
import ru.greatstep.jdbcexample.dto.TransactionLogDto;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final DataSource dataSource;

    public void transferMoney(int fromAccountId, int toAccountId, double amount) throws SQLException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            double fromBalance = getAccountBalance(connection, fromAccountId);
            if (fromBalance < amount) {
                logTransaction(connection, fromAccountId, toAccountId, amount, "FAILED",
                        "Insufficient funds");
                throw new SQLException("Insufficient funds in account " + fromAccountId);
            }

            updateAccountBalance(connection, fromAccountId, -amount);
            // Имитируем сбой между операциями
            if (amount == 666.00) {
                throw new SQLException("Simulated system failure after first update");
            }
            updateAccountBalance(connection, toAccountId, amount);
            logTransaction(connection, fromAccountId, toAccountId, amount, "COMPLETED", null);

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public List<AccountDto> getAllAccounts() throws SQLException {
        List<AccountDto> accounts = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM accounts");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                accounts.add(new AccountDto(
                        rs.getInt("id"),
                        rs.getString("user_name"),
                        rs.getDouble("balance")
                ));
            }
        }
        return accounts;
    }

    public List<TransactionLogDto> getTransactionLog() throws SQLException {
        List<TransactionLogDto> logs = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM transaction_log");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                logs.add(new TransactionLogDto(
                        rs.getInt("id"),
                        rs.getObject("from_account", Integer.class),
                        rs.getObject("to_account", Integer.class),
                        rs.getDouble("amount"),
                        rs.getString("status"),
                        rs.getString("error_message"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        }
        return logs;
    }

    private double getAccountBalance(Connection connection, int accountId) throws SQLException {
        String sql = "SELECT balance FROM accounts WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
            throw new SQLException("Account not found: " + accountId);
        }
    }

    private void updateAccountBalance(Connection connection, int accountId, double amount) throws SQLException {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, amount);
            stmt.setInt(2, accountId);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Account not found: " + accountId);
            }
        }
    }

    private void logTransaction(Connection connection, Integer fromAccountId, Integer toAccountId,
                                Double amount, String status, String errorMessage) throws SQLException {
        String sql = "INSERT INTO transaction_log (from_account, to_account, amount, status, error_message) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, fromAccountId);
            stmt.setObject(2, toAccountId);
            stmt.setDouble(3, amount);
            stmt.setString(4, status);
            stmt.setString(5, errorMessage);
            stmt.executeUpdate();
        }
    }
}
