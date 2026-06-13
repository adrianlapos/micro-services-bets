package com.example.service;

import com.example.models.DraftBetslip;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Service
public class DraftBetslipService {
    private final RedisTemplate<String,Object> redisTemplate;
    public DraftBetslipService(RedisTemplate<String,Object> redisTemplate,MatchStatusFeignClient matchStatusFeignClient){
        this.redisTemplate = redisTemplate;

    }

    public void saveDraft(Long userId,DraftBetslip draftBetslip){
        redisTemplate.opsForValue().set("draft-betslip:"+userId,draftBetslip, Duration.of(2, ChronoUnit.HOURS));
    }

    public DraftBetslip getDraft(Long userId){
        return (DraftBetslip) redisTemplate.opsForValue().get("draft-betslip:" + userId);
    }

    public void deleteDraft(Long userId) {
        redisTemplate.delete("draft-betslip:" + userId);
    }
}
