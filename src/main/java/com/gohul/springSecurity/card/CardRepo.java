package com.gohul.springSecurity.card;

import com.gohul.springSecurity.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepo extends JpaRepository<Card, Long> {

     List<Card> findByCustomer(Customer customer);
     boolean existsByCardNumber(Long cardNumber);
     Card findByCardNumber(Long cardNumber);
}
