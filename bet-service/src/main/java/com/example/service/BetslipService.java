package com.example.service;

import com.example.entity.Bet;
import com.example.entity.Betslip;
import com.example.models.*;
import com.example.repository.BetslipRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class BetslipService {
    private final BetslipRepository betslipRepository;
    private final DraftBetslipService draftBetslipService;
    private static final double DEFAULT_STAKE = 0.5;
    public BetslipService(BetslipRepository betslipRepository,DraftBetslipService draftBetslipService) {
        this.betslipRepository = betslipRepository;
        this.draftBetslipService = draftBetslipService;
    }

    public BetslipDTO getBetslipById(Long id){
        return betslipRepository.findById(id).map(BetslipMapper::maptoDTO).orElseThrow(EntityNotFoundException::new);
    }

    public DraftBetslip createBetslip(Long userId){
//        Betslip betslip = new Betslip(userId,betslipRequest.stake());
//
//        for (CreateBetRequest br : betslipRequest.betRequests()){
//            betslip.addBet(new Bet(br.matchId(),br.guess()));
//        }
//        return BetslipMapper.maptoDTO(
//                betslipRepository.save(betslip)
//        );
        DraftBetslip draftBetslip=  draftBetslipService.getDraft(userId);
        if (draftBetslip == null) {
            throw new IllegalStateException("No draft betslip found");
        }
        Betslip betslip = new Betslip(userId,draftBetslip.stake());
        for (CreateBetRequest br : draftBetslip.bets()){
            betslip.addBet(new Bet(br.matchId(),br.guess()));
        }
        betslipRepository.save(betslip);
        draftBetslipService.deleteDraft(userId);
        DraftBetslip newDraft = new DraftBetslip(
                userId,
                DEFAULT_STAKE,
                new ArrayList<>()
        );

        draftBetslipService.saveDraft(userId, newDraft);
        return newDraft;
    }

    public DraftBetslip addBetToDraft(CreateBetRequest createBetRequest, Long userId) {

        DraftBetslip draft = getUsersDraftBetslip(userId);

        List<CreateBetRequest> updatedBets =
                new ArrayList<>(draft.bets());

        updatedBets.add(createBetRequest);

        DraftBetslip updated = new DraftBetslip(
                draft.id(),
                draft.stake(),
                updatedBets
        );

        draftBetslipService.saveDraft(userId, updated);
        return updated;
    }

    public DraftBetslip removeBetFromDraft(Long matchId,Long userId){
        DraftBetslip draftBetslip=  draftBetslipService.getDraft(userId);
        if (draftBetslip == null) {
            throw new IllegalStateException("No draft betslip found");
        }
        DraftBetslip updated = new DraftBetslip(draftBetslip.id(),draftBetslip.stake(),draftBetslip.bets().stream().filter((b) -> !b.matchId().equals(matchId)).toList());
        draftBetslipService.saveDraft(userId,updated);
        return updated;
    }


    public DraftBetslip updateDraftStake(double stake,Long userId){
        DraftBetslip draftBetslip = draftBetslipService.getDraft(userId);
        if (draftBetslip == null) {
            throw new IllegalStateException("No draft betslip found");
        }
        DraftBetslip updated = new DraftBetslip(draftBetslip.id(),stake,draftBetslip.bets());
        draftBetslipService.saveDraft(userId,updated);
        return  updated;
    }
    public List<BetslipDTO> getBetslipsByUserId(Long userId){
        return betslipRepository.findByUserId(userId).stream().map(BetslipMapper::maptoDTO).toList();
    }

    public DraftBetslip getUsersDraftBetslip(Long userId) {
        DraftBetslip draft = draftBetslipService.getDraft(userId);

        if (draft != null) {
            return draft;
        }

        DraftBetslip newDraft = new DraftBetslip(
                userId,
                DEFAULT_STAKE,
                new ArrayList<>()
        );

        draftBetslipService.saveDraft(userId, newDraft);

        return newDraft;
    }
}
