package com.gohul.springSecurity.login;

public record LoginRequest(
        String email,
        String password
) {
}
