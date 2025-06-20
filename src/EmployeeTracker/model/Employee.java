// Package declaration: this class belongs to the model package of EmployeeTracker
package EmployeeTracker.model;

/**
 * Employee.java
 *
 *  *  Professor: Ashley Evans
 *  *  Student name: Minh Ngoc Tran
 *  *  Course: 202530-CEN-3024C-31774
 *  *  Date : Jun 18 - 2025
 *
 * This class models an Employee entity for the Employee Tracker system.
 *
 * Main responsibilities:
 * - Store employee information: ID, name, position, salary, hire date, department, and active status.
 * - Provide getters and fluent setters for each field.
 * - Calculate tenure (years worked) and classify tenure into categories.
 * - Support serialization (saving to string for file storage) via toDataString().
 * - Support deserialization (loading from file string) via fromDataString().
 * - Handle invalid input with custom InvalidEmployeeDataException.
 * - Override equals() and hashCode() for proper object comparison and collection usage.
 *
 * Features:
 * - Safe parsing of data with error handling for corrupted input.
 * - Null-safe date handling.
 * - Fluent setter design allows method chaining.
 * - Custom string formatting for easy display and file storage.
 *
 * Dependencies:
 * - java.time for date handling.
 * - java.util.Objects for object equality.
 */


// Import statements for handling dates, exceptions, and utility functions
import java.time.LocalDate; // Represents date objects
import java.time.format.DateTimeFormatter; // Formats and parses date strings
import java.time.format.DateTimeParseException; // Exception thrown if date parsing fails
import java.time.temporal.ChronoUnit; // For calculating units of time between dates
import java.util.Objects; // Utility class for object methods like equals and hashCode

/**
 * Represents an Employee with properties and behaviors.
 */
public class Employee {
    private Long id; // Unique identifier for employee
    private String name; // Employee's name
    private String position; // Job position
    private Double salary; // Salary amount
    private LocalDate hireDate; // Date of hiring
    private String department; // Department name
    private Boolean active; // Employment status: active/inactive
    private final DateTimeFormatter formatter; // Formatter for date strings, instance-level

    /**
     * Constructor with formatter initialization.
     * Initializes all properties and sets up date formatter.
     */
    public Employee(Long id, String name, String position, Double salary, LocalDate hireDate, String department, Boolean active) {
        this.id = id; // Assign provided id
        this.name = name; // Assign name
        this.position = position; // Assign position
        this.salary = salary; // Assign salary
        this.hireDate = hireDate; // Assign hire date
        this.department = department; // Assign department
        this.active = active; // Assign employment status
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Initialize date formatter
    }

    // Getter for id
    public Long getId() { return id; }
    // Fluent setter for id, returns Employee for chaining
    public Employee setId(Long id) { this.id = id; return this; }

    // Getter for name
    public String getName() { return name; }
    // Fluent setter for name
    public Employee setName(String name) { this.name = name; return this; }

    // Getter for position
    public String getPosition() { return position; }
    // Fluent setter for position
    public Employee setPosition(String position) { this.position = position; return this; }

    // Getter for salary
    public Double getSalary() { return salary; }
    // Fluent setter for salary
    public Employee setSalary(Double salary) { this.salary = salary; return this; }

    // Getter for hireDate
    public LocalDate getHireDate() { return hireDate; }
    // Fluent setter for hireDate
    public Employee setHireDate(LocalDate hireDate) { this.hireDate = hireDate; return this; }

    // Getter for department
    public String getDepartment() { return department; }
    // Fluent setter for department
    public Employee setDepartment(String department) { this.department = department; return this; }

    // Getter for active status
    public Boolean getActive() { return active; }
    // Fluent setter for active status
    public Employee setActive(Boolean active) { this.active = active; return this; }

    /**
     * Calculate the number of full years the employee has worked.
     * Uses ChronoUnit.YEARS to compute difference between hireDate and current date.
     * Returns 0 if hireDate is null.
     */
    public int getTenureYears() {
        if (hireDate == null) return 0; // Handle case where hireDate is null
        return (int) ChronoUnit.YEARS.between(hireDate, LocalDate.now()); // Calculate full years of service
    }

    /**
     * Categorize tenure into predefined ranges based on full years worked.
     */
    public String tenureCategory() {
        int years = getTenureYears(); // Get number of years employed
        if (years <= 1) return "0-1 years"; // 0-1 years category
        else if (years <= 5) return "1-5 years"; // 1-5 years category
        else return "5+ years"; // More than 5 years
    }

    /**
     * Convert employee data to a formatted string for display.
     * Uses String.format for cleaner output.
     */
    @Override
    public String toString() {
        return String.format(
                "ID: %d | Name: %s | Position: %s | Salary: %.2f | Hire Date: %s | Department: %s | Active: %s",
                id,
                name,
                position,
                salary,
                (hireDate == null ? "N/A" : hireDate.format(formatter)), // Format hireDate or show N/A
                department,
                active);
    }

    /**
     * Convert employee data to a CSV-like string for file storage.
     */
    public String toDataString() {
        return String.format("%d,%s,%s,%.2f,%s,%s,%s",
                id,
                name,
                position,
                salary,
                (hireDate == null ? "" : hireDate.format(formatter)), // Format date or empty string
                department,
                active);
    }

    /**
     * Create an Employee object from a CSV data string.
     * Parses string fields and converts to appropriate data types.
     * Throws InvalidEmployeeDataException if parsing fails.
     */
    public static Employee fromDataString(String line) throws InvalidEmployeeDataException {
        String[] fields = line.split(",", -1); // Split line into fields, -1 to include trailing empty strings
        if (fields.length != 7) { // Expect exactly 7 fields
            throw new InvalidEmployeeDataException("Invalid data line (wrong number of fields): " + line);
        }

        try {
            Long id = Long.parseLong(fields[0].trim()); // Parse ID
            String name = fields[1].trim(); // Parse name
            String position = fields[2].trim(); // Parse position
            Double salary = Double.parseDouble(fields[3].trim()); // Parse salary
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Date formatter
            LocalDate hireDate = fields[4].trim().isEmpty()
                    ? null // Empty string means no hire date
                    : LocalDate.parse(fields[4].trim(), formatter); // Parse date if present
            String department = fields[5].trim(); // Parse department
            Boolean active = Boolean.parseBoolean(fields[6].trim()); // Parse active status
            // Create and return new Employee object
            return new Employee(id, name, position, salary, hireDate, department, active);
        } catch (NumberFormatException | DateTimeParseException e) {
            // Throw custom exception if parsing fails
            throw new InvalidEmployeeDataException("Error parsing employee data: " + e.getMessage());
        }
    }

    // Override equals to compare employees based on id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Same object reference
        if (!(o instanceof Employee)) return false; // Not an Employee object
        Employee other = (Employee) o; // Cast to Employee
        return Objects.equals(id, other.id); // Check id equality
    }

    // Override hashCode to be consistent with equals
    @Override
    public int hashCode() {
        return Objects.hash(id); // Generate hash based on id
    }

    /**
     * Custom exception class for invalid employee data.
     */
    public static class InvalidEmployeeDataException extends Exception {
        public InvalidEmployeeDataException(String message) {
            super(message); // Call superclass constructor with message
        }
    }
}