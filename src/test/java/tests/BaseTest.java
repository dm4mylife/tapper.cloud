package tests;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.logging.Level;

import static constants.Constant.TestData.IPHONE12PRO;


public class BaseTest {

    @BeforeAll
    static void setUp() {

        Configuration.driverManagerEnabled = true;
        Configuration.headless = false;
        //Configuration.browserSize = "1920x1080";
        Configuration.browserSize = IPHONE12PRO;
        ChromeOptions options = new ChromeOptions();

        DesiredCapabilities capabilities = new DesiredCapabilities();
        ChromeOptions chromeOptions = new ChromeOptions();
        LoggingPreferences logPrefs = new LoggingPreferences();

        logPrefs.enable(org.openqa.selenium.logging.LogType.BROWSER, Level.ALL);
        logPrefs.enable(org.openqa.selenium.logging.LogType.PERFORMANCE, Level.ALL);
        capabilities.setCapability("goog:loggingPrefs", logPrefs);
        capabilities.setCapability(ChromeOptions.CAPABILITY, capabilities);

        Configuration.browserCapabilities = chromeOptions;

        options.addArguments("--enable-automation");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--no-sandbox");
        options.addArguments("--incognito");

        //options.addArguments("--disable-gpu");
        //options.addArguments("--auto-open-devtools-for-tabs");

        Configuration.browserCapabilities = options;

    }

    @AfterAll
    @DisplayName("Закрытие браузера")
    static void tearDown() {
        Selenide.closeWebDriver();
    }

    @BeforeEach
    void setupAllureReports() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));
    }


}


