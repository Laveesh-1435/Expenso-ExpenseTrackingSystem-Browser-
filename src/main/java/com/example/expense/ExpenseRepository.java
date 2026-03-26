package com.example.expense;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

// Change JpaRepository to MongoRepository
public interface ExpenseRepository extends MongoRepository<Expense, String> {
    List<Expense> findByUserAndDate(User user, LocalDate date);

    List<Expense> findByUser(User user);
}