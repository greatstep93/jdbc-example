DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS transaction_log;

CREATE TABLE accounts (
                          id SERIAL PRIMARY KEY,
                          user_name VARCHAR(100) NOT NULL,
                          balance DECIMAL(15, 2) NOT NULL
);

CREATE TABLE transaction_log (
                                 id SERIAL PRIMARY KEY,
                                 from_account INT,
                                 to_account INT,
                                 amount DECIMAL(15, 2),
                                 status VARCHAR(50),
                                 error_message TEXT,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO accounts (user_name, balance) VALUES
                                              ('Alice', 1000.00),
                                              ('Bob', 500.00),
                                              ('Charlie', 200.00);