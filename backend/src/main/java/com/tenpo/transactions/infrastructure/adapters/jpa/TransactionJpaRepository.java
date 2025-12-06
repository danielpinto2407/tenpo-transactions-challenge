package com.tenpo.transactions.infrastructure.adapters.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionJpaRepository extends JpaRepository<TransactionJpaEntity, Long> {}

