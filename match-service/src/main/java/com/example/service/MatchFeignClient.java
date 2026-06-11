package com.example.service;

import com.example.models.FootballResponse;
import com.example.models.OddsResponse;
import jakarta.websocket.server.PathParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "match-api",url = "${football.api.url}",configuration = ApiFeignFootbalConfig.class)
public interface MatchFeignClient {
    @GetMapping("/fixtures")
    FootballResponse fixtures(@RequestParam("date") String date);
    @GetMapping("/odds")
    OddsResponse odds(@RequestParam("date") String date);
}
