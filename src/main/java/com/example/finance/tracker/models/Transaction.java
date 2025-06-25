package com.example.finance.tracker.models;

import com.example.finance.tracker.models.Enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal; // Se não estiver usando BigDecimal, pode remover
import java.time.LocalDate;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "value")
    private Double value;

    @Column(name = "data")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}