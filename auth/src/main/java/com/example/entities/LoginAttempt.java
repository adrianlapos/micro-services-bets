package com.example.entities;

public class LoginAttempt {
    private String credential;
    private String password;
    private LOGIN_STATUS status;

    public LoginAttempt(String credential,String password){
        this.credential = credential;
        this.password = password;
        this.status = LOGIN_STATUS.UNSUCCESFUL;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LOGIN_STATUS getStatus() {
        return status;
    }

    public void setStatus(LOGIN_STATUS status) {
        this.status = status;
    }
}
