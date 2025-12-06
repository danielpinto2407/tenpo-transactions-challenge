package com.tenpo.transactions.infrastructure.adapters.api.controller;

import com.tenpo.transactions.application.service.TransactionService;
import com.tenpo.transactions.domain.model.Transaction;
import com.tenpo.transactions.infrastructure.adapters.api.controller.dto.TransactionRequest;
import com.tenpo.transactions.infrastructure.adapters.api.controller.dto.TransactionResponse;
import com.tenpo.transactions.infrastructure.adapters.api.mapper.TransactionDtoMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService service;

    @Mock
    private TransactionDtoMapper mapper;

    @InjectMocks
    private TransactionController controller;

    private TransactionRequest request;
    private TransactionResponse response;

    @BeforeEach
    void setUp() {
        request = mock(TransactionRequest.class);
        response = mock(TransactionResponse.class);
    }

    @Test
    void testCreateTransactionSuccess() {
        var domainTransaction = mock(Transaction.class);
        var createdTransaction = mock(Transaction.class);

        when(mapper.toDomain(request)).thenReturn(domainTransaction);
        when(service.create(domainTransaction)).thenReturn(createdTransaction);
        when(mapper.toResponse(createdTransaction)).thenReturn(response);
        org.springframework.http.ResponseEntity<TransactionResponse> result = controller.create(request);

        assertNotNull(result);
        assertEquals(201, result.getStatusCodeValue());
        assertSame(response, result.getBody());
        verify(mapper).toDomain(request);
        verify(service).create(domainTransaction);
        verify(mapper).toResponse(createdTransaction);
    }

    @Test
    void testListAllTransactions() {
        var tx1 = mock(Transaction.class);
        var tx2 = mock(Transaction.class);
        var resp1 = mock(TransactionResponse.class);
        var resp2 = mock(TransactionResponse.class);

        when(service.listAll()).thenReturn(List.of(tx1, tx2));
        when(mapper.toResponse(tx1)).thenReturn(resp1);
        when(mapper.toResponse(tx2)).thenReturn(resp2);

        List<TransactionResponse> results = controller.listAll();

        assertEquals(2, results.size());
        verify(service).listAll();
        verify(mapper, times(2)).toResponse(any());
    }

    @Test
    void testListAllTransactionsEmpty() {
        when(service.listAll()).thenReturn(List.of());

        List<TransactionResponse> results = controller.listAll();

        assertTrue(results.isEmpty());
        verify(service).listAll();
        verify(mapper, never()).toResponse(any());
    }
}
