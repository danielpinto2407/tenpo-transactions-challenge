package com.tenpo.transactions.application.port;

import com.tenpo.transactions.domain.model.Transaction;
import java.util.List;

public interface TransactionRepositoryPort {
    Transaction save(Transaction transaction);
    List<Transaction> findAll();
}

