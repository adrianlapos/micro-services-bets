package com.example.models;

public record CreateMatchRequest(
        Long homeTeamId,
        Long awayTeamId,
        double homeOdds,
        double drawOdds,
        double awayOdds
) {}