// Package declaration: this class belongs to the ui package of EmployeeTracker
package EmployeeTracker.ui;

/**
 * MainApp.java
 *
 *  *  Professor: Ashley Evans
 *  *  Student name: Minh Ngoc Tran
 *  *  Course: 202530-CEN-3024C-31774
 *  *  Date : Jun 18 - 2025
 *
 * This class serves as the main user interface (UI) for the Employee Tracker console application.
 *
 * Responsibilities:
 * - Provides a text-based menu-driven interface for users to interact with the system.
 * - Delegates core operations to the EmployeeService class for business logic handling.
 * - Uses InputValidator class to perform input validation and ensure data integrity.
 * - Supports full CRUD operations: loading data from file, displaying employees, adding, removing,
 *   updating employee records, saving data back to file, and generating tenure reports.
 * - Handles user input, provides formatted outputs, and ensures graceful error handling.
 *
 * Main Features:
 * - File I/O operations for reading and writing employee data.
 * - Interactive employee data management with validation.
 * - Clean, formatted display of employee lists using tabular output.
 * - Employee tenure report generation with categorization.
 * - Modular design for easy extension and maintenance.
 *
 * Dependencies:
 * - Employee (data model)
 * - EmployeeService (business logic)
 * - InputValidator (input validation and user input handling)
 */


// Import necessary classes for employee data, service operations, and input validation
import EmployeeTracker.model.Employee;
import EmployeeTracker.service.EmployeeService;
import EmployeeTracker.utils.InputValidator;

import java.time.LocalDate; // For handling date objects
import java.util.List; // For lists of employees
import java.util.Map; // For report grouping
import java.util.Optional; // For optional return types

/**
 * Main application class for console UI.
 */
public class MainApp {
    // Service object to handle employee data operations
    private final EmployeeService employeeService;
    // Utility object to handle validated user input
    private final InputValidator inputValidator;

    /**
     * Constructor initializes service and input validator objects.
     */
    public MainApp() {
        this.employeeService = new EmployeeService(); // Instantiate employee service
        this.inputValidator = new InputValidator(); // Instantiate input validator
    }

    /**
     * Main method: program entry point.
     */
    public static void main(String[] args) {
        MainApp app = new MainApp(); // Create instance of MainApp
        app.run(); // Call run() to start menu loop
    }

    /**
     * Main menu loop displaying options and handling user choices.
     */
    public void run() {
        boolean continueRunning = true; // Loop control variable
        while (continueRunning) { // Loop until user exits
            displayMenu(); // Show menu options
            int choice = inputValidator.getMenuChoice("Choose option: ", 1, 8); // Get user choice between 1-8
            switch (choice) {
                case 1 -> { // Load data from file
                    boolean loaded = loadData(); // Call loadData()
                    System.out.println(loaded ? "Data loaded." : "Failed to load data."); // Feedback
                }
                case 2 -> { // Display all employees
                    displayEmployees(); // Call displayEmployees()
                }
                case 3 -> { // Add a new employee
                    Employee emp = addEmployee(); // Call addEmployee()
                    System.out.println("Added Employee:\n" + emp); // Show added employee info
                }
                case 4 -> { // Remove employee
                    boolean removed = removeEmployee(); // Call removeEmployee()
                    System.out.println(removed ? "Employee removed." : "Employee not found."); // Feedback
                }
                case 5 -> { // Update employee
                    boolean updated = updateEmployee(); // Call updateEmployee()
                    System.out.println(updated ? "Update successful." : "Update failed."); // Feedback
                }
                case 6 -> { // Generate tenure report
                    generateReport(); // Call generateReport()
                }
                case 7 -> { // Save data to file
                    boolean saved = saveData(); // Call saveData()
                    System.out.println(saved ? "Data saved." : "Failed to save data."); // Feedback
                }
                case 8 -> { // Exit program
                    System.out.println("Exiting Thank you for using Employee Tracker... Goodbye!"); // Exit message
                    continueRunning = false; // Set loop control to false to exit
                }
            }
        }
    }

    /**
     * Displays the menu options to the user.
     */
    private void displayMenu() {
        System.out.println("\n=== Employee Tracker Menu ===");
        System.out.println("1. Load employees from file");
        System.out.println("2. Display all employees");
        System.out.println("3. Add employee");
        System.out.println("4. Remove employee");
        System.out.println("5. Update employee");
        System.out.println("6. Generate tenure report");
        System.out.println("7. Save employees");
        System.out.println("8. Exit");
    }

    /**
     * Loads employee data from a file, prompts for file path.
     * @return true if data loaded successfully, false otherwise.
     */
    private boolean loadData() {
        String path = inputValidator.getFilePath("Enter file path to load"); // Prompt user for file path
        return employeeService.loadFromFile(path); // Load data using service
    }

    /**
     * Saves employee data to a file, prompts for file path.
     * @return true if saved successfully.
     */
    private boolean saveData() {
        String path = inputValidator.getFilePath("Enter file path to save"); // Prompt user for file path
        return employeeService.saveToFile(path); // Save data via service
    }

    /**
     * Displays all employee records in tabular format.
     */
    private void displayEmployees() {
        List<Employee> list = employeeService.getAllEmployees(); // Retrieve list from service
        if (list.isEmpty()) { // Check if list is empty
            System.out.println("No employee records found."); // Notify user if empty
            return;
        }
        // Print table header with column titles
        System.out.println("+----+-------------------+----------------------+------------+------------+--------------+--------+");
        System.out.format("| %-2s | %-17s | %-20s | %-10s | %-10s | %-12s | %-6s |\n",
                "ID", "Name", "Position", "Salary", "Hire Date", "Department", "Active");
        System.out.println("+----+-------------------+----------------------+------------+------------+--------------+--------+");
        // Loop through each employee and print formatted data
        for (Employee e : list) {
            System.out.format("| %2d | %-17s | %-20s | %10.2f | %10s | %-12s | %-6s |\n",
                    e.getId(),
                    truncate(e.getName(), 17),
                    truncate(e.getPosition(), 20),
                    e.getSalary(),
                    e.getHireDate() != null ? e.getHireDate().toString() : "N/A",
                    truncate(e.getDepartment(), 12),
                    e.getActive());
        }
        System.out.println("+----+-------------------+----------------------+------------+------------+--------------+--------+");
    }

    /**
     * Helper method to truncate strings for table display.
     * @param text String to truncate.
     * @param maxLength Maximum allowed length.
     * @return Truncated string with ellipsis if needed.
     */
    private String truncate(String text, int maxLength) {
        if (text == null) return ""; // Handle null
        return text.length() <= maxLength ? text : text.substring(0, maxLength - 3) + "..."; // Truncate with ellipsis
    }

    /**
     * Adds a new employee based on user input.
     * @return Employee object created.
     */
    private Employee addEmployee() {
        String name = inputValidator.getNonEmptyString("Name: "); // Prompt for name
        String position = inputValidator.getNonEmptyString("Position: "); // Prompt for position
        Double salary = inputValidator.getPositiveDouble("Salary: "); // Prompt for salary
        LocalDate hireDate = inputValidator.getDate("Hire Date"); // Prompt for hire date
        String department = inputValidator.getNonEmptyString("Department: "); // Prompt for department
        Boolean active = inputValidator.getBoolean("Active: "); // Prompt for active status
        return employeeService.addEmployee(name, position, salary, hireDate, department, active); // Create and return new employee
    }

    /**
     * Removes an employee by ID, prompts user for ID.
     * @return true if removal was successful.
     */
    private boolean removeEmployee() {
        Long id = inputValidator.getPositiveLong("Employee ID to remove: "); // Prompt for employee ID
        return employeeService.removeEmployee(id); // Remove via service
    }

    /**
     * Prompts for employee ID and field to update, then performs update.
     * @return true if update was successful.
     */
    private boolean updateEmployee() {
        Long id = inputValidator.getPositiveLong("Employee ID to update: "); // Prompt for employee ID
        Optional<Employee> empOpt = employeeService.findById(id); // Find employee
        if (empOpt.isEmpty()) { // If not found
            System.out.println("Employee not found."); // Notify user
            return false;
        }
        Employee emp = empOpt.get(); // Get employee object
        System.out.println("Current data: " + emp); // Show current data
        // Show field options
        System.out.println("Select field to update:");
        System.out.println("1. Name");
        System.out.println("2. Position");
        System.out.println("3. Salary");
        System.out.println("4. Hire Date");
        System.out.println("5. Department");
        System.out.println("6. Active");
        int fieldChoice = inputValidator.getMenuChoice("Choice: ", 1, 6); // Get choice

        boolean success = false; // Track update success
        switch (fieldChoice) {
            case 1 -> { // Update name
                String newName = inputValidator.getNonEmptyString("New Name: ");
                success = employeeService.updateEmployee(id, "name", newName);
            }
            case 2 -> { // Update position
                String newPosition = inputValidator.getNonEmptyString("New Position: ");
                success = employeeService.updateEmployee(id, "position", newPosition);
            }
            case 3 -> { // Update salary
                Double newSalary = inputValidator.getPositiveDouble("New Salary: ");
                success = employeeService.updateEmployee(id, "salary", newSalary);
            }
            case 4 -> { // Update hire date
                LocalDate newHireDate = inputValidator.getDate("New Hire Date");
                success = employeeService.updateEmployee(id, "hiredate", newHireDate);
            }
            case 5 -> { // Update department
                String newDept = inputValidator.getNonEmptyString("New Department: ");
                success = employeeService.updateEmployee(id, "department", newDept);
            }
            case 6 -> { // Update active status
                Boolean newActive = inputValidator.getBoolean("Is Active: ");
                success = employeeService.updateEmployee(id, "active", newActive);
            }
        }
        return success; // Return whether update was successful
    }

    /**
     * Generates a report of employees categorized by tenure.
     */
    private void generateReport() {
        Map<String, List<Employee>> report = employeeService.generateTenureReport(); // Generate report
        // Loop through each category
        for (Map.Entry<String, List<Employee>> entry : report.entrySet()) {
            System.out.println("\n" + entry.getKey() + ":"); // Category name
            List<Employee> list = entry.getValue(); // List of employees in category
            if (list.isEmpty()) { // If no employees
                System.out.println("  No employees."); // Notify
            } else {
                // Print table header
                System.out.println("+----+-------------------+----------------------+------------+------------+--------------+--------+");
                System.out.format("| %-2s | %-17s | %-20s | %-10s | %-10s | %-12s | %-6s |\n",
                        "ID", "Name", "Position", "Salary", "Hire Date", "Department", "Active");
                System.out.println("+----+-------------------+----------------------+------------+------------+--------------+--------+");
                // Print each employee in category
                for (Employee e : list) {
                    System.out.format("| %2d | %-17s | %-20s | %10.2f | %10s | %-12s | %-6s |\n",
                            e.getId(),
                            truncate(e.getName(), 17),
                            truncate(e.getPosition(), 20),
                            e.getSalary(),
                            e.getHireDate() != null ? e.getHireDate().toString() : "N/A",
                            truncate(e.getDepartment(), 12),
                            e.getActive());
                }
                // End of category table
                System.out.println("+----+-------------------+----------------------+------------+------------+--------------+--------+");
            }
        }
    }
}