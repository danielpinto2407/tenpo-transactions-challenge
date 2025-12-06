package com.tenpo.transactions.infrastructure.adapters.mapper;

import org.springframework.stereotype.Component;

import com.tenpo.transactions.domain.model.Transaction;
import com.tenpo.transactions.infrastructure.adapters.jpa.TransactionJpaEntity;

@Component
public class TransactionMapper {

    public Transaction toDomain(TransactionJpaEntity e) {
        return new Transaction(
                e.getId(),
                e.getAmount(),
                e.getBusiness(),
                e.getTenpistaName(),
                e.getTransactionDate()
        );
    }

    public TransactionJpaEntity toEntity(Transaction t) {
        return TransactionJpaEntity.builder()
                .id(t.id())
                .amount(t.amount())
                .business(t.business())
                .tenpistaName(t.tenpistaName())
                .transactionDate(t.transactionDate())
                .build();
    }
}

