package com.example.controller;

import com.example.commons.MatchDTO;
import com.example.commons.MatchStatus;
import com.example.service.MatchApiService;
import com.example.service.MatchReconciliationService;
import com.example.service.MatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/matches")
public class MatchController {
    private final MatchService matchService;
    private final MatchApiService matchApiService;
    private final MatchReconciliationService matchEvaluationService;
    public MatchController(MatchService matchService,MatchApiService matchApiService,MatchReconciliationService matchEvaluationService){
        this.matchService = matchService;
        this.matchApiService = matchApiService;
        this.matchEvaluationService = matchEvaluationService;
    }

//    @PostMapping("")
//    public ResponseEntity<MatchDTO> createMatch(@RequestBody  CreateMatchRequest createMatchRequest, @RequestHeader("X-Role")String role){
//        if (!"ADMIN".equals(role))
//            throw new RuntimeException("You have no rights to create matches");
//        return new ResponseEntity<>(matchService.createMatch(createMatchRequest), HttpStatus.CREATED);
//    }
//
//    @PostMapping("/teams")
//    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamCreationRequest teamCreationRequest, @RequestHeader("X-Role") String role){
//        if (!"ADMIN".equals(role))
//            throw new RuntimeException("You have no rights to create teams");
//        return new ResponseEntity<>(matchService.addTeam(teamCreationRequest), HttpStatus.CREATED);
//    }

    @GetMapping("/all")
    public ResponseEntity<List<MatchDTO>> getMatches(){
        return new ResponseEntity<>(matchService.getAllMatches(),HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<String> fetchMatches(){
        matchApiService.fetchMatches();
        return new ResponseEntity<>("matches fetched",HttpStatus.OK);
    }
    @GetMapping("/test2")
    public ResponseEntity<String> closeFinishedMatches(){
        matchEvaluationService.reconcileMatches();
        return new ResponseEntity<>("matches closed",HttpStatus.OK);
    }

    @GetMapping("status/{id}")
    public ResponseEntity<MatchStatus> getMatchStatus(@PathVariable("id") Long id){
        MatchStatus matchStatus = matchService.getMatchStatus(id);
        if (matchStatus == null){
            throw new RuntimeException("Match status could not be retrieved");
        }
        else{
            return new ResponseEntity<>(matchStatus,HttpStatus.OK);
        }
    }
}
