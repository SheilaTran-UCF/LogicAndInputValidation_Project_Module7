// Package declaration: this class belongs to the utils package of EmployeeTracker
package EmployeeTracker.utils;

/**
 * InputValidator.java
 *
 *  * Professor: Ashley Evans
 *  * Student name: Minh Ngoc Tran
 *  * Course: 202530-CEN-3024C-31774
 *  * Date : Jun 18 - 2025
 *
 * This utility class provides reusable methods to safely collect and validate user input from the console.
 * It helps ensure robust input validation and prevents invalid or unexpected data from being entered.
 *
 * Main Responsibilities:
 * - Prompt users for various types of input: String, Double, Long, LocalDate, Boolean, Integer.
 * - Validate input values and formats before returning.
 * - Handle invalid input gracefully with clear error messages.
 * - Provide menu choice validation within a specified range.
 * - Provide default file path prompt with fallback option.
 * - Standardize date input using the format yyyy-MM-dd.
 *
 * Features:
 * - Reusable input validation logic across the entire application.
 * - Prevents common input errors (empty strings, negative numbers, invalid dates, etc.).
 * - Keeps user interaction consistent and user-friendly.
 * - Reduces repetitive validation code in other parts of the system.
 *
 * Usage:
 * - Called by UI or main application classes whenever validated user input is required.
 *
 * Dependencies:
 * - java.util.Scanner for reading user input.
 * - java.time for date parsing and formatting.
 */


// Import necessary classes for date handling and user input
import java.time.LocalDate; // Represents date objects
import java.time.format.DateTimeFormatter; // Formats and parses dates
import java.time.format.DateTimeParseException; // Exception thrown for invalid date parsing
import java.util.Scanner; // For capturing user input from console

/**
 * Handles validated user input.
 */
public class InputValidator {
    // Scanner instance for reading user input from System.in
    private final Scanner scanner;

    // Static constant for date formatting, used throughout the class
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Constructor initializes the Scanner object.
     */
    public InputValidator() {
        this.scanner = new Scanner(System.in); // Create Scanner for console input
    }

    /**
     * Prompt the user for a non-empty string.
     * @param prompt Message to display to the user.
     * @return Validated non-empty string input.
     */
    public String getNonEmptyString(String prompt) {
        String input;
        do {
            System.out.print(prompt); // Display prompt message
            input = scanner.nextLine().trim(); // Read user input and trim whitespace
            if (input.isEmpty()) System.out.println("Input cannot be empty."); // Warn if input is empty
        } while (input.isEmpty()); // Repeat until non-empty input is received
        return input; // Return valid input
    }

    /**
     * Prompt the user for a positive double value.
     * @param prompt Message to display.
     * @return Validated non-negative double value.
     */
    public Double getPositiveDouble(String prompt) {
        Double value = null;
        while (value == null) { // Loop until valid input is obtained
            System.out.print(prompt); // Show prompt
            String input = scanner.nextLine().trim(); // Read input
            try {
                value = Double.parseDouble(input); // Convert input to Double
                if (value < 0) { // Check for non-negative
                    System.out.println("Value must be non-negative."); // Warn if negative
                    value = null; // Reset to retry
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid decimal number."); // Warn if input isn't a valid number
            }
        }
        return value; // Return validated number
    }

    /**
     * Prompt the user for a positive long value (e.g., ID).
     * @param prompt Message to display.
     * @return Validated positive long.
     */
    public Long getPositiveLong(String prompt) {
        Long value = null;
        while (value == null) { // Loop until valid input
            System.out.print(prompt); // Show prompt
            String input = scanner.nextLine().trim(); // Read input
            try {
                value = Long.parseLong(input); // Convert to Long
                if (value < 1) { // Check if positive
                    System.out.println("ID must be positive."); // Warn if not positive
                    value = null; // Reset for retry
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format."); // Warn if invalid number
            }
        }
        return value; // Return validated number
    }

    /**
     * Prompt the user for a date in the format yyyy-MM-dd.
     * @param prompt Message to display.
     * @return Validated LocalDate object.
     */
    public LocalDate getDate(String prompt) {
        LocalDate date = null;
        while (date == null) { // Loop until valid date
            System.out.print(prompt + " (yyyy-MM-dd): "); // Show prompt with date format
            String input = scanner.nextLine().trim(); // Read input
            try {
                date = LocalDate.parse(input, DATE_FORMATTER); // Parse date
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format."); // Warn if parsing fails
            }
        }
        return date; // Return valid date
    }

    /**
     * Prompt the user for a boolean value (true/false).
     * @param prompt Message to display.
     * @return Parsed boolean value.
     */
    public Boolean getBoolean(String prompt) {
        while (true) { // Loop until valid input
            System.out.print(prompt + " (true/false): "); // Show prompt
            String input = scanner.nextLine().trim().toLowerCase(); // Read input and convert to lowercase
            if ("true".equals(input)) return true; // Return true if input is "true"
            if ("false".equals(input)) return false; // Return false if input is "false"
            System.out.println("Please enter true or false."); // Warn if input is invalid
        }
    }

    /**
     * Prompt the user for a menu choice between min and max.
     * @param prompt Message to display.
     * @param min Minimum valid choice.
     * @param max Maximum valid choice.
     * @return Validated menu choice as int.
     */
    public int getMenuChoice(String prompt, int min, int max) {
        Integer choice = null;
        while (choice == null) { // Loop until valid choice
            System.out.print(prompt); // Show prompt
            String input = scanner.nextLine().trim(); // Read input
            try {
                choice = Integer.parseInt(input); // Convert to int
                if (choice < min || choice > max) { // Check bounds
                    System.out.printf("Choice must be between %d and %d.%n", min, max); // Warn if out of bounds
                    choice = null; // Reset for retry
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer input."); // Warn if not a number
            }
        }
        return choice; // Return validated choice
    }

    /**
     * Prompt for a file path, with default fallback.
     * @param prompt Message to display.
     * @return User-entered path or default if empty.
     */
    public String getFilePath(String prompt) {
        System.out.print(prompt + " (Press Enter for default): "); // Show prompt
        String path = scanner.nextLine().trim(); // Read input
        // Return user input if not empty, else default filename
        return path.isEmpty() ? "employees.txt" : path;
    }
}