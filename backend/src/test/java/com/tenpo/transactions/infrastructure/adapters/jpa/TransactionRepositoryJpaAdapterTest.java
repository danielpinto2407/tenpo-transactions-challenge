package com.tenpo.transactions.infrastructure.adapters.jpa;

import com.tenpo.transactions.application.port.TransactionRepositoryPort;
import com.tenpo.transactions.domain.model.Transaction;
import com.tenpo.transactions.infrastructure.adapters.mapper.TransactionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TransactionRepositoryJpaAdapterTest {

    private TransactionJpaRepository repository;
    private TransactionMapper mapper;
    private TransactionRepositoryJpaAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = mock(TransactionJpaRepository.class);
        mapper = mock(TransactionMapper.class);
        adapter = new TransactionRepositoryJpaAdapter(repository, mapper);
    }

    @Test
    void save_ShouldMapToEntityAndSaveAndMapToDomain() {
        Transaction domainTransaction = mock(Transaction.class);
        TransactionJpaEntity entity = mock(TransactionJpaEntity.class);
        TransactionJpaEntity savedEntity = mock(TransactionJpaEntity.class);
        Transaction domainResult = mock(Transaction.class);

        when(mapper.toEntity(domainTransaction)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(domainResult);

        Transaction result = adapter.save(domainTransaction);

        assertEquals(domainResult, result);
        verify(mapper).toEntity(domainTransaction);
        verify(repository).save(entity);
        verify(mapper).toDomain(savedEntity);
    }

    @Test
    void findAll_ShouldReturnMappedDomainTransactions() {
        TransactionJpaEntity entity1 = mock(TransactionJpaEntity.class);
        TransactionJpaEntity entity2 = mock(TransactionJpaEntity.class);
        Transaction domain1 = mock(Transaction.class);
        Transaction domain2 = mock(Transaction.class);

        when(repository.findAll()).thenReturn(Arrays.asList(entity1, entity2));
        when(mapper.toDomain(entity1)).thenReturn(domain1);
        when(mapper.toDomain(entity2)).thenReturn(domain2);

        List<Transaction> result = adapter.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(domain1));
        assertTrue(result.contains(domain2));
        verify(repository).findAll();
        verify(mapper).toDomain(entity1);
        verify(mapper).toDomain(entity2);
    }
}