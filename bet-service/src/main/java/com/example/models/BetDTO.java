package com.example.models;

import com.example.commons.BetStatus;
import com.example.commons.Guess;

public record BetDTO(Long matchId, Guess guess, BetStatus status) {
}
