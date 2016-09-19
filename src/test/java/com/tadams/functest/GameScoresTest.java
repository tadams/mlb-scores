package com.tadams.functest;

import com.google.common.base.MoreObjects;
import com.google.common.base.Supplier;
import com.tadams.Functional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Category(Functional.class)
public class GameScoresTest {

    @RequiredArgsConstructor
    @Getter
    enum Driver {
        HTML_UNIT(HtmlUnitDriver::new),
        FIRE_FOX(FirefoxDriver::new);

        private final Supplier<WebDriver> driver;

        public static WebDriver getWebDriver() {
            String driver = MoreObjects.firstNonNull(System.getProperty("WEB_DRIVER"),
                                                     Driver.FIRE_FOX.name());
            System.out.println("Running selenium  with web driver: " + driver);
            try {
                return Driver.valueOf(driver).driver.get();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return FIRE_FOX.driver.get();
            }
        }
    }

    static final int DOUBLE_HITTER = 2;
    static WebDriver webDriver;
    static ScoresPage scoresPage;

    @BeforeClass
    public static void given() {
        webDriver = Driver.getWebDriver();
        webDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        scoresPage = new ScoresPage(webDriver);
    }

    @AfterClass
    public static void cleanUp() {
        webDriver.close();
    }

    @Test
    public void shouldLoadGamesForDatePage() {
        scoresPage.loadScoresPage();
        String pageTitle = scoresPage.getTitle();
        assertThat(pageTitle).isEqualTo("MLB Game Scores");
    }

    @Test
    public void shouldGetScoresForDate() {
        scoresPage.getScoresForDate("2016-08-11");

        assertThat(scoresPage.getGameCount()).isEqualTo(11);

        List<String> rockiesRangersGame = scoresPage.getScoreOf("Rockies", "Rangers");
        assertThat(rockiesRangersGame.get(0)).isEqualTo("12-9");

        List<String> astrosTwins = scoresPage.getScoreOf("Astros", "Twins");
        assertThat(astrosTwins).hasSize(DOUBLE_HITTER);
        assertThat(astrosTwins.get(0)).isEqualTo("15-7");
        assertThat(astrosTwins.get(1)).isEqualTo("10-2");
    }

    @Test
    public void shouldShowErrorWhenNoGamesForDateFound() {
        scoresPage.getScoresForDate("2001-01-01");

        assertThat(scoresPage.getError()).contains("No games found");
    }

    @Test
    public void shouldShowErrorWhenDateInvalid() {
        scoresPage.getScoresForDate("2001");

        assertThat(scoresPage.getError()).contains("Invalid date");
    }
}
