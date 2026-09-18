CREATE DATABASE IF NOT EXISTS metro_system;
USE metro_system;

DROP TABLE IF EXISTS journeys;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS smart_cards;
DROP TABLE IF EXISTS passengers;
DROP TABLE IF EXISTS stations;
DROP TABLE IF EXISTS admins;

CREATE TABLE admins (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE passengers (
    passenger_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) UNIQUE NOT NULL,
    email VARCHAR(100),
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stations (
    station_id INT PRIMARY KEY AUTO_INCREMENT,
    station_name VARCHAR(100) UNIQUE NOT NULL,
    line_name VARCHAR(50) NOT NULL,
    distance_from_origin DECIMAL(8,2) NOT NULL
);

CREATE TABLE smart_cards (
    card_id INT PRIMARY KEY AUTO_INCREMENT,
    card_number VARCHAR(30) UNIQUE NOT NULL,
    passenger_id INT NOT NULL,
    balance DECIMAL(10,2) DEFAULT 0.00,
    status ENUM('ACTIVE','BLOCKED') DEFAULT 'ACTIVE',
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (passenger_id) REFERENCES passengers(passenger_id)
);

CREATE TABLE transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    card_id INT NOT NULL,
    transaction_type ENUM('RECHARGE','FARE','REFUND') NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(255),
    FOREIGN KEY (card_id) REFERENCES smart_cards(card_id)
);

CREATE TABLE journeys (
    journey_id INT PRIMARY KEY AUTO_INCREMENT,
    card_id INT NOT NULL,
    entry_station_id INT NOT NULL,
    exit_station_id INT,
    entry_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    exit_time TIMESTAMP NULL,
    fare DECIMAL(10,2) DEFAULT 0.00,
    status ENUM('OPEN','COMPLETED') DEFAULT 'OPEN',
    FOREIGN KEY (card_id) REFERENCES smart_cards(card_id),
    FOREIGN KEY (entry_station_id) REFERENCES stations(station_id),
    FOREIGN KEY (exit_station_id) REFERENCES stations(station_id)
);

INSERT INTO admins(username,password) VALUES ('admin','admin123');

INSERT INTO stations(station_name,line_name,distance_from_origin) VALUES
('Bhopal Central','Blue Line',0),
('MP Nagar','Blue Line',4),
('Habibganj','Blue Line',7),
('Misrod','Blue Line',12),
('Mandideep','Blue Line',20);

INSERT INTO passengers(name,phone,email,password) VALUES
('Demo Passenger','9999999999','demo@example.com','demo123');

INSERT INTO smart_cards(card_number,passenger_id,balance) VALUES
('METRO10001',1,150.00);
