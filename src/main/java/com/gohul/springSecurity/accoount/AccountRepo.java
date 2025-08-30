package com.gohul.springSecurity.accoount;

import com.gohul.springSecurity.customer.Customer;
import com.gohul.springSecurity.loan.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepo extends JpaRepository<Account, Long> {

    Account findByAccountNumber(Long accNumber);
    List<Account> findByCustomer(Customer customer);
}
