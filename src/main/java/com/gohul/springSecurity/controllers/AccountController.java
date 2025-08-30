package com.gohul.springSecurity.controllers;


import com.gohul.springSecurity.accoount.Account;
import com.gohul.springSecurity.accoount.AccountRepo;
import com.gohul.springSecurity.accoount.AccountResponse;
import com.gohul.springSecurity.customer.Customer;
import com.gohul.springSecurity.customer.CustomerRepo;
import com.gohul.springSecurity.loan.Loan;
import jakarta.persistence.PrePersist;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Persistent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.List;

@RestController
@RequestMapping("accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountRepo accRepo;
    private final CustomerRepo customerRepo;
    private static final SecureRandom random = new SecureRandom();

    @PostMapping("/myAccount")
    public ResponseEntity<?> saveAccount(@RequestBody Account account) {

        Long customerId = account.getCustomer().getCustomerId();
        var customer = customerRepo.findById(customerId).orElseThrow(() -> new RuntimeException("Username not found for the give id:: " + customerId));
        account.setCustomer(customer);
        if(account.getAccountNumber()==null)
            account.setAccountNumber(generateUniqueAccountNumber());
        Account savedAcc = accRepo.save(account);
        return ResponseEntity.ok(savedAcc);

    }

    @GetMapping("/myAccount")
    public ResponseEntity<?> getMyAccountDetails(@RequestParam Long id) {
        Customer customer = customerRepo.findById(id).orElseThrow(() -> new RuntimeException("customer id not found:: "+id));
        List<Account> account = accRepo.findByCustomer(customer);
        if (account != null) {
            AccountResponse response = toAccResponse(account.get(0));
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private Long generateUniqueAccountNumber() {
        long number;
        do {
            number = 1000000000000000L + (Math.abs(random.nextLong()) % 9000000000000000L);
        } while (accRepo.existsById(number)); // Retry if exists
        return number;
    }
    private AccountResponse toAccResponse(Account account)
    {
        return new AccountResponse(account.getCustomer().getCustomerId()
            ,account.getAccountNumber()
            ,account.getAccountType()
            ,account.getBranchAddress()
        );
    }
}
