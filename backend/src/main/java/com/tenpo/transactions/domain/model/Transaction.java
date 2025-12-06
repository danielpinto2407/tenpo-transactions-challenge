package com.tenpo.transactions.domain.model;

import java.time.LocalDateTime;

public record Transaction(
        Long id,
        Integer amount,
        String business,
        String tenpistaName,
        LocalDateTime transactionDate
) {
    public Transaction {
        if (amount < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo.");
        }

        if (transactionDate.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha no puede ser mayor a la actual.");
        }
    }
}


