# PROJECT TITLE :- Metro Management System with Smart Card Integration

---

# OVERVIEW OF THE PROJECT
---
Metro Management System with Smart Card Integration is a command-line based Core Java application designed to manage metro passengers and automate smart-card based fare processing.

The system allows passengers to register, log in, obtain a smart card, recharge their balance, enter and exit metro stations, and automatically calculate and deduct fares based on travel distance.

The project demonstrates Core Java, Object-Oriented Programming, JDBC and MySQL database connectivity.

# FEATURES
---
* Passenger registration and login
* Admin management
* Smart-card issuance
* Smart-card recharge
* Station management
* Metro entry and exit
* Distance-based fare calculation
* Automatic fare deduction
* Journey history
* Transaction history
* Input validation
* Exception handling
* MySQL database storage

# TECHNOLOGIES / TOOLS USED 
---
* Java 17+ – Main programming language used to develop the application.
* Core Java – Used for implementing the application logic and OOP concepts.
* JDBC (Java Database Connectivity) – Used to connect Java with the MySQL database.
* MySQL 8 – Used for storing passenger, smart-card, station, journey and transaction data.
* Object-Oriented Programming (OOP) – Used for abstraction, inheritance, polymorphism and encapsulation.
* Collections Framework – Used for managing and processing data in the application.
* Exception Handling – Used for input validation and handling runtime errors.
* Command-Line Interface (CLI) – Used for interacting with the application through the terminal.
* Git – Used for version control.
* GitHub – Used for source-code hosting and project submission.
* Maven – Used for project build and dependency management, if configured.

# STEPS TO INSTALL AND RUN THE PROJECT
---
## 1. Install Required Software

Make sure the following software is installed on your system:
Java JDK 17 or later
MySQL 8
Git
Maven (if Maven is used)

Verify Java installation:
java -version

Verify Maven installation:
mvn -version

## 2. Clone the Repository
Open the terminal or command prompt and run:
git clone <YOUR-GITHUB-REPOSITORY-URL>

## 3. Open the Project Directory
cd Metro-Management-System

## 4. Set Up the MySQL Database
* Start the MySQL server and create the database.
* Run the SQL file provided in the project:
* database/schema.sql
* Then insert the sample data:
* database/sample_data.sql
* The database contains the following main tables:

  * admins
  * passengers
  * stations
  * smart_cards
  * transactions
  * journeys
    
## 5. Configure Database Connection
Open the database configuration file and update the following details according to your MySQL installation:

## 6. Compile the Project
If Maven is configured, run:
mvn clean compile

## 7. Run the Application
The main entry point of the application is:

metro.Main

Using Maven:

mvn exec:java

Alternatively, the project can be compiled and executed directly using the Java compiler.

## 8. Use the Application
After launching, follow the command-line menu to perform operations such as:

Passenger registration
Passenger login
Smart-card issuance
Card recharge
Station selection
Metro entry
Metro exit
Fare calculation
Journey history
Transaction history

The application uses standard console input/output and does not require a GUI.

# INSTRUCTIONS FOR TESTING 
---
## Instructions for Testing

The Metro Management System can be tested through the command-line interface. Testing should be performed after setting up the MySQL database and configuring the database connection.

### 1. Start the Application

Run the main application:

```bash
mvn exec:java
```

or run:

```text
metro.Main
```

### 2. Test Passenger Registration

* Select the **Passenger Registration** option.
* Enter valid passenger details.
* Verify that the passenger is successfully registered.
* Try entering invalid or duplicate details and verify that an appropriate error message is displayed.

### 3. Test Passenger Login

* Enter valid login credentials.
* Verify that the passenger can log in successfully.
* Enter incorrect credentials and verify that login is rejected.

### 4. Test Smart Card

Test the following operations:

* Issue a smart card to a passenger.
* View card details.
* Recharge the smart card with a valid amount.
* Verify that the updated balance is displayed correctly.

### 5. Test Metro Entry and Exit

* Select a valid entry station.
* Start a journey using a valid smart card.
* Select a valid exit station.
* Verify that the journey distance is calculated.
* Verify that the correct fare is calculated and deducted from the smart-card balance.

### 6. Test Fare Calculation

Verify the fare model using the following test cases:

| Test Case | Distance | Expected Fare |
| --------- | -------: | ------------: |
| TC01      |     3 km |           ₹10 |
| TC02      |     5 km |           ₹10 |
| TC03      |     7 km |           ₹20 |
| TC04      |    10 km |           ₹20 |
| TC05      |    15 km |           ₹30 |
| TC06      |    20 km |           ₹30 |
| TC07      |    25 km |           ₹40 |

### 7. Test Insufficient Balance

* Use a smart card with insufficient balance.
* Attempt a journey whose fare is greater than the available balance.
* Verify that the transaction is rejected and an appropriate error message is displayed.

### 8. Test Invalid Input and Exception Handling

Test invalid inputs such as:

* Negative recharge amount
* Invalid passenger ID
* Invalid smart-card ID
* Invalid station ID
* Invalid distance
* Empty input fields
* Insufficient card balance

Verify that the application handles these errors without crashing.

### 9. Test Journey and Transaction History

After completing journeys and transactions:

* Open **Journey History**.
* Verify that completed journeys are displayed correctly.
* Open **Transaction History**.
* Verify that recharge and fare deduction transactions are recorded correctly.

### 10. Database Verification

Verify that the corresponding records are correctly stored in MySQL tables:

```text
passengers
smart_cards
stations
journeys
transactions
admins
```

### Testing Result

The application is considered successfully tested when all major functional operations produce the expected output, invalid inputs are handled properly, and the corresponding database records are correctly created or updated.

  
