package com.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Status(
        @JsonProperty("short")
        String shortCode
) {}