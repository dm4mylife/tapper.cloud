package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import common.Actions;
import io.qameta.allure.Attachment;
import io.qameta.allure.Description;
import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

public class BaseTest {

    public static WebDriver driver = Actions.createWebDriver();

    protected BasePage basePage = new BasePage(driver);

    @AfterAll
    static void teardown() {
        driver.quit();
    }

    @BeforeEach
    static void screens() {
        SelenideLogger.addListener("AllureSelenide", New AllureSelenide());
    }



}


