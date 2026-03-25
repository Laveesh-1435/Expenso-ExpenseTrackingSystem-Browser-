package com.example.expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, String> {
    // Custom query to find expenses for a specific user on a specific date
    List<Expense> findByUserAndDate(User user, LocalDate date);
    
    // Find all expenses for a specific user
    List<Expense> findByUser(User user);
}