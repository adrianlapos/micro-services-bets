package com.example.service;

import com.example.commons.MatchDTO;
import com.example.commons.TeamDTO;
import com.example.entity.Match;
import com.example.entity.Team;
import com.example.models.CreateMatchRequest;
import com.example.models.MatchMapper;
import com.example.models.TeamCreationRequest;
import com.example.models.TeamMapper;
import com.example.repository.MatchRepository;
import com.example.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;

    public MatchService(MatchRepository matchRepository,TeamRepository teamRepository){
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
    }

//    public TeamDTO addTeam(TeamCreationRequest teamCreationRequest){
//        return TeamMapper.maptoDTO(teamRepository.save(new Team(teamCreationRequest.name(),teamCreationRequest.stadium())));
//    }


    @Transactional
    public TeamDTO modifyTeam(Long id,TeamCreationRequest request){
        Team team = teamRepository.findById(id).orElseThrow(() ->new EntityNotFoundException("Team with the ID not found"));
        if (!team.getName().equals(request.name()) ||
                !team.getStadium().equals(request.stadium())) {
            team.setName(request.name());
            team.setStadium(request.stadium());
        }
        return TeamMapper.maptoDTO(team);
    }
//    @Transactional
//    public MatchDTO createMatch(CreateMatchRequest createMatchRequest){
//        Long homeTeamId = createMatchRequest.homeTeamId();
//        Long awayTeamId = createMatchRequest.awayTeamId();
//        if (homeTeamId.equals(awayTeamId)) {
//            throw new IllegalArgumentException("A team cannot play against itself");
//        }
//        if (createMatchRequest.homeOdds() <= 0 ||
//                createMatchRequest.drawOdds() <= 0 ||
//                createMatchRequest.awayOdds() <= 0) {
//            throw new IllegalArgumentException("Odds must be greater than 0");
//        }
//        Team homeTeam = teamRepository.findById(homeTeamId).orElseThrow(()-> new EntityNotFoundException("Team with id: " + homeTeamId  + " not found"));
//        Team awayTeam = teamRepository.findById(awayTeamId).orElseThrow(()-> new EntityNotFoundException("Team with id: " + awayTeamId  + " not found"));
//        Match match = matchRepository.save(new Match(homeTeam,awayTeam,createMatchRequest.homeOdds(),createMatchRequest.drawOdds(),createMatchRequest.awayOdds(),null));
//        return MatchMapper.maptoDTO(match);
//    }

    public List<MatchDTO> getAllMatches(){
        return matchRepository.findAll().stream().map(MatchMapper::maptoDTO).toList();
    }


    public List<MatchDTO> getAllMatchesBefore(LocalDateTime time){
        return matchRepository.findByTimeBefore(time).stream().map(MatchMapper::maptoDTO).toList();
    }

    public List<MatchDTO> getAllMatchesAfter(LocalDateTime time){
        return matchRepository.findByTimeAfter(time).stream().map(MatchMapper::maptoDTO).toList();
    }

    public List<MatchDTO> getAllMatchesBetween(LocalDateTime timeBefore,LocalDateTime timeAfter){
        return matchRepository.findMatchesBetween(timeBefore,timeAfter).stream().map(MatchMapper::maptoDTO).toList();
    }


}
