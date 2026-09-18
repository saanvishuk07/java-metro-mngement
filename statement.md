# Project Statement

**Metro Management System with Smart Card Integration**

## Problem Statement
---
Daily metro passengers often need to purchase tickets or manually process fares, which can lead to queues and increased processing time.

The proposed Metro Management System provides a smart-card based workflow where passengers can register, receive a smart card, recharge the card, enter and exit metro stations, and have the appropriate fare automatically deducted based on travel distance.

The system stores passenger, card, journey and transaction information in a MySQL database.

## Project Scope
---
The project covers the following activities:

* Passenger registration and login
* Smart-card issuance
* Smart-card recharge
* Station management
* Metro entry and exit processing
* Distance-based fare calculation
* Automatic fare deduction
* Journey history management
* Transaction history management
* Database-based storage
* Input validation and exception handling

The project is implemented as a command-line Core Java application using JDBC and MySQL.

## Target Users
---
### 1. Passengers
Passengers can:
* Register and log in
* Obtain a smart card
* Recharge their card
* Enter and exit metro stations
* View journey history
* View transaction history

### 2. Administrators
Administrators can manage metro-related information such as stations and passenger/card records.

## High-Level Features
---
1. Passenger Management
2. Admin Management
3. Smart-Card Management
4. Card Recharge
5. Station Management
6. Metro Entry/Exit
7. Distance-Based Fare Calculation
8. Automatic Fare Deduction
9. Journey History
10. Transaction History
11. Input Validation
12. Exception Handling

## Fare Model
---

| Travel Distance | Fare |
| --------------- | ---: |
| 0–5 km          |  ₹10 |
| 5–10 km         |  ₹20 |
| 10–20 km        |  ₹30 |
| Above 20 km     |  ₹40 |
