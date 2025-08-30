package com.gohul.springSecurity.transaction;

import com.gohul.springSecurity.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction,Long> {

    List<Transaction> findByCustomerOrderByTransactionDateTime(Customer customer);

}
