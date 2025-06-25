package com.example.finance.tracker.services;

import com.example.finance.tracker.DTOs.CategoryDTO;
import com.example.finance.tracker.DTOs.TransactionDTO;
import com.example.finance.tracker.models.Category;
import com.example.finance.tracker.models.Enums.TransactionType;
import com.example.finance.tracker.models.Transaction;
import com.example.finance.tracker.repositories.CategoryRepository;
import com.example.finance.tracker.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    // ***** MÉTODO ALTERADO *****
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TransactionDTO createTransaction(TransactionDTO transactionDto) {
        Transaction newTransaction = new Transaction();

        Category category = categoryRepository.findById(transactionDto.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada com ID: " + transactionDto.getCategoryId()));

        newTransaction.setDescription(transactionDto.getDescription());
        newTransaction.setValue(transactionDto.getValue());
        newTransaction.setDate(transactionDto.getDate());
        newTransaction.setCategory(category);
        newTransaction.setType(TransactionType.valueOf(transactionDto.getType().toUpperCase()));

        Transaction savedTransaction = transactionRepository.save(newTransaction);

        return convertToDTO(savedTransaction); // Converte a entidade salva para o DTO de resposta
    }

    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("Transação com ID " + id + " não encontrada!");
        }
        transactionRepository.deleteById(id);
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setCategoryId(transaction.getId());
        dto.setDescription(transaction.getDescription());
        dto.setValue(transaction.getValue());
        dto.setDate(transaction.getDate());
        dto.setType(transaction.getType().name());

        CategoryDTO categoryDto = new CategoryDTO();
        categoryDto.setId(transaction.getCategory().getId());
        categoryDto.setName(transaction.getCategory().getName());

        dto.setCategory(categoryDto);

        return dto;
    }
}