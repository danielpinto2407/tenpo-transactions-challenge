package com.tenpo.transactions.infrastructure.adapters.jpa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.tenpo.transactions.application.port.TransactionRepositoryPort;
import com.tenpo.transactions.domain.model.Transaction;
import com.tenpo.transactions.infrastructure.adapters.mapper.TransactionMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransactionRepositoryJpaAdapter implements TransactionRepositoryPort {

    private final TransactionJpaRepository repository;
    private final TransactionMapper mapper;

    @Override
    public Transaction save(Transaction transaction) {
        TransactionJpaEntity entity = mapper.toEntity(transaction);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public List<Transaction> findAll() {
        return repository.findAll(Sort.by("createdAt").descending())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Page<Transaction> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDomain);
    }
}
