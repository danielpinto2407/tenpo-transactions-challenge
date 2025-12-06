package com.tenpo.transactions.application.service;

import com.tenpo.transactions.application.port.TransactionRepositoryPort;
import com.tenpo.transactions.domain.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceImplTest {

    private TransactionRepositoryPort port;
    private TransactionServiceImpl service;

    @BeforeEach
    void setUp() {
        port = mock(TransactionRepositoryPort.class);
        service = new TransactionServiceImpl(port);
    }

    private Transaction buildTransaction(Long id) {
        return new Transaction(
                id,
                100,                       // amount
                "Test description",
                "DEBIT",                   // type
                LocalDateTime.now()
        );
    }

    @Test
    void testCreateShouldSaveTransaction() {
        Transaction transaction = buildTransaction(1L);

        when(port.save(transaction)).thenReturn(transaction);

        Transaction result = service.create(transaction);

        assertEquals(transaction, result);
        verify(port, times(1)).save(transaction);
    }

    @Test
    void testListAllShouldReturnAllTransactions() {
        Transaction t1 = buildTransaction(1L);
        Transaction t2 = buildTransaction(2L);
        List<Transaction> transactions = Arrays.asList(t1, t2);

        when(port.findAll()).thenReturn(transactions);

        List<Transaction> result = service.listAll();

        assertEquals(transactions, result);
        assertEquals(2, result.size());
        verify(port, times(1)).findAll();
    }
}
