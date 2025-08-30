package com.gohul.springSecurity.customer;

import java.util.List;

public record CustomerPlusRolesRequest(
        String username,
        String email,
        String password,
        Long mobileNumber,
        List<String> roles
) {
}
