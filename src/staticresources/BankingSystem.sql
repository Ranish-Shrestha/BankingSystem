CREATE DATABASE BankingSystem;

USE BankingSystem;

CREATE TABLE Clients (
    client_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    phone_number VARCHAR(15),
    email VARCHAR(100) UNIQUE
);

CREATE TABLE Accounts (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT,
    account_type VARCHAR(50),
    balance DECIMAL(10, 2) DEFAULT 0.00,
    FOREIGN KEY (client_id) REFERENCES Clients(client_id)
);

CREATE TABLE Transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT,
    transaction_type VARCHAR(50),
    amount DECIMAL(10, 2),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(255),
    FOREIGN KEY (account_id) REFERENCES Accounts(account_id)
);

INSERT INTO Clients (name, address, phone_number, email) VALUES
('John Doe', '123 Elm Street', '1234567890', 'john@example.com'),
('Jane Smith', '456 Oak Street', '0987654321', 'jane@example.com');

INSERT INTO Accounts (client_id, account_type, balance) VALUES
(1, 'savings', 1000.00),
(1, 'checking', 500.00),
(2, 'savings', 2000.00);
