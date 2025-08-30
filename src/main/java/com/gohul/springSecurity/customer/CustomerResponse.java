package com.gohul.springSecurity.customer;

import java.util.List;

public record CustomerResponse(
        Long id,
        String username,
        String email,
        Long mobileNumber,
        List<String> roles
) {
}
