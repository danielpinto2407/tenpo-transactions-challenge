package com.tenpo.transactions.domain.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void testTransactionWithValidAmount() {
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        Transaction transaction = new Transaction(1L, 100, "Business", "Tenpista", pastDate);
        assertEquals(100, transaction.amount());
    }

    @Test
    void testTransactionWithZeroAmount() {
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        Transaction transaction = new Transaction(1L, 0, "Business", "Tenpista", pastDate);
        assertEquals(0, transaction.amount());
    }

    @Test
    void testTransactionWithNegativeAmountThrowsException() {
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        assertThrows(IllegalArgumentException.class, () ->
            new Transaction(1L, -100, "Business", "Tenpista", pastDate)
        );
    }

    @Test
    void testTransactionWithFutureDateThrowsException() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        assertThrows(IllegalArgumentException.class, () ->
            new Transaction(1L, 100, "Business", "Tenpista", futureDate)
        );
    }

   @Test
    void testTransactionWithCurrentDateTimeIsValid() {
        LocalDateTime now = LocalDateTime.now();
        Transaction transaction = new Transaction(1L, 100, "Business", "Tenpista", now);
        assertEquals(now, transaction.transactionDate());
    }
}