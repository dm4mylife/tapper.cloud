package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import common.Actions;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

public class BaseTest {

    public static WebDriver driver = Actions.createWebDriver();

    protected BasePage basePage = new BasePage(driver);

    @AfterAll
    static void teardown() {
        driver.quit();
    }

    @BeforeAll
    static void setupAllureReports() {
        SelenideLogger.addListener("AllureSelenide",  new AllureSelenide().screenshots(true).savePageSource(false));
    }

}


