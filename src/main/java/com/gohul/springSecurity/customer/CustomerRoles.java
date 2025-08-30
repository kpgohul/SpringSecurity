package com.gohul.springSecurity.customer;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "customer_authority")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EntityListeners(AuditingEntityListener.class)
public class CustomerRoles {

    @Id
    private Long roleId;
    private String role;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
