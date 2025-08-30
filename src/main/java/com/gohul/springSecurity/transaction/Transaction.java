package com.gohul.springSecurity.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gohul.springSecurity.accoount.Account;
import com.gohul.springSecurity.customer.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Transaction {
    @Id
    private Long transactionId;
    @ManyToOne
    @JoinColumn(name = "account_number")
    private Account account;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private LocalDateTime transactionDateTime;
    private String TransactionType;
    private Long TransactionAmount;
    private Long closingAmount;
    @JsonIgnore
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
}
