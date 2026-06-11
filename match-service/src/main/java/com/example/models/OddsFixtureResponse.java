package com.example.models;

import java.util.List;

public record OddsFixtureResponse(OddsFixture fixture,
                                  List<BookMakerDTO> bookmakers) {
}
