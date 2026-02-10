package com.mvgore.garageapi.auth;

public class AuthResponse {
    private String token;
    private String roleOrMessage;

    public AuthResponse(String token, String roleOrMessage){
        this.token = token;
        this.roleOrMessage = roleOrMessage;
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRoleOrMessage() { return roleOrMessage; }
    public void setRoleOrMessage(String roleOrMessage) { this.roleOrMessage = roleOrMessage; }
}
