package com.tadams.functest;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class ScoresPage {

    private static final String URL = String.format("http://localhost:%s/mlb-scores/scores", getPort());
    private static final By BY_DATE_INPUT = By.cssSelector("input[name='gameDate']");
    private static final By BY_ERROR = By.className("error");

    private final WebDriver webDriver;

    private static String getPort() {
        return System.getProperty("mlb-scores.port", "8080");
    }

    public void loadScoresPage() {
        webDriver.get(URL);
    }

    public void getScoresForDate(String gameDate) {
        loadScoresPage();

        WebElement dateInput = webDriver.findElement(BY_DATE_INPUT);

        dateInput.sendKeys(gameDate);
        dateInput.submit();
    }

    public String getTitle() {
        return webDriver.getTitle();
    }

    public int getGameCount() {
        List<String> games = buildGames();
        return games.size();
    }

    public List<String> getScoreOf(String team1, String team2) {
        return buildGames().stream()
                           .filter(game -> game.contains(team1) && game.contains(team2))
                           .map(game -> {
                               String[] values = game.split("\\|");
                               return values[1] + "-" + values[3];
                           })
                           .collect(toList());
    }

    private List<String> buildGames() {
        List<WebElement> rows = webDriver.findElements(By.cssSelector("tr"));
        Pattern pattern = Pattern.compile("([^0-9]+)(\\d+)([^0-9]*)(\\d+).*");
        return rows.stream()
                   .map(WebElement::getText)
                   .filter(text -> !text.contains("Team"))
                   .map(text -> {
                       Matcher matcher = pattern.matcher(text);
                       return matcher.replaceAll("$1|$2|$3|$4");
                   })
                   .collect(toList());
    }

    public String getError() {
        return webDriver.findElement(BY_ERROR).getText();
    }
}
