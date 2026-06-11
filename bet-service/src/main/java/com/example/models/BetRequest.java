package com.example.models;

import com.example.commons.Guess;

public record BetRequest(Long matchId, Guess guess) {
}
