package com.gohul.springSecurity.controllers;


import com.gohul.springSecurity.card.Card;
import com.gohul.springSecurity.card.CardRepo;
import com.gohul.springSecurity.card.CardResponse;
import com.gohul.springSecurity.customer.Customer;
import com.gohul.springSecurity.customer.CustomerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.List;

@RestController
@RequestMapping("cards")
@RequiredArgsConstructor
public class CardController {
    private final CardRepo cardRepo;
    private final CustomerRepo customerRepo;
    private static final SecureRandom random = new SecureRandom();

    @PostMapping("/myCard")
    public ResponseEntity<?> saveCardDetails(@RequestBody Card card) {
        try{
            Long customerId = card.getCustomer().getCustomerId();
            Customer customer = customerRepo.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found for the given ID:: "+customerId));
            card.setCustomer(customer);
            Long cardNumber = generateUniqueCardNumber();
            if(card.getCardNumber() == null)
                card.setCardNumber(cardNumber);
            Card savedCard = cardRepo.save(card);
            return ResponseEntity.ok(savedCard);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid json format");
        }

    }

    @GetMapping("/myCard")
    public ResponseEntity<?> getMyCardDetails(@RequestParam Long cusID) {
        Customer customer = customerRepo.findById(cusID)
                .orElseThrow(() -> new RuntimeException("Customer not found for the given ID:: "+cusID));
        List<CardResponse> cards = cardRepo.findByCustomer(customer)
                .stream().map(this::toCardRes).toList();

        if (cards != null) {
            return ResponseEntity.ok(cards);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No card found for the given customer ID:: "+cusID);
        }
    }

    private Long generateUniqueCardNumber() {
        long min = 1000_0000_0000_0000L;
        long max = 9999_9999_9999_9999L;
        Long cardNumber;
        do {
            cardNumber = min + (long) (Math.random() * (max - min));
        } while (cardRepo.existsByCardNumber(cardNumber));
        return cardNumber;
    }

    private CardResponse toCardRes(Card card)
    {
        return new CardResponse(card.getCardNumber(),card.getCustomer().getCustomerId(),"savings",card.getTotalLimit(),
                card.getAmountUsed(),card.getAvailableAmount());
    }

}
