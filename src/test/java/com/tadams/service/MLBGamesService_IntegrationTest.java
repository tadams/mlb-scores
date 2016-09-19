package com.tadams.service;

import com.tadams.Integration;
import com.tadams.config.WebMvcConfig;
import com.tadams.model.GamesForDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Category(Integration.class)
public class MLBGamesService_IntegrationTest {

    MLBGamesService service = new MLBGamesService();

    @Before
    public void given() {
        WebMvcConfig config = new WebMvcConfig();
        service.restTemplate = config.restTemplate();
    }

    @Test
    public void shouldCallMlbService_AndReturnData() throws Exception {
        GamesForDate gamesForDate = service.getGamesByDate(LocalDate.of(2016, 7, 4));

        assertThat(gamesForDate.getGames().size()).isGreaterThan(5);
    }

}