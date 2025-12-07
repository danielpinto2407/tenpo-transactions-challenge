package com.tenpo.transactions.application.service;

import com.tenpo.transactions.domain.model.Transaction;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    Transaction create(Transaction transaction);

    List<Transaction> listAll();

    Page<Transaction> list(Pageable pageable);
}

