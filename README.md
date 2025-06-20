# Employee Tracker

A Java console application for managing employee records with CRUD operations, data persistence, input validation, and reporting.

## Table of Contents
- [Project Overview](#project-overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Setup & Running the Application](#setup--running-the-application)
- [Data File Format](#data-file-format)
- [Usage Guide](#usage-guide)
- [Tenure Report](#tenure-report)
- [Error Handling & Validation](#error-handling--validation)
- [Contributing](#contributing)
- [License](#license)

## Project Overview

Employee Tracker is a Java console-based application designed to help businesses manage their employee records efficiently. It supports creating, viewing, updating, and deleting employees, while persisting data between runs via a CSV text file.

The app emphasizes robust input validation, ease of use, and a clean architecture separating data, business logic, user input handling, and the UI.

## Features

- Load employee data from a CSV file  
- Display all employee records in a readable format  
- Add new employees with validated input  
- Remove employees by their unique ID  
- Update employee details such as name, position, salary, hire date, department, and active status  
- Generate a tenure report grouping employees by length of service (0–1, 1–5, 5+ years)  
- Save employee data back to a CSV file  
- Friendly menu-driven interface with clear prompts and validations  

## Project Structure

/employee-tracker<br/>
├── model<br/>
│ └── Employee.java # Employee data model with fields, getters/setters, and CSV serialization<br/>
├── service<br/>
│ └── EmployeeService.java # Business logic managing employee list, file I/O, and reporting<br/>
├── ui<br/>
│ └── MainApp.java # Console-based UI for user interaction and menu system<br/>
└── utils<br/>
└── InputValidator.java # Input validation utilities for safe console input<br/>

/employees.txt # CSV data file storing employee records outside source folders


## Prerequisites

- Java Development Kit (JDK) 11 or newer installed  
- Terminal or command prompt access  
- Basic knowledge of running Java programs from command line  

## Setup & Running the Application

1. **Prepare the Data File**  
   Place an `employees.txt` file in your project root folder (next to the `employee-tracker` directory). This file can be empty or contain existing employee data in CSV format (see [Data File Format](#data-file-format)).

2. **Compile the Source Code**  
   Open your terminal or command prompt in the `employee-tracker` folder and run:

   ```bash
   javac model/Employee.java service/EmployeeService.java ui/MainApp.java utils/InputValidator.java
   
3. **Run the Application**  

Start the program with:
- java ui.MainApp
  
4. **Use the Menu*** 
- Interact with the application using the displayed menu options.
 

## Data File Format

The employees.txt file stores employee data with one employee per line in CSV format:
id,name,position,salary,hireDate,department,active

.id — Unique employee ID (Long)

.name — Employee full name (String)

.position — Job title (String)

.salary — Annual salary (Double)

.hireDate — Hire date in yyyy-MM-dd format

.department — Department name (String)

.active — Employment status (true or false)

## Example entries:

1,Sarah Clark,Data Scientist,35000.00,2025-03-20,Software,true <br/>
2,Jane Smith,Developer,75000.00,2020-09-15,Engineering,true<br/>
3,Sam Johnson,Analyst,65000.00,2019-01-10,Finance,true


## Usage Guide
Upon running, the app shows the main menu:
**=== Employee Tracker Menu ===** 

1. Load employees from the file
2. Display all employees
3. Add a new employee
4. Remove an employee
5. Update an employee
6. Generate tenure report
7. Save employees to file
8. Exit <br/>

Option 1: Load employee data from a specified file path.

Option 2: List all employees currently loaded in memory.

Option 3: Add a new employee by entering details as prompted.

Option 4: Remove an employee by entering their ID.

Option 5: Update a specific employee’s details (selectable fields).

Option 6: Generate and display a tenure report grouping employees by years worked.

Option 7: Save the current employee data back to a file.

Option 8: Exit the application (save before exiting).



## Input Validation
Text inputs must not be empty.

Salaries must be positive numbers.

Dates must be entered in yyyy-MM-dd format.

Boolean fields accept only true or false.

Menu selections are validated for allowed ranges.

## Tenure Report
Selecting option 6 groups employees by their years of service:

0–1 years: New hires or interns

1–5 years: Mid-term employees

5+ years: Long-term employees

The report shows employees grouped and listed under each category.

## Error Handling & Validation
Invalid or malformed lines in the input file are skipped on load (without crashing).

Invalid user input prompts for re-entry with error messages.

Employee IDs must be unique; IDs are auto-assigned when adding new employees.

Removing or updating an employee by a non-existent ID will notify the user.

## Contributing
Contributions, bug reports, and feature requests are welcome! Please fork the repository and submit pull requests or issues.

## License
This project is licensed under the MIT License. See the LICENSE file for details.



