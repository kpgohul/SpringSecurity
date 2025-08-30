package com.gohul.springSecurity.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer,Long> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByCustomerId(Long id);
    Optional<Customer> findByUsername(String name);
    Optional<Customer> findByMobileNumber(Long number);

    @Modifying
    @Query("UPDATE Customer c SET c.email = :email, c.mobileNumber = :mobileNumber, c.username = :username WHERE c.customerId = :customerId")
    int updateCustomerById(@Param("customerId") Long customerId,
                           @Param("email") String email,
                           @Param("mobileNumber") Long mobileNumber,
                           @Param("username") String username);

}
