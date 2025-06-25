package com.example.finance.tracker.DTOs;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TransactionDTO {
    private String description;
    private Double value;
    private LocalDate date;
    private String type;
    private Long categoryId;
    private CategoryDTO category;
}