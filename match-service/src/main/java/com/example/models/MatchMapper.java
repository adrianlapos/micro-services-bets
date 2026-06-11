package com.example.models;

import com.example.commons.MatchDTO;
import com.example.commons.TeamDTO;
import com.example.entity.Match;
import com.example.entity.Team;

public class MatchMapper {

    public static MatchDTO maptoDTO(Match match) {
        return new MatchDTO(match.getId(), TeamMapper.maptoDTO(match.getHomeTeam()),TeamMapper.maptoDTO(match.getAwayTeam()),match.getHomeOdds(),match.getDrawOdds(),match.getAwayOdds(),match.getTime(),match.getResult());
    }
}

