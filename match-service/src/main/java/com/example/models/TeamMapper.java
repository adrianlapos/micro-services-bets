package com.example.models;

import com.example.commons.TeamDTO;
import com.example.entity.Team;

import java.util.List;

public class TeamMapper {

    public static TeamDTO maptoDTO(Team team) {
        return new TeamDTO(team.getName(), team.getStadium());

    }
}
