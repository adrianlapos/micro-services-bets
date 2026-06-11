package com.example.models;

import java.util.List;

public record BookMakerDTO(String name, List<BetMarketDTO> bets) {
}
