package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import static common.ConfigDriver.PLATFORM_AND_BROWSER;


public class BaseTest {


    @BeforeAll
    public static void setUp() {

        if (PLATFORM_AND_BROWSER.equals("win+chrome")) {

            WebDriverManager.chromedriver().setup();
            Configuration.browser = "chrome";
            Configuration.driverManagerEnabled = true;
            Configuration.browserSize = "1920x1080";

        } else {

            Assertions.fail("Incorrect type or browser name" + PLATFORM_AND_BROWSER);
        }


    }


    @AfterAll
    static void tearDown() {
        Selenide.closeWebDriver();
    }

    @BeforeAll
    static void setupAllureReports() {
        SelenideLogger.addListener("AllureSelenide",  new AllureSelenide().screenshots(true).savePageSource(false));
    }

}


