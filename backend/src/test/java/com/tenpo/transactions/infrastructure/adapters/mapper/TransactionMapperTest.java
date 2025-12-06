package com.tenpo.transactions.infrastructure.adapters.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tenpo.transactions.domain.model.Transaction;
import com.tenpo.transactions.infrastructure.adapters.jpa.TransactionJpaEntity;

class TransactionMapperTest {

    private TransactionMapper transactionMapper;

    private TransactionJpaEntity jpaEntity;
    private Transaction transaction;
    private LocalDateTime date;

    @BeforeEach
    void setUp() {
        transactionMapper = new TransactionMapper();

        date = LocalDateTime.of(2024, 1, 1, 10, 0);

        jpaEntity = TransactionJpaEntity.builder()
                .id(1L)
                .amount(100) // Integer
                .business("Test Business")
                .tenpistaName("John Doe")
                .transactionDate(date)
                .build();

        transaction = new Transaction(
                1L,
                100, // Integer
                "Test Business",
                "John Doe",
                date
        );
    }

    @Test
    void testToDomain() {
        Transaction result = transactionMapper.toDomain(jpaEntity);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(100, result.amount());
        assertEquals("Test Business", result.business());
        assertEquals("John Doe", result.tenpistaName());
        assertEquals(date, result.transactionDate());
    }

    @Test
    void testToEntity() {
        TransactionJpaEntity result = transactionMapper.toEntity(transaction);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100, result.getAmount());
        assertEquals("Test Business", result.getBusiness());
        assertEquals("John Doe", result.getTenpistaName());
        assertEquals(date, result.getTransactionDate());
    }
}
