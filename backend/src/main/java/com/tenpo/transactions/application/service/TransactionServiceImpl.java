package com.tenpo.transactions.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tenpo.transactions.application.port.TransactionRepositoryPort;
import com.tenpo.transactions.domain.model.Transaction;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepositoryPort port;

    @Override
    public Transaction create(Transaction transaction) {
        return port.save(transaction);
    }

    @Override
    public List<Transaction> listAll(){
        return port.findAll();
    }
}

