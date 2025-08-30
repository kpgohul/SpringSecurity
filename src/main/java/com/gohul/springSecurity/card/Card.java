package com.gohul.springSecurity.card;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gohul.springSecurity.customer.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "cards")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Card {
    @Id
    private Long cardNumber;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private Long totalLimit;
    private Long amountUsed;
    private Long availableAmount;
    @JsonIgnore
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

}



