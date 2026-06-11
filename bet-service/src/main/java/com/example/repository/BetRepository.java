package com.example.repository;

import com.example.entity.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetRepository extends JpaRepository<Bet,Long> {
    List<Bet> findByMatchId(Long matchId);

}
