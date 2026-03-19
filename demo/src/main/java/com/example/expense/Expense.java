package com.example.expense;

import java.time.LocalDate;

public class Expense {
    private String description;
    private double amount;
    private String category;
    private LocalDate date;

    // Required empty constructor for JSON parsing
    public Expense() {}

    public Expense(String description, double amount, String category) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = LocalDate.now();
    }
    
    // Getters
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public LocalDate getDate() { return date; }

    // Setters
    public void setDescription(String description) { this.description = description; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setCategory(String category) { this.category = category; }
    public void setDate(LocalDate date) { this.date = date; }
}