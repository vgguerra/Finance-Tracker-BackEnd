package com.example.finance.tracker.services;

import com.example.finance.tracker.models.Category;
import com.example.finance.tracker.models.Transaction;
import com.example.finance.tracker.repositories.CategoryRepository;
import com.example.finance.tracker.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction createTransaction(Transaction transaction) {
        // --- LÓGICA DE NEGÓCIO IMPORTANTE ---
        // O objeto 'transaction' que chega aqui do front-end terá um objeto 'category'
        // que contém apenas o ID. Precisamos buscar a entidade Category completa no banco.

        // 1. Pega o ID da categoria que veio na requisição
        Long categoryId = transaction.getCategory().getId();

        // 2. Busca a entidade Category completa no banco de dados usando o ID
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Categoria com ID " + categoryId + " não encontrada!")); // Lança uma exceção se não achar

        // 3. Agora, associa a categoria REAL (gerenciada pelo JPA) à transação
        transaction.setCategory(category);

        // 4. Finalmente, salva a transação completa
        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("Transação com ID " + id + " não encontrada!");
        }
        transactionRepository.deleteById(id);
    }
}