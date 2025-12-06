package com.tenpo.transactions.infrastructure.adapters.api.controller.dto;

import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        Integer amount,
        String business,
        String tenpistaName,
        LocalDateTime transactionDate
) {}

