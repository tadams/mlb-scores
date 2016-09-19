package com.tadams.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@lombok.Data
public class GamesForDate {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final GamesForDate EMPTY = new GamesForDate();

    private String     date     = "None";
    @JsonProperty(value = "game")
    private List<Game> games    = Collections.emptyList();

    public static GamesForDate empty() {
        return EMPTY;
    }

    public boolean isNotEmpty() {
        return !games.isEmpty();
    }

    public Date getGamesDate() {
        LocalDate gameDate = LocalDate.parse(date, DATE_FORMATTER);
        return Date.from(gameDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}

