package com.example.entity;

import com.example.commons.Result;
import com.example.models.MatchStatus;
import com.example.models.Status;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "home_team_id",nullable = false)
    private Team homeTeam;

    @ManyToOne(optional = false)
    @JoinColumn(name = "away_team_id",nullable = false)
    private Team awayTeam;
    @Enumerated(EnumType.STRING)
    private Result result;
    private double homeOdds;
    private double drawOdds;
    private double awayOdds;
    private int homeGoals;
    private int awayGoals;
    @Enumerated(EnumType.STRING)
    private MatchStatus status;
    private LocalDateTime time;
    private Long externalApiId;
    public Match() {
    }

    public Match(Team hometeam, Team awayteam, double homeOdds, double drawOdds, double awayOdds,Long externalApiId,int homeGoals,int awayGoals,MatchStatus status) {
        this.homeTeam = hometeam;
        this.awayTeam = awayteam;
        this.homeOdds = homeOdds;
        this.drawOdds = drawOdds;
        this.awayOdds = awayOdds;
        this.result = null;
        this.time = LocalDateTime.now();
        this.externalApiId = externalApiId;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team hometeam) {
        this.homeTeam = hometeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayteam) {
        this.awayTeam = awayteam;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public double getHomeOdds() {
        return homeOdds;
    }

    public void setHomeOdds(double homeOdds) {
        this.homeOdds = homeOdds;
    }

    public double getDrawOdds() {
        return drawOdds;
    }

    public void setDrawOdds(double drawOdds) {
        this.drawOdds = drawOdds;
    }

    public double getAwayOdds() {
        return awayOdds;
    }

    public void setAwayOdds(double awayOdds) {
        this.awayOdds = awayOdds;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Long getExternalApiId() {
        return externalApiId;
    }

    public void setExternalApiId(Long externalApiId) {
        this.externalApiId = externalApiId;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(int homeGoals) {
        this.homeGoals = homeGoals;
    }

    public int getAwayGoals() {
        return awayGoals;
    }

    public void setAwayGoals(int awayGoals) {
        this.awayGoals = awayGoals;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }
}
