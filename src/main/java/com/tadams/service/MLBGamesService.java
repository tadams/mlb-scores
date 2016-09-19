package com.tadams.service;

import com.tadams.controller.MLBScoresController;
import com.tadams.exception.MLBGameServiceNotAvailable;
import com.tadams.exception.NoGamesForDate;
import com.tadams.model.GameResponse;
import com.tadams.model.GamesForDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404;

@Service
public class MLBGamesService {
    private static final     Logger logger             = LoggerFactory.getLogger(MLBScoresController.class);
    private static final String MLB_GAMES_FOR_DATE_URL = "http://gd2.mlb.com/components/game/mlb/" +
                                                         "year_%02d/month_%02d/day_%02d/miniscoreboard.json";
    @Autowired
    protected RestTemplate restTemplate;

    public GamesForDate getGamesByDate(LocalDate gameDate) throws NoGamesForDate, MLBGameServiceNotAvailable {

        String gamesForDateUrl = String.format(MLB_GAMES_FOR_DATE_URL,
                                               gameDate.getYear(), gameDate.getMonthValue(), gameDate.getDayOfMonth());

        try {
            GameResponse gameResponse = restTemplate.getForObject(gamesForDateUrl, GameResponse.class);

            if (gameResponse.getGamesForDate().isNotEmpty()) {
                return gameResponse.getGamesForDate();
            }
            throw new NoGamesForDate(gameDate);

        } catch(HttpClientErrorException httpError) {
            if (httpError.getStatusCode().value() == NOT_FOUND_404) {
                throw new NoGamesForDate(gameDate);
            }
            logger.warn("Exception calling MLB Game Service", httpError);
            throw new MLBGameServiceNotAvailable();
        } catch (RestClientException e) {
            logger.warn("Exception calling MLB Game Service", e);
            throw new MLBGameServiceNotAvailable();
        }
    }
}
