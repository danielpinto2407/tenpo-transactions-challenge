package com.tenpo.transactions.application.port;

import com.tenpo.transactions.domain.model.Transaction;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionRepositoryPort {
    Transaction save(Transaction transaction);
    List<Transaction> findAll();
    Page<Transaction> findAll(Pageable pageable);
}

