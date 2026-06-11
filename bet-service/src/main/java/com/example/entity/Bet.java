package com.example.entity;

import com.example.commons.BetStatus;
import com.example.commons.BetslipStatus;
import com.example.commons.Guess;
import jakarta.persistence.*;
import org.apache.logging.log4j.util.Lazy;

@Entity
@Table(name = "bet")
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    private Long matchId;
    @Enumerated(value = EnumType.STRING)
    private Guess guess;
    @Enumerated(value = EnumType.STRING)
    private BetStatus betStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bet_betslip_id")
    private Betslip betslip;
    public Bet(){}

    public Bet(Long matchId,Guess guess){
        this.matchId = matchId;
        this.guess = guess;
        this.betStatus = BetStatus.PLACED;
    }

    public Long getId() {
        return id;
    }

    public Long getMatchId() {
        return matchId;
    }

    public Guess getGuess() {
        return guess;
    }

    public BetStatus getBetStatus() {
        return betStatus;
    }

    public Betslip getBetslip() {
        return betslip;
    }

    public void setBetslip(Betslip betslip) {
        this.betslip = betslip;
    }
}
