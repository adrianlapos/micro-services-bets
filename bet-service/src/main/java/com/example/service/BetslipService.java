package com.example.service;

import com.example.commons.Guess;
import com.example.commons.MatchStatus;
import com.example.entity.Bet;
import com.example.entity.Betslip;
import com.example.models.*;
import com.example.repository.BetslipRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BetslipService {
    private final BetslipRepository betslipRepository;
    private final DraftBetslipService draftBetslipService;
    private static final double DEFAULT_STAKE = 0.5;
    private final MatchStatusFeignClient matchStatusFeignClient;
    public BetslipService(BetslipRepository betslipRepository,DraftBetslipService draftBetslipService,MatchStatusFeignClient matchStatusFeignClient) {
        this.betslipRepository = betslipRepository;
        this.draftBetslipService = draftBetslipService;
        this.matchStatusFeignClient = matchStatusFeignClient;
    }

    public BetslipDTO getBetslipById(Long id){
        return betslipRepository.findById(id).map(BetslipMapper::maptoDTO).orElseThrow(EntityNotFoundException::new);
    }

    public CreateBetslipResponse createBetslip(Long userId){
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

        List<MatchStatus> matchStatuses = new ArrayList<>();
        betslip.getBets().forEach(ms-> matchStatuses.add(matchStatusFeignClient.getMatchStatus(ms.getMatchId())));
        if (matchStatuses.stream().allMatch(m -> MatchStatus.NOTSTARTED == m)) {
            betslipRepository.save(betslip);
            draftBetslipService.deleteDraft(userId);
            DraftBetslip newDraft = new DraftBetslip(
                    userId,
                    DEFAULT_STAKE,
                    new ArrayList<>()

            );
            draftBetslipService.saveDraft(userId, newDraft);
            return new CreateBetslipResponse(newDraft,new ArrayList<>());

        }
        else{
            List<Long> matchesStarted = new ArrayList<>();
            List<Bet> filteredBets = new ArrayList<>();
            betslip.getBets().stream().forEach((b)-> {if (matchStatusFeignClient.getMatchStatus(b.getMatchId()) == MatchStatus.NOTSTARTED)
                filteredBets.add(b);
                else{matchesStarted.add(b.getMatchId());}
            });
            betslip.setBets(filteredBets);
            List<CreateBetRequest> createBetRequests = filteredBets.stream().map(b -> new CreateBetRequest(b.getMatchId(),b.getGuess())).toList();
            DraftBetslip updated = new DraftBetslip(
                    draftBetslip.id(),
                    draftBetslip.stake(),
                    createBetRequests
            );
            draftBetslipService.saveDraft(userId,updated);
            return new CreateBetslipResponse(updated,matchesStarted);
            }
        }



    public DraftBetslip addBetToDraft(CreateBetRequest createBetRequest, Long userId) {

        MatchStatus matchStatus = matchStatusFeignClient.getMatchStatus(createBetRequest.matchId());
        if (matchStatus != MatchStatus.NOTSTARTED)
            throw new IllegalStateException("This match is not available for betting");
        DraftBetslip draft = getUsersDraftBetslip(userId);
        boolean alreadyExists = draft.bets().stream().anyMatch((br)-> Objects.equals(br.matchId(), createBetRequest.matchId()));
        if (alreadyExists)
            return updateBetOnBetslip(userId,createBetRequest.matchId(),createBetRequest.guess());
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

    public DraftBetslip updateBetOnBetslip(Long userId, Long matchId, Guess guess){
        DraftBetslip draftBetslip = getUsersDraftBetslip(userId);
        CreateBetRequest createBetRequest = draftBetslip.bets().stream().filter(cbr -> Objects.equals(cbr.matchId(), matchId)).findFirst().orElse(null);
        if (createBetRequest != null){
            draftBetslip.bets().remove(createBetRequest);
            draftBetslip.bets().add(new CreateBetRequest(matchId,guess));
        }
        draftBetslipService.saveDraft(userId,draftBetslip);
        return  draftBetslip;
    }
}
