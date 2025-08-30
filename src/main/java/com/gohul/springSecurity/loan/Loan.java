package com.gohul.springSecurity.loan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gohul.springSecurity.customer.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Loan {
    @Id
    private Long loanNumber;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private LocalDate startedDate;
    private String loanType;
    private Long totalLoanAmount;
    private Long amountPaid;
    private Long outstandingAmount;
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

}
