package com.example.models;

import com.example.entity.Bet;

import java.util.List;

public class BetMapper {

    public static BetDTO maptoDTO(Bet betslip){
        return new BetDTO(betslip.getMatchId(),betslip.getGuess(),betslip.getBetStatus());
    }

    public static List<BetDTO> maptoDTOs(List<Bet> betslips){
        return betslips.stream().map(BetMapper::maptoDTO).toList();
    }
}
