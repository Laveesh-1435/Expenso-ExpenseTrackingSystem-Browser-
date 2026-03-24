package com.example.expense;

import java.time.LocalDate;
import java.util.UUID;

public class Expense {
    private String id; // New unique ID field
    private String description;
    private double amount;
    private String category;
    private String currency;
    private LocalDate date;

    public Expense() {
        this.id = UUID.randomUUID().toString(); // Auto-generate ID on creation
    }

    public Expense(String description, double amount, String category, String currency) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.currency = currency != null ? currency : "INR";
        this.date = LocalDate.now();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDate getDate() {
        return date;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}