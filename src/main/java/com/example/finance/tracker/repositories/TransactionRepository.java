package com.example.finance.tracker.repositories;

import com.example.finance.tracker.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t JOIN FETCH t.category")
    List<Transaction> findAllWithCategory();
}