package com.example.models;

import com.example.commons.BetslipStatus;

import java.time.LocalDateTime;
import java.util.List;

public record BetslipDTO(Long userId,List<Long> bets, double stake, LocalDateTime createdAt, BetslipStatus betslipStatus) {

}
