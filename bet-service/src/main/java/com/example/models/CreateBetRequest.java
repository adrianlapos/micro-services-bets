package com.example.models;

import com.example.commons.Guess;

public record CreateBetRequest(
        Long matchId,
        Guess guess
) {
}
