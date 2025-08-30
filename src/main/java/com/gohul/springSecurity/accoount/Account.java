package com.gohul.springSecurity.accoount;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gohul.springSecurity.customer.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Account {
    @Id
    private Long accountNumber;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    private String accountType;
    private String branchAddress;
    @JsonIgnore
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;


}



