package com.gohul.springSecurity.card;

public record CardResponse(
        Long cardNumber,
        Long customerId,
        String cardType,
        Long totalLimit,
        Long amountUsed,
        Long availableAmount
) {
}
