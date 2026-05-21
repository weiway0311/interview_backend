package com.howord.backend.auth;

public class LoginRequestDTO {
	private String username;
    private String password;

    // Getter 和 Setter
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
