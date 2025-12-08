package com.tenpo.transactions.infrastructure.adapters.api.controller.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TransactionResponseTest {

    @Test
    void testTransactionResponseRecordFields() {
        Long id = 1L;
        Integer amount = 1000;
        String business = "Coffee Shop";
        String tenpistaName = "Alice";
        LocalDateTime transactionDate = LocalDateTime.now();

        TransactionResponse response = new TransactionResponse(id, amount, business, tenpistaName, transactionDate, transactionDate, transactionDate);

        assertEquals(id, response.id());
        assertEquals(amount, response.amount());
        assertEquals(business, response.business());
        assertEquals(tenpistaName, response.tenpistaName());
        assertEquals(transactionDate, response.transactionDate());
    }

    @Test
    void testTransactionResponseEquality() {
        Long id = 2L;
        Integer amount = 500;
        String business = "Bookstore";
        String tenpistaName = "Bob";
        LocalDateTime transactionDate = LocalDateTime.now();

        TransactionResponse response1 = new TransactionResponse(id, amount, business, tenpistaName, transactionDate, transactionDate, transactionDate);
        TransactionResponse response2 = new TransactionResponse(id, amount, business, tenpistaName, transactionDate, transactionDate, transactionDate);

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testTransactionResponseToString() {
        TransactionResponse response = new TransactionResponse(3L, 200, "Bakery", "Carol", LocalDateTime.of(2024, 1, 1, 12, 0), LocalDateTime.of(2024, 1, 1, 12, 0), LocalDateTime.of(2024, 1, 1, 12, 0));
        String toString = response.toString();
        assertTrue(toString.contains("3"));
        assertTrue(toString.contains("200"));
        assertTrue(toString.contains("Bakery"));
        assertTrue(toString.contains("Carol"));
        assertTrue(toString.contains("2024-01-01T12:00"));
    }
}