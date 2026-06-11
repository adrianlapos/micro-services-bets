package com.example.service;

import com.example.repository.BetRepository;
import com.example.entity.Bet;
import com.example.models.BetDTO;
import com.example.models.BetMapper;
import com.example.models.BetRequest;
import org.springframework.stereotype.Service;

@Service
public class BetService {
    private final BetRepository betRepository;

    public BetService(BetRepository betRepository){
        this.betRepository = betRepository;
    }

    public BetDTO addBet(BetRequest betRequest){
        return BetMapper.maptoDTO(betRepository.save(new Bet(betRequest.matchId(),betRequest.guess())));
    }


}
