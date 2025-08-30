package com.gohul.springSecurity.accoount;

public record AccountResponse(
        Long customerId,
        Long accountNumber,
        String accountType,
        String branchAddress
) {
}
