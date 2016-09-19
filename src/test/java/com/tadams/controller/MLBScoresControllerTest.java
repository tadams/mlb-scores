package com.tadams.controller;

import com.tadams.exception.NoGamesForDate;
import com.tadams.model.GamesForDate;
import com.tadams.service.MLBGamesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MLBScoresControllerTest {

    @Mock
    MLBGamesService gamesService;
    @InjectMocks
    MLBScoresController controller;

    String gameDateStr = "2016-08-01";
    LocalDate gameDate = LocalDate.parse(gameDateStr);
    GamesForDate gamesForDate = new GamesForDate();

    @Test
    public void shouldReturnData_NoErrors() throws Exception {
        when(gamesService.getGamesByDate(gameDate)).thenReturn(gamesForDate);
        ModelAndView modelAndView = controller.showScoresForDate(gameDateStr);

        assertThat(modelAndView.getViewName()).isEqualTo("mlb-scores");
        assertThat(getErrors(modelAndView)).isEmpty();
        assertThat(getGamesForDate(modelAndView)).isSameAs(gamesForDate);
    }

    @Test
    public void shouldReturnNoDateForGames_Error() throws Exception {
        ModelAndView modelAndView = controller.showScoresForDate("");

        assertThat(modelAndView.getViewName()).isEqualTo("mlb-scores");
        List<String> errors = getErrors(modelAndView);
        assertThat(errors).hasSize(1);
        assertThat(errors.get(0)).startsWith("No date");
        assertThat(getGamesForDate(modelAndView)).isSameAs(GamesForDate.empty());
    }

    @Test
    public void shouldReturnInvalidDateForGames_Error() throws Exception {
        ModelAndView modelAndView = controller.showScoresForDate("123");

        assertThat(modelAndView.getViewName()).isEqualTo("mlb-scores");
        List<String> errors = getErrors(modelAndView);
        assertThat(errors).hasSize(1);
        assertThat(errors.get(0)).startsWith("Invalid date");
        assertThat(getGamesForDate(modelAndView)).isSameAs(GamesForDate.empty());
    }

    @Test
    public void shouldReturnErrorWhenServiceExceptionOccurs() throws Exception {
        when(gamesService.getGamesByDate(gameDate))
                .thenThrow(new NoGamesForDate(gameDate));
        ModelAndView modelAndView = controller.showScoresForDate(gameDateStr);

        assertThat(modelAndView.getViewName()).isEqualTo("mlb-scores");
        List<String> errors = getErrors(modelAndView);
        assertThat(errors).hasSize(1);
        assertThat(errors.get(0)).startsWith("No games found");
        assertThat(getGamesForDate(modelAndView)).isSameAs(GamesForDate.empty());
    }

    @SuppressWarnings("unchecked")
    private List<String> getErrors(ModelAndView modelAndView) {
        return (List<String>) modelAndView.getModel().get("errors");
    }

    private GamesForDate getGamesForDate(ModelAndView modelAndView) {
        return (GamesForDate) modelAndView.getModel().get("gamesForDate");
    }
}