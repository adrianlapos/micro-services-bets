package com.example.entity;

import com.example.commons.Role;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usersnew")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private double balance;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    @ElementCollection
    private List<Long> betslipIdList = new ArrayList<>();

    public User(){}
    public User(String username,String email,String password,Role role){
        this.username = username;
        this.email = email;
        this.password = password;
        this.balance = 0.0;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Long> getBetslipIdList() {
        return betslipIdList;
    }

    public void setBetslipList(List<Long> betslipIdList) {
        this.betslipIdList = betslipIdList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
