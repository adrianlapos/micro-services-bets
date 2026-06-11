package com.example.service;

import com.example.commons.Guess;
import com.example.models.BetslipDTO;
import com.example.models.BetslipMapper;
import com.example.repository.BetslipRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BetslipService {
    private final BetslipRepository betslipRepository;

    public BetslipService(BetslipRepository betslipRepository){
        this.betslipRepository = betslipRepository;
    }

    public BetslipDTO getBetslipWithId(Long id) {
        return betslipRepository.findById(id)
                .map(BetslipMapper::maptoDTO)
                .orElseThrow(() -> new EntityNotFoundException("Betslip not found with id: " + id));
    }

    public List<BetslipDTO> getUsersBetslips(Long userId) {
        return betslipRepository.findByUserId(userId)
                .stream()
                .map(BetslipMapper::maptoDTO)
                .toList();
    }

//    public BetslipDTO createBetslip(Map<Long, Guess>tips ,double stake){
//
//    })
}
