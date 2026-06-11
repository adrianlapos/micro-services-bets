package com.example.models;

import java.util.List;

public record CreateBetslipRequest(
        List<CreateBetRequest> betRequests,
        double stake
) {
}
