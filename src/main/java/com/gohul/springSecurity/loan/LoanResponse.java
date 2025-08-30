package com.gohul.springSecurity.loan;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record LoanResponse(
        Long loanNumber,
        Long customerId,
        LocalDate startDt,
        String loanType,
        Long totalLoan,
        Long amountPaid,
        Long outstandingAmount
) {
}
