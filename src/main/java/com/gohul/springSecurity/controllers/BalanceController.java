package com.gohul.springSecurity.controllers;


import com.gohul.springSecurity.customer.Customer;
import com.gohul.springSecurity.customer.CustomerRepo;
import com.gohul.springSecurity.transaction.Transaction;
import com.gohul.springSecurity.transaction.TransactionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.List;

@RestController
@RequestMapping("balance")
@RequiredArgsConstructor
public class BalanceController {
    private final TransactionRepo transactionRepo;
    private final CustomerRepo customerRepo;
    private static final SecureRandom random = new SecureRandom();

    @GetMapping("/myBalance")
    public ResponseEntity<?> getMyAccount(@RequestParam long cusID)
    {
       try
       {
           Customer customer = customerRepo.findById(cusID).orElseThrow(() -> new RuntimeException("Customer not Found:: "+cusID));
           return ResponseEntity.ok(transactionRepo.findByCustomerOrderByTransactionDateTime(customer));
       }
       catch (Exception e)
       {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON");
       }
    }
    @PostMapping("/myBalance")
    public  ResponseEntity<?> saveBalance(@RequestBody Transaction transaction)
    {
       try
       {
           Long cusID = transaction.getCustomer().getCustomerId();
           Customer customer = customerRepo.findById(cusID).orElseThrow(() -> new RuntimeException("Customer not Found:: "+cusID));
           transaction.setCustomer(customer);
           transaction.setTransactionId(generateUniqueTransactionNumber());
           return ResponseEntity.ok(transactionRepo.save(transaction).getTransactionId());
       }
       catch (Exception e)
       {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON --> "+e.getMessage());

       }
    }

    private Long generateUniqueTransactionNumber() {
        long number;
        do {
            number = 1000000000000000L + (Math.abs(random.nextLong()) % 9000000000000000L);
        } while (transactionRepo.existsById(number)); // Retry if exists
        return number;
    }
}
