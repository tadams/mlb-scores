package com.tadams.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class GameResponse {

    @lombok.Data
    static class Data {
        @JsonProperty(value = "games")
        private  GamesForDate gamesForDate;
    }

    private Data data;

    public GamesForDate getGamesForDate() {
        return data.getGamesForDate();
    }
}
