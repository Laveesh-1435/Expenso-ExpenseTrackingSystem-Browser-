package com.example.expense;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

// Change JpaRepository to MongoRepository, and the ID type from Long to String
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}