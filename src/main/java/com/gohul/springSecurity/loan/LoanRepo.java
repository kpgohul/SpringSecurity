package com.gohul.springSecurity.loan;

import com.gohul.springSecurity.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepo extends JpaRepository<Loan,Long> {
    List<Loan> findByCustomer(Customer customer);

}
