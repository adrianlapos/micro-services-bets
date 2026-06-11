package com.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OddsResponse(
        @JsonProperty("response")
        List<OddsFixtureResponse> oddsFixtureResponses) {
}
