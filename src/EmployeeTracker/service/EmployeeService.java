// Package declaration: this class belongs to the service layer of EmployeeTracker
package EmployeeTracker.service;

// Import Employee model class
import EmployeeTracker.model.Employee;

/**
 * EmployeeService.java
 *
 *  * Professor: Ashley Evans
 *  *  Student name: Minh Ngoc Tran
 *  *  Course: 202530-CEN-3024C-31774
 *  *  Date : Jun 18 - 2025
 *
 * This class provides core business logic for managing employees in the Employee Tracker system.
 *
 * Main Responsibilities:
 * - Manage in-memory employee list using ArrayList.
 * - Provide CRUD operations: create, read, update, and delete employees.
 * - Automatically assign unique IDs to new employees.
 * - Load employee data from file and parse into Employee objects.
 * - Save employee data back to file in CSV format.
 * - Handle file I/O safely with exception handling.
 * - Support employee search by ID.
 * - Provide field-level update functionality using dynamic field names.
 * - Generate tenure-based employee report categorizing employees into:
 *     - 0-1 years
 *     - 1-5 years
 *     - 5+ years
 *
 * Features:
 * - Simple and efficient file storage (text-based CSV files).
 * - Defensive error handling for corrupted or missing files.
 * - Use of modern Java features (Optional, streams, try-with-resources, switch expressions).
 * - Separation of business logic from model and UI layers.
 *
 * Dependencies:
 * - Employee class from EmployeeTracker.model package.
 * - Standard Java I/O and utility classes.
 */

import java.io.BufferedReader;    // For reading text files efficiently
import java.io.BufferedWriter;    // For writing text files efficiently
import java.io.File;              // Represents file paths and files
import java.io.FileReader;        // Reads character files
import java.io.FileWriter;        // Writes character files
import java.io.IOException;       // Exception for file I/O errors
import java.time.LocalDate;       // Date representation for hire dates
import java.util.*;               // Import common utilities like List, Map, Optional, etc.

public class EmployeeService {

    // List to store employee objects in memory
    private final List<Employee> employees = new ArrayList<>();

    // Variable to track the next unique ID to assign
    private long nextId = 1L;

    // Default filename for saving/loading employee data
    private final String dataFile = "employees.txt";

    /**
     * Retrieve a copy of all employee records.
     */
    public List<Employee> getAllEmployees() {
        // Return a new list containing all employees to prevent external modification
        return new ArrayList<>(employees);
    }

    /**
     * Load employees from the default file.
     */
    public boolean loadEmployeesFromFile() {
        // Call loadFromFile() with the default filename
        return loadFromFile(dataFile);
    }

    /**
     * Load employee data from a specified file.
     * @param filepath Path to the file to load data from.
     * @return true if loading is successful, false otherwise.
     */
    public boolean loadFromFile(String filepath) {
        // Clear existing employee data
        employees.clear();
        // Reset nextId to 1 for new data
        nextId = 1L;

        // Create a File object for the given filepath
        File file = new File(filepath);
        if (!file.exists() || file.isDirectory()) {
            System.out.println("Error: File not found or invalid file path: " + filepath);
            return false;
        }

        // Use try-with-resources to automatically close the BufferedReader
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();
                // Skip empty lines or lines with only commas/spaces
                if (trimmedLine.isEmpty() || trimmedLine.matches("^[,\\s]*$")) {
                    continue; // Skip this line silently
                }
                try {
                    // Parse an Employee object from the data line
                    Employee employee = Employee.fromDataString(trimmedLine);
                    employees.add(employee);
                    // Update nextId if employee's ID is greater or equal
                    if (employee.getId() >= nextId) {
                        nextId = employee.getId() + 1;
                    }
                } catch (Exception ex) {
                    // If parsing fails, skip the line and notify
                    System.err.println("Skipping invalid line: " + line);
                }
            }
            return true; // Return true if all lines processed successfully
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return false; // Return false if an exception occurs
        }
    }

    /**
     * Save employees to the default file.
     */
    public boolean saveEmployeesToFile() {
        // Call saveToFile() with the default filename
        return saveToFile(dataFile);
    }

    /**
     * Save employee data to a specified file.
     * @param filepath Path to the file to save data into.
     * @return true if saving is successful, false otherwise.
     */
    public boolean saveToFile(String filepath) {
        // Use try-with-resources to ensure writer is closed properly
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            // Loop through each employee and write their data string
            for (Employee employee : employees) {
                writer.write(employee.toDataString());
                writer.newLine(); // Add a new line after each employee
            }
            return true; // Indicate success
        } catch (IOException e) {
            // Handle I/O exceptions
            System.err.println("Error writing file: " + e.getMessage());
            return false; // Indicate failure
        }
    }

    /**
     * Add a new employee with a unique ID.
     * @return The newly created Employee object.
     */
    public Employee addEmployee(String name, String position, Double salary, LocalDate hireDate, String department, Boolean active) {
        Employee employee = new Employee(nextId++, name, position, salary, hireDate, department, active);
        employees.add(employee);
        return employee;
    }

    /**
     * Remove an employee by ID.
     * @param id Employee ID.
     * @return true if an employee was removed, false if not found.
     */
    public boolean removeEmployee(Long id) {
        // Remove employees matching the ID using lambda expression
        return employees.removeIf(employee -> employee.getId().equals(id));
    }

    /**
     * Find an employee by ID.
     * @param id Employee ID.
     * @return Optional containing Employee if found, empty otherwise.
     */
    public Optional<Employee> findById(Long id) {
        // Search using stream and return Optional
        return employees.stream().filter(employee -> employee.getId().equals(id)).findFirst();
    }

    /**
     * Update a specific field of an employee by ID.
     * @param id Employee ID.
     * @param field Field name to update (case-insensitive).
     * @param newValue New value for the field.
     * @return true if update was successful, false otherwise.
     */
    public boolean updateEmployee(Long id, String field, Object newValue) {
        // Find the employee by ID
        Optional<Employee> optionalEmployee = findById(id);
        if (optionalEmployee.isEmpty()) {
            return false; // Employee not found
        }

        Employee employee = optionalEmployee.get();

        // Use switch statement to determine which field to update
        switch (field.toLowerCase()) {
            case "name" -> employee.setName((String) newValue);
            case "position" -> employee.setPosition((String) newValue);
            case "salary" -> employee.setSalary((Double) newValue);
            case "hiredate" -> employee.setHireDate((LocalDate) newValue);
            case "department" -> employee.setDepartment((String) newValue);
            case "active" -> employee.setActive((Boolean) newValue);
            default -> { // Invalid field name
                return false;
            }
        }
        return true; // Update succeeded
    }

    /**
     * Generate a report grouping employees based on tenure categories.
     * @return Map with category names as keys and lists of employees as values.
     */
    public Map<String, List<Employee>> generateTenureReport() {
        // Initialize map with categories
        Map<String, List<Employee>> report = new HashMap<>();
        report.put("0-1 years", new ArrayList<>());
        report.put("1-5 years", new ArrayList<>());
        report.put("5+ years", new ArrayList<>());

        // Loop through each employee to categorize
        for (Employee employee : employees) {
            int years = employee.getTenureYears();
            // Determine category based on tenure
            String category = (years <= 1) ? "0-1 years" :
                    (years <= 5) ? "1-5 years" :
                            "5+ years";

            // Add employee to the appropriate category list
            report.get(category).add(employee);
        }
        return report; // Return the completed report
    }
}