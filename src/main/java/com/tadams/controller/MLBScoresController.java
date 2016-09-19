package com.tadams.controller;

import com.tadams.exception.MLBGameServiceNotAvailable;
import com.tadams.exception.NoGamesForDate;
import com.tadams.model.GamesForDate;
import com.tadams.service.MLBGamesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class MLBScoresController {

    private static final Logger logger = LoggerFactory.getLogger(MLBScoresController.class);

    @Autowired
    MLBGamesService gamesService;

    List<String> errorMsgs = new ArrayList<>();

    @RequestMapping(value = "/scores", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView showScoresForDate(@RequestParam(value = "gameDate", required = false) String gameDateString) {

        if (Objects.isNull(gameDateString)) {
            return defaultModelAndView();
        }

        List<String> errors = new ArrayList<>();
        GamesForDate gamesForDate = getGamesForDate(gameDateString, errors);
        ModelAndView modelAndView = new ModelAndView("mlb-scores");

        modelAndView.addObject("errors", errors);
        modelAndView.addObject("gamesForDate", gamesForDate);

        return modelAndView;
    }

    private ModelAndView defaultModelAndView() {

        ModelAndView modelAndView = new ModelAndView("mlb-scores");

        modelAndView.addObject("errors", new ArrayList<String>());
        modelAndView.addObject("gamesForDate", GamesForDate.empty());

        return modelAndView;
    }

    private GamesForDate getGamesForDate(String gameDateString, List<String> errors) {
        Optional<LocalDate> gameDate = parseGameDate(gameDateString, errors);
        if (!gameDate.isPresent()) {
            return GamesForDate.empty();
        }

        try {
            return gamesService.getGamesByDate(gameDate.get());
        } catch(MLBGameServiceNotAvailable | NoGamesForDate e) {
            errors.add(e.getMessage());
            return GamesForDate.empty();
        }
    }

    private Optional<LocalDate> parseGameDate(String gameDateString, List<String> errors) {
        if (StringUtils.isEmpty(gameDateString)) {
            errors.add("No date for games entered");
            return Optional.empty();
        }

        try {
            return Optional.of(LocalDate.parse(gameDateString));
        } catch (DateTimeParseException e) {
            logger.error("Unable to parse game date {}", gameDateString);
            errors.add("Invalid date " + gameDateString);
            return Optional.empty();
        }
    }
}
