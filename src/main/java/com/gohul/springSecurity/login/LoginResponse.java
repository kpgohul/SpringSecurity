package com.gohul.springSecurity.login;

public record LoginResponse(
        String status,
        String JwtToken
) {
}
