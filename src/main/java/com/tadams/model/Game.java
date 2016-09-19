package com.tadams.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Game {

    String homeTeamRuns;
    String homeTeamName;
    @JsonProperty(value = "home_name_abbrev")
    String homeTeamNameCode;
    String awayTeamRuns;
    String awayTeamName;
    @JsonProperty(value = "away_name_abbrev")
    String awayTeamNameCode;
}
