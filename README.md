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
    
5. Configure Database Connection
Open the database configuration file and update the following details according to your MySQL installation:

6. Compile the Project
If Maven is configured, run:
mvn clean compile

8. Run the Application
The main entry point of the application is:

metro.Main

Using Maven:

mvn exec:java

Alternatively, the project can be compiled and executed directly using the Java compiler.

8. Use the Application
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
  
