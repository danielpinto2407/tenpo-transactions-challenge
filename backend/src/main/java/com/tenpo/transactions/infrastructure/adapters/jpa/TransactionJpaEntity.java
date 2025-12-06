package com.tenpo.transactions.infrastructure.adapters.jpa;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transactions")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class TransactionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @jakarta.persistence.Column(nullable = false)
    private Integer amount;

    @jakarta.persistence.Column(length = 255)
    private String business;

    @jakarta.persistence.Column(length = 255)
    private String tenpistaName;

    private LocalDateTime transactionDate;
}

