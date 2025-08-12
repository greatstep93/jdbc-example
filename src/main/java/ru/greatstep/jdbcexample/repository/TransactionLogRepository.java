package ru.greatstep.jdbcexample.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.greatstep.jdbcexample.entity.TransactionLog;

@Profile("jpa")
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
}
