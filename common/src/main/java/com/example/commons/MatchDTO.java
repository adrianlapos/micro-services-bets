package com.example.commons;

import java.time.LocalDateTime;

public record MatchDTO(Long matchId,TeamDTO homeTeam, TeamDTO awayTeam, double homeOdds, double drawOdds, double awayOdds,
                       LocalDateTime startTime,Result result) {
}
