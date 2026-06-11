package com.example.models;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record Fixture(Long id,
                      OffsetDateTime date,
                      Status status) {
}
