package com.gohul.springSecurity.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RolesRepo extends JpaRepository<CustomerRoles,Long> {
    boolean existsByRoleId(Long no);
    List<CustomerRoles> findByCustomer(Customer customer);
}
