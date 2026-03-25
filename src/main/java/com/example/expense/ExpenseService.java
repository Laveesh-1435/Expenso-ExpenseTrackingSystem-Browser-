package com.example.expense;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    @SuppressWarnings("unused")
    private final double monthlyBudget = 0;

    // Inject both repositories
    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    // Helper method to link the authenticated session user to the database user
    // Replace the old getOrCreateDbUser method with this simpler one:
    private User getDbUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Remember to update addExpense, updateExpense, and getTodayExpenses
    // to call getDbUser(username) instead of getOrCreateDbUser.

    public void addExpense(Expense expense, String username) {
        User user = getDbUser(username);
        expense.setUser(user);

        if (expense.getDate() == null) {
            expense.setDate(LocalDate.now());
        }
        if (expense.getCurrency() == null) {
            expense.setCurrency("INR");
        }
        expenseRepository.save(expense);
    }

    public void updateExpense(String id, Expense updatedExpense, String username) {
        User user = getDbUser(username);
        Expense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        // Ensure the user modifying it owns it
        if (existing.getUser().getId().equals(user.getId())) {
            existing.setDescription(updatedExpense.getDescription());
            existing.setAmount(updatedExpense.getAmount());
            existing.setCategory(updatedExpense.getCategory());
            existing.setCurrency(updatedExpense.getCurrency());
            expenseRepository.save(existing);
        }
    }

    public List<Expense> getTodayExpenses(String username) {
        User user = getDbUser(username);
        return expenseRepository.findByUserAndDate(user, LocalDate.now());
    }

    // ... (Keep getDbUser, addExpense, updateExpense, and getTodayExpenses as they
    // are) ...

    public void setMonthlyBudget(double amount, String username) {
        User user = getDbUser(username);
        user.setMonthlyBudget(amount);
        userRepository.save(user); // Save the new budget to the database
    }

    public String getBudgetStatus(String username) {
        User user = getDbUser(username);
        YearMonth currentMonth = YearMonth.now();

        // Calculate total spent this month
        double totalSpentThisMonth = expenseRepository.findByUser(user).stream()
                .filter(e -> YearMonth.from(e.getDate()).equals(currentMonth))
                .mapToDouble(Expense::getAmount)
                .sum();

        double budget = user.getMonthlyBudget();
        double remaining = budget - totalSpentThisMonth;

        // Format the output for the UI
        String status = remaining < 0 ? "⚠️ OVER BUDGET!" : "✅ On Track";

        return String.format("""
                Monthly Budget Limit: \u20b9%.2f
                Total Spent This Month: \u20b9%.2f
                Remaining Balance: \u20b9%.2f
                Status: %s""",
                budget, totalSpentThisMonth, remaining, status);
    }

    public Map<String, Double> getMonthlyReportData(String username) {
        User user = getDbUser(username);
        YearMonth currentMonth = YearMonth.now();

        // Group expenses by category and sum the amounts for the Pie Chart
        return expenseRepository.findByUser(user).stream()
                .filter(e -> YearMonth.from(e.getDate()).equals(currentMonth))
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)));
    }

    public String getMonthlyReport(String username) {
        Map<String, Double> data = getMonthlyReportData(username);
        if (data.isEmpty()) {
            return "No expenses recorded this month yet.";
        }

        StringBuilder report = new StringBuilder("Category Breakdown for Current Month:\n\n");
        data.forEach((category, amount) -> report.append(String.format("- %s: ₹%.2f\n", category, amount)));
        return report.toString();
    }

    public String exportToCsv(String username) {
        User user = getDbUser(username);
        List<Expense> expenses = expenseRepository.findByUser(user);

        StringBuilder csv = new StringBuilder("ID,Date,Description,Category,Currency,Amount\n");
        for (Expense e : expenses) {
            csv.append(String.format("%s,%s,%s,%s,%s,%.2f\n",
                    e.getId(), e.getDate(), e.getDescription().replace(",", " "),
                    e.getCategory(), e.getCurrency(), e.getAmount()));
        }
        return csv.toString();
    }
}