package com.example.entity;

import com.example.commons.BetslipStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Betslip{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ElementCollection
    @CollectionTable(name = "betslip_bets",joinColumns = @JoinColumn(name="betslip_id"))
    private List<Long> bets = new ArrayList<>();
    private LocalDateTime createdAt;
    private Long userId;
    private double stake;
    @Enumerated(EnumType.STRING)
    private BetslipStatus betslipStatus;

    public Betslip(){}

    public Betslip(Long userId,double stake){
        this.createdAt = LocalDateTime.now();
        this.userId = userId;
        this.stake = stake;
        this.betslipStatus = BetslipStatus.CREATED;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getBets() {
        return bets;
    }

    public void setBets(List<Long> bets) {
        this.bets = bets;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getStake() {
        return stake;
    }

    public void setStake(double stake) {
        this.stake = stake;
    }

    public BetslipStatus getBetslipStatus() {
        return betslipStatus;
    }

    public void setBetslipStatus(BetslipStatus betslipStatus) {
        this.betslipStatus = betslipStatus;
    }
}
