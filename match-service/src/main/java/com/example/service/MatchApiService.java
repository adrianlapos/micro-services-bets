package com.example.service;

import com.example.commons.MatchFinishedEvent;
import com.example.commons.MatchStatus;
import com.example.commons.Result;
import com.example.entity.Match;
import com.example.entity.Team;
import com.example.models.*;
import com.example.repository.MatchRepository;
import com.example.repository.TeamRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class MatchApiService {

    private final MatchFeignClient matchFeignClient;
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final MatchEvaluationService matchEvaluationService;
    private final MatchEventProducer matchEventProducer;
    public MatchApiService(
            MatchFeignClient matchFeignClient,
            MatchRepository matchRepository,
            TeamRepository teamRepository,
            MatchEvaluationService matchEvaluationService,
            MatchEventProducer matchEventProducer
    ) {
        this.matchFeignClient = matchFeignClient;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.matchEvaluationService = matchEvaluationService;
        this.matchEventProducer = matchEventProducer;
    }

    @Transactional
    @Scheduled(fixedRate = 1800000)
    public void fetchMatches() {
        FootballResponse footballResponse = matchFeignClient.fixtures(LocalDate.now().toString());
        System.out.println("Response for fixtures: " + footballResponse);
        OddsResponse oddsResponse = matchFeignClient.odds(LocalDate.now().toString());
        System.out.println("Response for odds: " + oddsResponse);
        for (FixtureResponse fr : footballResponse.response()) {

            Long fixtureId = fr.fixture().id();

            TeamHelper home = fr.teams().home();
            TeamHelper away = fr.teams().away();

            String apiStatus = fr.fixture().status().shortCode();
            MatchStatus status = convertApiStatus(apiStatus);
            OffsetDateTime  offsetDateTime =   fr.fixture().date();
            LocalDateTime kickoff = offsetDateTime.toLocalDateTime();

            int homeGoals = Optional.ofNullable(fr.goals())
                    .map(g -> g.home() == null ? 0 : g.home())
                    .orElse(0);

            int awayGoals = Optional.ofNullable(fr.goals())
                    .map(g -> g.away() == null ? 0 : g.away())
                    .orElse(0);

            Team homeTeam = teamRepository
                    .findByExternalApiId(home.id())
                    .orElseGet(() ->
                            teamRepository.save(
                                    new Team(home.name(), null, home.id())
                            )
                    );

            Team awayTeam = teamRepository
                    .findByExternalApiId(away.id())
                    .orElseGet(() ->
                            teamRepository.save(
                                    new Team(away.name(), null, away.id())
                            )
                    );

            Match match = matchRepository
                    .findByExternalApiId(fixtureId)
                    .orElseGet(() ->
                            new Match(
                                    homeTeam,
                                    awayTeam,
                                    0,
                                    0,
                                    0,
                                    fixtureId,
                                    homeGoals,
                                    awayGoals,
                                    status
                            )
                    );

            match.setHomeTeam(homeTeam);
            match.setAwayTeam(awayTeam);
            match.setTime(kickoff);
            match.setStatus(status);


            match.setHomeGoals(homeGoals);
            match.setAwayGoals(awayGoals);
            matchRepository.save(match);

            if (status == MatchStatus.FINISHED) {
                Result result = matchEvaluationService.evaluateMatch(match);
                if (result != null){
                matchEventProducer.sendMatchFinished(new MatchFinishedEvent(match.getId(), match.getExternalApiId(),result,match.getHomeGoals(),match.getAwayGoals()));
                }
            }
        }
        if (oddsResponse == null || oddsResponse.oddsFixtureResponses() == null) {
            return;
        }
        for (OddsFixtureResponse fixture : oddsResponse.oddsFixtureResponses()) {

            Long fixtureId = fixture.fixture().id();

            Match match = matchRepository
                    .findByExternalApiId(fixtureId)
                    .orElse(null);

            if (match == null) continue;
            if (match.getStatus() != MatchStatus.NOTSTARTED) {
                continue;
            }
            fixture.bookmakers().stream()
                    .filter(b -> "bet365".equalsIgnoreCase(b.name()))
                    .flatMap(b -> b.bets().stream())
                    .filter(b -> "Match Winner".equals(b.name()))
                    .findFirst()
                    .ifPresent(market -> {

                        for (OddValueDTO odd : market.values()) {

                            switch (odd.value()) {

                                case "Home" ->
                                        match.setHomeOdds(Double.parseDouble(odd.odd()));

                                case "Draw" ->
                                        match.setDrawOdds(Double.parseDouble(odd.odd()));

                                case "Away" ->
                                        match.setAwayOdds(Double.parseDouble(odd.odd()));
                            }
                        }

                        matchRepository.save(match);
                    });
        }
    }

    private static MatchStatus convertApiStatus(String shortcode){
        if (shortcode == null)
            return MatchStatus.NOTSTARTED;
        return switch(shortcode){
            case "NS" -> MatchStatus.NOTSTARTED;
            case "FT" -> MatchStatus.FINISHED;
            case "1H", "2H", "HT", "LIVE" -> MatchStatus.LIVE;
            default -> MatchStatus.NOTSTARTED;
        };
    }
}