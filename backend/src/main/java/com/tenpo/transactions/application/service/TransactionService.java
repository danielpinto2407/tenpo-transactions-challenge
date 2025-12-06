package com.tenpo.transactions.application.service;

import com.tenpo.transactions.domain.model.Transaction;
import java.util.List;

public interface TransactionService {

    Transaction create(Transaction transaction);

    List<Transaction> listAll();
}

