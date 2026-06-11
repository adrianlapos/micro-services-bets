package com.example.repository;

import com.example.commons.Result;
import com.example.entity.Match;
import com.example.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match,Long> {
    List<Match> findByTimeAfter(LocalDateTime time);
    List<Match> findByTimeBefore(LocalDateTime time);
    @Query("SELECT m FROM Match m WHERE m.time BETWEEN :start AND :end")
    List<Match> findMatchesBetween(@Param("start") LocalDateTime start,
                                   @Param("end") LocalDateTime end);
    List<Match> findByHomeTeam(Team homeTeam);
    List<Match> findByAwayTeam(Team awayTeam);
    List<Match> findByResult(Result result);
    //List<Match> findByHomeTeamOrAwayTeam(Team homeTeam, Team awayTeam);
    @Query("""
    SELECT m
    FROM Match m
    WHERE m.homeTeam = :team
       OR m.awayTeam = :team
""")
    List<Match> findAllMatchesForTeam(@Param("team") Team team);
    Optional<Match> findByExternalApiId(Long id);
}
