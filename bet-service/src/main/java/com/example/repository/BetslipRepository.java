package com.example.repository;

import com.example.commons.BetslipStatus;
import com.example.entity.Betslip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BetslipRepository extends JpaRepository<Betslip,Long> {
//    @Query("select b from Betslip b where b.betslipStatus  = :status")
//    List<Betslip> findByStatus(@Param("status") BetslipStatus status);
    List<Betslip> findByBetslipStatus(BetslipStatus status);
    List<Betslip> findByUserId(Long userId);
    List<Betslip> findByUserIdAndBetslipStatus(Long userId, BetslipStatus status);
    List<Betslip> findByStakeGreaterThan(double stake);
    List<Betslip> findByStakeLessThan(double stake);
    List<Betslip> findByUserIdAndStakeGreaterThan(Long userId,double stake);
    List<Betslip> findByUserIdAndStakeLessThan(Long userId,double stake);
    List<Betslip> findByCreatedAtAfter(LocalDateTime date);
    List<Betslip> findByCreatedAtBefore(LocalDateTime date);
    List<Betslip> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    @Query("""
    SELECT DISTINCT b
    FROM Betslip b
    JOIN b.bets bet
    WHERE bet.matchId = :matchId
    """)
    List<Betslip> findBetslipsByMatchId(@Param("matchId") Long matchId);
}
