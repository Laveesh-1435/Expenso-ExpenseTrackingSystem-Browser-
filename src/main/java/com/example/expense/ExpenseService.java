package com.example.expense;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class ExpenseService {
    private final List<Expense> expenses = new ArrayList<>();
    private double monthlyBudget = 0;

    private double convertToINR(double amount, String currency) {
        if (currency == null)
            return amount;
        return switch (currency.toUpperCase()) {
            case "USD" -> amount * 83.0;
            case "EUR" -> amount * 90.0;
            case "GBP" -> amount * 105.0;
            default -> amount;
        }; // Assuming INR
    }

    public void addExpense(Expense expense) {
        if (expense.getDate() == null) {
            expense.setDate(LocalDate.now());
        }
        if (expense.getCurrency() == null) {
            expense.setCurrency("INR");
        }
        expenses.add(expense);
    }

    // New: Update existing expense logic
    public void updateExpense(String id, Expense updatedExpense) {
        for (Expense e : expenses) {
            if (e.getId().equals(id)) {
                e.setDescription(updatedExpense.getDescription());
                e.setAmount(updatedExpense.getAmount());
                e.setCategory(updatedExpense.getCategory());
                e.setCurrency(updatedExpense.getCurrency());
                break;
            }
        }
    }

    public List<Expense> getTodayExpenses() {
        LocalDate today = LocalDate.now();
        return expenses.stream()
                .filter(e -> e.getDate().equals(today))
                .collect(Collectors.toList());
    }

    public void setMonthlyBudget(double budget) {
        this.monthlyBudget = budget;
    }

    public String getBudgetStatus() {
        if (monthlyBudget == 0)
            return "❌ No budget set! Use 'Set Budget'.";

        double monthlySpent = expenses.stream()
                .filter(e -> YearMonth.from(e.getDate()).equals(YearMonth.now()))
                .mapToDouble(e -> convertToINR(e.getAmount(), e.getCurrency()))
                .sum();

        double remaining = monthlyBudget - monthlySpent;
        double percentage = (monthlySpent / monthlyBudget) * 100;

        StringBuilder status = new StringBuilder();
        status.append(String.format("Monthly Budget: ₹%.2f\n", monthlyBudget));
        status.append(String.format("Spent (Converted to INR): ₹%.2f (%.1f%%)\n", monthlySpent, percentage));
        status.append(String.format("Remaining: ₹%.2f\n\n", remaining));

        if (percentage > 90)
            status.append("⚠️ CRITICAL - Budget almost gone!\n");
        else if (percentage > 75)
            status.append("🔶 HIGH usage - Be careful!\n");
        else
            status.append("✅ Good budget control!\n");

        return status.toString();
    }

    public Map<String, Double> getMonthlyReportData() {
        YearMonth currentMonth = YearMonth.now();
        return expenses.stream()
                .filter(e -> YearMonth.from(e.getDate()).equals(currentMonth))
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(e -> convertToINR(e.getAmount(), e.getCurrency()))));
    }

    public String getMonthlyReport() {
        Map<String, Double> categoryTotals = getMonthlyReportData();
        if (categoryTotals.isEmpty())
            return "No expenses for this month yet.";

        double totalSpent = categoryTotals.values().stream().mapToDouble(Double::doubleValue).sum();

        StringBuilder report = new StringBuilder();
        report.append(String.format("Total Spent (in INR): ₹%.2f\n\n🏷️ Category Breakdown:\n", totalSpent));

        categoryTotals.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(entry -> report.append(String.format("%s: ₹%.2f (%.1f%%)\n",
                        entry.getKey(), entry.getValue(), (entry.getValue() / totalSpent) * 100)));

        return report.toString();
    }

    public String exportToCsv() {
        StringBuilder csv = new StringBuilder("Date,Category,Description,Amount,Currency,ConvertedAmount(INR)\n");
        for (Expense e : expenses) {
            csv.append(e.getDate()).append(",")
                    .append(e.getCategory().replace(",", " ")).append(",")
                    .append(e.getDescription().replace(",", " ")).append(",")
                    .append(e.getAmount()).append(",")
                    .append(e.getCurrency()).append(",")
                    .append(convertToINR(e.getAmount(), e.getCurrency())).append("\n");
        }
        return csv.toString();
    }
}