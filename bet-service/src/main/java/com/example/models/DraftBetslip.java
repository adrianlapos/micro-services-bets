package com.example.models;

import java.util.List;

public record DraftBetslip(Long id, double stake, List<CreateBetRequest> bets) {
}
