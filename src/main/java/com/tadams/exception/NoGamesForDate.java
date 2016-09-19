package com.tadams.exception;

import java.time.LocalDate;

public class NoGamesForDate extends Exception {
    public NoGamesForDate(LocalDate date) {
        super("No games found for date: " + date);
    }
}
