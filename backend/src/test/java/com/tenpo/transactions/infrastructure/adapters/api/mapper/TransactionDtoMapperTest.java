package com.tenpo.transactions.infrastructure.adapters.api.mapper;

import com.tenpo.transactions.domain.model.Transaction;
import com.tenpo.transactions.infrastructure.adapters.api.controller.dto.TransactionRequest;
import com.tenpo.transactions.infrastructure.adapters.api.controller.dto.TransactionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionDtoMapperTest {

    private TransactionDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TransactionDtoMapper();
    }

    @Test
    void testToDomainWithValidRequest() {
        LocalDateTime date = LocalDateTime.of(2024, 1, 15, 10, 0);

        TransactionRequest request = new TransactionRequest(
                100,
                "Store A",
                "John Doe",
                date
        );

        Transaction result = mapper.toDomain(request);

        assertNotNull(result);
        assertNull(result.id());
        assertEquals(100, result.amount());
        assertEquals("Store A", result.business());
        assertEquals("John Doe", result.tenpistaName());
        assertEquals(date, result.transactionDate());
    }

    @Test
    void testToResponseWithValidTransaction() {
        LocalDateTime date = LocalDateTime.of(2024, 1, 16, 15, 30);

        Transaction transaction = new Transaction(
                1L,
                50,
                "Store B",
                "Jane Doe",
            date,
            null,
            null
        );

        TransactionResponse result = mapper.toResponse(transaction);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(50, result.amount());
        assertEquals("Store B", result.business());
        assertEquals("Jane Doe", result.tenpistaName());
        assertEquals(date, result.transactionDate());
    }

    @Test
    void testToDomainPreservesRequestData() {
        LocalDateTime date = LocalDateTime.of(2024, 1, 17, 20, 0);

        TransactionRequest request = new TransactionRequest(
                250,
                "Store C",
                "Bob Smith",
                date
        );

        Transaction result = mapper.toDomain(request);

        assertEquals(request.amount(), result.amount());
        assertEquals(request.business(), result.business());
        assertEquals(request.tenpistaName(), result.tenpistaName());
        assertEquals(request.transactionDate(), result.transactionDate());
    }

    @Test
    void testToResponsePreservesTransactionData() {
        LocalDateTime date = LocalDateTime.of(2024, 1, 18, 8, 45);

        Transaction transaction = new Transaction(
                2L,
                175,
                "Store D",
                "Alice Johnson",
            date,
            null,
            null
        );

        TransactionResponse result = mapper.toResponse(transaction);

        assertEquals(transaction.id(), result.id());
        assertEquals(transaction.amount(), result.amount());
        assertEquals(transaction.business(), result.business());
        assertEquals(transaction.tenpistaName(), result.tenpistaName());
        assertEquals(transaction.transactionDate(), result.transactionDate());
    }
}
