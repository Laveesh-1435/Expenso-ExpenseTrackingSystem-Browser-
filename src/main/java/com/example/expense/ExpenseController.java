package com.example.expense;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    // New: Endpoint to handle edits
    @PutMapping("/edit/{id}")
    public String editExpense(@PathVariable String id, @RequestBody Expense expense) {
        service.updateExpense(id, expense);
        return "✅ Expense Updated!";
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

    @GetMapping("/report/data")
    public Map<String, Double> getReportData() {
        return service.getMonthlyReportData();
    }

    @GetMapping(value = "/export/csv", produces = "text/csv")
    public ResponseEntity<String> exportCsv() {
        String csv = service.exportToCsv();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"expenses.csv\"")
                .body(csv);
    }
}