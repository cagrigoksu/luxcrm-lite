package com.luxcrm.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";

    // one-arg constructor
    public AuthResponse(String token) {
        this.token = token;
    }
}
