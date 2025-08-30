package com.gohul.springSecurity.controllers;


import com.gohul.springSecurity.customer.Customer;
import com.gohul.springSecurity.customer.CustomerRepo;
import com.gohul.springSecurity.loan.Loan;
import com.gohul.springSecurity.loan.LoanRepo;
import com.gohul.springSecurity.loan.LoanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("loans")
@RequiredArgsConstructor
public class LoansController {
    private final LoanRepo loanRepo;
    private static final SecureRandom random = new SecureRandom();
    private final CustomerRepo customerRepo;

    @GetMapping("/myLoan")
    public ResponseEntity<?> getMyLoans(@RequestParam long cusId)
    {
        try {
            Customer customer = customerRepo.findById(cusId).orElseThrow(() -> new RuntimeException("customer id not found:: " + customerRepo));
            List<Loan> loans = loanRepo.findByCustomer(customer);
            if (loans != null) {
                List<LoanResponse> loanResponses = loans.stream().map(this::toLoanRes).toList();
                return ResponseEntity.ok(loanResponses);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No loans for the given customer ID:: " + cusId);
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Json");
        }
    }

    @PostMapping("/myLoan")
    public ResponseEntity<?> saveLoan(@RequestBody Loan loan)
    {
        Long CusId = loan.getCustomer().getCustomerId();
        Customer customer = customerRepo.findById(CusId).orElseThrow(() -> new RuntimeException("customer not found:: "+CusId));
        loan.setLoanNumber(generateUniqueLoanNumber());
        Long loanNo=loanRepo.save(loan).getLoanNumber();
        return ResponseEntity.ok("Loan created with ID:: "+loanNo);
    }

    private Long generateUniqueLoanNumber() {
        long number;
        do {
            number = 1000000000000L + (Math.abs(random.nextLong()) % 9000000000000L);
        } while (loanRepo.existsById(number)); // Retry if exists
        return number;
    }

    private LoanResponse toLoanRes (Loan loan)
    {
        return new LoanResponse(
                loan.getLoanNumber()
                ,loan.getCustomer().getCustomerId()
                ,loan.getStartedDate()
                ,loan.getLoanType()
                ,loan.getTotalLoanAmount()
                ,loan.getAmountPaid()
                ,loan.getOutstandingAmount()
        );
    }
}
