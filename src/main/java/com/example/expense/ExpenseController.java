package com.example.expense;

import java.security.Principal;
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
    public String addExpense(@RequestBody Expense expense, Principal principal) {
        // Pass the username to the service
        service.addExpense(expense, principal.getName());
        return "✅ Expense Added!";
    }

    @PutMapping("/edit/{id}")
    public String editExpense(@PathVariable String id, @RequestBody Expense expense, Principal principal) {
        service.updateExpense(id, expense, principal.getName());
        return "✅ Expense Updated!";
    }

    @GetMapping("/today")
    public List<Expense> getToday(Principal principal) {
        return service.getTodayExpenses(principal.getName());
    }

    @PostMapping("/budget")
    public String setBudget(@RequestParam double amount, Principal principal) { // Added Principal
        service.setMonthlyBudget(amount, principal.getName());
        return "Budget updated";
    }

    @GetMapping("/budget/status")
    public String getBudgetStatus(Principal principal) { // Added Principal
        return service.getBudgetStatus(principal.getName());
    }

    @GetMapping("/report")
    public String getReport(Principal principal) { // Added Principal
        return service.getMonthlyReport(principal.getName());
    }

    @GetMapping("/report/data")
    public Map<String, Double> getReportData(Principal principal) { // Added Principal
        return service.getMonthlyReportData(principal.getName());
    }

    @GetMapping(value = "/export/csv", produces = "text/csv")
    public ResponseEntity<String> exportCsv(Principal principal) { // Added Principal
        String csv = service.exportToCsv(principal.getName());
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"expenses.csv\"")
                .body(csv);
    }
}