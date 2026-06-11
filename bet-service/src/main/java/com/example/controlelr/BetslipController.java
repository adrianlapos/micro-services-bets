package com.example.controlelr;

import com.example.entity.Bet;
import com.example.models.*;
import com.example.service.BetslipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestScope;

import java.util.List;

@RestController
@RequestMapping("/api/betslips")
public class BetslipController {
    private final BetslipService betslipService;

    public BetslipController(BetslipService betslipService){
        this.betslipService = betslipService;
    }

    @GetMapping("/mybetslips")
    public ResponseEntity<List<BetslipDTO>> getUsersBetslips(@RequestHeader("X-User-Id") Long userId){
        return new ResponseEntity<>(betslipService.getBetslipsByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/mydraft")
    public ResponseEntity<DraftBetslip> getUserBetslipDraft(@RequestHeader("X-User-Id") Long userId){
        return new ResponseEntity<>(betslipService.getUsersDraftBetslip(userId), HttpStatus.OK);
    }

//    @PostMapping("/create")
//    public ResponseEntity<BetslipDTO> createBetslip(@RequestHeader("X-User-Id") Long userId,@RequestBody  CreateBetslipRequest createBetslipRequest){
//        return new ResponseEntity<BetslipDTO>(betslipService.createBetslip(userId),HttpStatus.CREATED);
//    }

    @PostMapping("/addmatch")
    public ResponseEntity<DraftBetslip> addMatchToDraft(@RequestHeader("X-User-Id") Long userId, @RequestBody CreateBetRequest createBetRequest){
        return new ResponseEntity<>(betslipService.addBetToDraft(createBetRequest,userId),HttpStatus.OK);
    }

    @DeleteMapping("/removematch/{matchId}")
    public ResponseEntity<DraftBetslip> removeMatchFromDraft(@RequestHeader("X-User-Id") Long userId,@PathVariable("matchId") Long matchId ){
        return new ResponseEntity<>(betslipService.removeBetFromDraft(matchId,userId),HttpStatus.OK);
    }

    @PostMapping("/updatestake")
    public ResponseEntity<DraftBetslip> updateDraftStake(@RequestHeader("X-User-Id") Long userId, @RequestBody StakeUpdateRequest stakeUpdateRequest){
        return new ResponseEntity<>(betslipService.updateDraftStake(stakeUpdateRequest.stake(),userId),HttpStatus.OK);
    }

    @PostMapping("/submitdraft")
    public ResponseEntity<DraftBetslip> submitDraftBetslip(@RequestHeader("X-User-Id") Long userId){
        return new ResponseEntity<>(betslipService.createBetslip(userId),HttpStatus.CREATED);
    }
}
