package com.example.models;

import java.util.List;

public record BetMarketDTO(String name,
                           List<OddValueDTO> values) {
}
