package com.tadams.exception;

public class MLBGameServiceNotAvailable extends Exception {
    public MLBGameServiceNotAvailable() {
        super("Unable to connect to MLB game service");
    }
}
