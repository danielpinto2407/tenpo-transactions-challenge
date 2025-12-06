package com.tenpo.transactions.infrastructure.adapters.api.mapper;

import com.tenpo.transactions.domain.model.Transaction;
import com.tenpo.transactions.infrastructure.adapters.api.controller.dto.TransactionRequest;
import com.tenpo.transactions.infrastructure.adapters.api.controller.dto.TransactionResponse;

import org.springframework.stereotype.Component;

@Component
public class TransactionDtoMapper {

    public Transaction toDomain(TransactionRequest request) {
        return new Transaction(
                null,                                   
                request.amount(),
                request.business(),
                request.tenpistaName(),
                request.transactionDate()
        );
    }

    public TransactionResponse toResponse(Transaction domain) {
        return new TransactionResponse(
                domain.id(),
                domain.amount(),
                domain.business(),
                domain.tenpistaName(),
                domain.transactionDate()
        );
    }
}

