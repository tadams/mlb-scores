package com.tadams.service;

import com.tadams.exception.MLBGameServiceNotAvailable;
import com.tadams.exception.NoGamesForDate;
import com.tadams.model.Game;
import com.tadams.model.GameResponse;
import com.tadams.model.GamesForDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MLBGamesServiceTest {

    @Mock
    RestTemplate restTemplate;
    @Mock
    GameResponse gameResponse;
    @InjectMocks
    MLBGamesService service;

    GamesForDate gamesForDate = new GamesForDate();
    LocalDate gameDate = LocalDate.of(2018, 8, 1);

    @Before
    public void given() {
        when(gameResponse.getGamesForDate()).thenReturn(gamesForDate);
        when(restTemplate.getForObject(anyString(), eq(GameResponse.class)))
                .thenReturn(gameResponse);
    }

    @Test
    public void shouldReturnGamesFromAPICall() throws Exception {
        List<Game> games = Arrays.asList(new Game(), new Game());
        gamesForDate.setGames(games);

        GamesForDate gamesByDate = service.getGamesByDate(gameDate);

        assertThat(gamesByDate.getGames()).isEqualTo(games);
    }

    @Test(expected = NoGamesForDate.class)
    public void shouldThrowNoGames_WhenGamesIsEmpty() throws Exception {
        gamesForDate.setGames(Collections.emptyList());

        service.getGamesByDate(gameDate);
    }

    @Test(expected = NoGamesForDate.class)
    public void shouldThrowNoGames_WhenAPIReturns404() throws Exception {
        HttpClientErrorException http404Error = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        when(restTemplate.getForObject(anyString(), eq(GameResponse.class)))
                .thenThrow(http404Error);

        service.getGamesByDate(gameDate);
    }

    @Test(expected = MLBGameServiceNotAvailable.class)
    public void shouldThrowServiceNotAvail_WhenAPIReturns_Non404Error() throws Exception {
        HttpClientErrorException http403Error = new HttpClientErrorException(HttpStatus.FORBIDDEN);
        when(restTemplate.getForObject(anyString(), eq(GameResponse.class)))
                .thenThrow(http403Error);

        service.getGamesByDate(gameDate);
    }

    @Test(expected = MLBGameServiceNotAvailable.class)
    public void shouldThrowServiceNotAvail_WhenRestClientExceptionThrown() throws Exception {
        when(restTemplate.getForObject(anyString(), eq(GameResponse.class)))
                .thenThrow(new RestClientException("Unit Test"));

        service.getGamesByDate(gameDate);
    }

}