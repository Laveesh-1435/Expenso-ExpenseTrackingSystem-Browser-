package com.example.expense;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

//import com.example.expense.Expense;

@Service
public class ExpenseService {
    private final List<Expense> expenses = new ArrayList<>();
    private double monthlyBudget = 0;

    public void addExpense(Expense expense) {
        if(expense.getDate() == null) {
            expense.setDate(LocalDate.now());
        }
        expenses.add(expense);
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

    // Returns a summary string mimicking your old GUI logic
    public String getBudgetStatus() {
        if (monthlyBudget == 0) return "❌ No budget set! Use 'Set Budget'.";
        
        double monthlySpent = expenses.stream()
            .filter(e -> YearMonth.from(e.getDate()).equals(YearMonth.now()))
            .mapToDouble(Expense::getAmount).sum();
            
        double remaining = monthlyBudget - monthlySpent;
        double percentage = (monthlySpent / monthlyBudget) * 100;
        
        StringBuilder status = new StringBuilder();
        status.append(String.format("Monthly Budget: ₹%.2f\n", monthlyBudget));
        status.append(String.format("Spent: ₹%.2f (%.1f%%)\n", monthlySpent, percentage));
        status.append(String.format("Remaining: ₹%.2f\n\n", remaining));
        
        if (percentage > 90) status.append("⚠️ CRITICAL - Budget almost gone!\n");
        else if (percentage > 75) status.append("🔶 HIGH usage - Be careful!\n");
        else status.append("✅ Good budget control!\n");
        
        return status.toString();
    }

    public String getMonthlyReport() {
        YearMonth currentMonth = YearMonth.now();
        List<Expense> monthlyExpenses = expenses.stream()
            .filter(e -> YearMonth.from(e.getDate()).equals(currentMonth))
            .collect(Collectors.toList());

        if (monthlyExpenses.isEmpty()) return "No expenses for this month yet.";

        double totalSpent = monthlyExpenses.stream().mapToDouble(Expense::getAmount).sum();
        Map<String, Double> categoryTotals = monthlyExpenses.stream()
            .collect(Collectors.groupingBy(Expense::getCategory, Collectors.summingDouble(Expense::getAmount)));

        StringBuilder report = new StringBuilder();
        report.append(String.format("Total Spent: ₹%.2f\n\n🏷️ Category Breakdown:\n", totalSpent));
        
        categoryTotals.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .forEach(entry -> report.append(String.format("%s: ₹%.2f (%.1f%%)\n", 
                entry.getKey(), entry.getValue(), (entry.getValue() / totalSpent) * 100)));
                
        return report.toString();
    }
}