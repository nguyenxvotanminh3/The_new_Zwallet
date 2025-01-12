package com.nguyenminh.microservices.zwallet.configuration;

public class JwtResponse {
    private String token; // JWT
    private String type = "Bearer";
    private String userName;
    private String roles;



    public JwtResponse(String accessToken, String userName, String roles) {
        this.token = accessToken;
        this.userName = userName;
        this.roles = roles;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return userName;
    }

    public String getRoles() {
        return roles;
    }
}
