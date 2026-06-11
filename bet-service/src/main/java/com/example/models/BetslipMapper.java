package com.example.models;

import com.example.entity.Bet;
import com.example.entity.Betslip;

import java.util.List;

public class BetslipMapper {

    public static BetslipDTO maptoDTO(Betslip betslip){
        return new BetslipDTO(betslip.getUserId(),betslip.getBets().stream().map(BetMapper::maptoDTO).toList(),betslip.getStake(),betslip.getCreatedAt(),betslip.getBetslipStatus());
    }

    public static List<BetslipDTO> maptoDTOs(List<Betslip> betslips){
        return betslips.stream().map(BetslipMapper::maptoDTO).toList();
    }
}
