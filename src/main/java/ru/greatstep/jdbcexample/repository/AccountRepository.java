package ru.greatstep.jdbcexample.repository;

import org.springframework.context.annotation.Profile;
import ru.greatstep.jdbcexample.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

@Profile("jpa")
public interface AccountRepository extends JpaRepository<Account, Long> {
}
