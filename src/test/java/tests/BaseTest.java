package tests;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.qameta.allure.selenide.LogType;
import org.junit.jupiter.api.*;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static constants.Constant.TestData.IPHONE12PRO;


public class BaseTest {

    @BeforeAll
    static void browserLogs() {


        ChromeOptions chromeOptions = new ChromeOptions();
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(String.valueOf(LogType.BROWSER), Level.ALL);
        logPrefs.enable(String.valueOf(LogType.PERFORMANCE), Level.ALL);
        chromeOptions.setCapability("goog:loggingPrefs", logPrefs);
        Configuration.browserCapabilities = chromeOptions;

    }



    @BeforeAll
    @DisplayName("Установка, настройка, инициализиция браузера")
     static void setUp() {


        Configuration.driverManagerEnabled = true;
        Configuration.headless = true;
        //Configuration.browserSize = "1920x1080";
        Configuration.browserSize = IPHONE12PRO;
        ChromeOptions options = new ChromeOptions();
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

    @BeforeAll
    @DisplayName("Установка отчёта")
    static void setupAllureReports() {
        SelenideLogger.addListener("AllureSelenide",  new AllureSelenide().screenshots(true).savePageSource(false));
    }



}


