package com.tenpo.transactions.infrastructure.adapters.api.controller.dto;

import java.util.List;

public record PaginatedTransactionResponse(
        List<TransactionResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {}
