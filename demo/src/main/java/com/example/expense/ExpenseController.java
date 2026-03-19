package com.example.expense;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ExpenseController {

    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public String addExpense(@RequestBody Expense expense) {
        service.addExpense(expense);
        return "✅ Expense Added!";
    }

    @GetMapping("/today")
    public List<Expense> getToday() {
        return service.getTodayExpenses();
    }

    @PostMapping("/budget")
    public String setBudget(@RequestParam double amount) {
        service.setMonthlyBudget(amount);
        return "Budget updated";
    }

    @GetMapping("/budget/status")
    public String getBudgetStatus() {
        return service.getBudgetStatus();
    }

    @GetMapping("/report")
    public String getReport() {
        return service.getMonthlyReport();
    }
}